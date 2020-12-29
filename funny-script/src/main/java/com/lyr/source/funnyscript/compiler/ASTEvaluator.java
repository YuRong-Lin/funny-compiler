package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.parser.FunnyScriptBaseVisitor;
import com.lyr.source.funnyscript.parser.FunnyScriptParser;

import java.util.Stack;

/**
 * AST解释器。利用语义信息(AnnotatedTree)，在AST上解释执行脚本。
 *
 * @Author LinYuRong
 * @Date 2020/12/29 15:08
 * @Version 1.0
 */
public class ASTEvaluator extends FunnyScriptBaseVisitor<Object> {

    private AnnotatedTree at;

    // 栈桢的管理
    private Stack<StackFrame> stack = new Stack<>();

    protected boolean traceStackFrame = false;

    protected boolean traceFunctionCall = false;

    // 堆，用于保存对象
    public ASTEvaluator(AnnotatedTree at) {
        this.at = at;
    }

    /**
     * 栈桢入栈。
     * 其中最重要的任务，是要保证栈桢的parentFrame设置正确。否则，
     * (1)随着栈的变深，查找变量的性能会降低；
     * (2)甚至有可能找错栈桢，比如在递归(直接或间接)的场景下。
     *
     * @param frame
     */
    private void pushStack(StackFrame frame) {
        if (stack.size() > 0) {
            //从栈顶到栈底依次查找
            for (int i = stack.size() - 1; i > 0; i--) {
                StackFrame f = stack.get(i);

                /*
                如果新加入的栈桢，跟某个已有的栈桢的enclosingScope是一样的，那么这俩的parentFrame也一样。
                因为它们原本就是同一级的嘛。
                比如：
                void foo(){};
                void bar(foo());

                或者：
                void foo();
                if (...){
                    foo();
                }
                */
                if (f.scope.enclosingScope == frame.scope.enclosingScope) {
                    frame.parentFrame = f.parentFrame;
                    break;
                }

                /*
                如果新加入的栈桢，是某个已有的栈桢的下一级，那么就把把这个父子关系建立起来。比如：
                void foo(){
                    if (...){  //把这个块往栈桢里加的时候，就符合这个条件。
                    }
                }
                再比如,下面的例子:
                class MyClass{
                    void foo();
                }
                MyClass c = MyClass();  //先加Class的栈桢，里面有类的属性，包括父类的
                c.foo();                //再加foo()的栈桢
                 */
                else if (f.scope == frame.scope.enclosingScope) {
                    frame.parentFrame = f;
                    break;
                }

                /*
                这是针对函数可能是一等公民的情况。这个时候，函数运行时的作用域，与声明时的作用域会不一致。
                我在这里设计了一个“receiver”的机制，意思是这个函数是被哪个变量接收了。要按照这个receiver的作用域来判断。
                 */
                else if (frame.object instanceof FunctionObject) {
                    FunctionObject functionObject = (FunctionObject) frame.object;
                    if (functionObject.receiver != null && functionObject.receiver.enclosingScope == f.scope) {
                        frame.parentFrame = f;
                        break;
                    }
                }
            }

            if (frame.parentFrame == null) {
                frame.parentFrame = stack.peek();
            }
        }

        stack.push(frame);

        if (traceStackFrame) {
            dumpStackFrame();
        }
    }

    private void popStack() {
        stack.pop();
    }

    public LValue getLValue(Variable variable) {
        StackFrame f = stack.peek();

        FunnyObject valueContainer = null;
        while (f != null) {
            if (f.scope.containsSymbol(variable)) { //对于对象来说，会查找所有父类的属性
                valueContainer = f.object;
                break;
            }
            f = f.parentFrame;
        }

        // 通过正常的作用域找不到，就从闭包里找
        // 原理：FunnyObject中可能有一些变量，其作用域跟StackFrame.scope是不同的。
        if (valueContainer == null) {
            f = stack.peek();
            while (f != null) {
                if (f.contains(variable)) {
                    valueContainer = f.object;
                    break;
                }
                f = f.parentFrame;
            }
        }

        MyLValue lvalue = new MyLValue(valueContainer, variable);

        return lvalue;
    }

    /**
     * 程序执行入口
     *
     * @param ctx
     * @return
     */
    @Override
    public Object visitProg(FunnyScriptParser.ProgContext ctx) {
        pushStack(new StackFrame((BlockScope) at.node2Scope.get(ctx)));

        Object rtn = visitBlockStatements(ctx.blockStatements());

        popStack();

        return rtn;
    }

    @Override
    public Object visitBlockStatements(FunnyScriptParser.BlockStatementsContext ctx) {
        Object rtn = null;
        for (FunnyScriptParser.BlockStatementContext child : ctx.blockStatement()) {
            rtn = visitBlockStatement(child);

            // 如果返回的是break，那么不执行下面的语句
            if (rtn instanceof BreakObject) {
                break;
            }

            // 碰到Return, 退出函数
            // TODO 要能层层退出一个个block，弹出一个栈桢
            else if (rtn instanceof ReturnObject) {
                break;
            }
        }
        return rtn;
    }

    @Override
    public Object visitBlockStatement(FunnyScriptParser.BlockStatementContext ctx) {
        Object rtn = null;
        if (ctx.variableDeclarators() != null) {
            rtn = visitVariableDeclarators(ctx.variableDeclarators());
        } else if (ctx.statement() != null) {
            rtn = visitStatement(ctx.statement());
        }
        return rtn;
    }

    @Override
    public Object visitVariableDeclarators(FunnyScriptParser.VariableDeclaratorsContext ctx) {
        Object rtn = null;
        for (FunnyScriptParser.VariableDeclaratorContext child : ctx.variableDeclarator()) {
            rtn = visitVariableDeclarator(child);
        }
        return rtn;
    }

//    @Override
//    public Object visitStatement(FunnyScriptParser.StatementContext ctx) {
//
//    }

    @Override
    public Object visitVariableDeclarator(FunnyScriptParser.VariableDeclaratorContext ctx) {
        Object rtn = null;
        LValue lValue = (LValue) visitVariableDeclaratorId(ctx.variableDeclaratorId());
        if (ctx.variableInitializer() != null) {
            rtn = visitVariableInitializer(ctx.variableInitializer());
            if (rtn instanceof LValue) {
                rtn = ((LValue) rtn).getValue();
            }
            lValue.setValue(rtn);
        }
        return rtn;
    }

    @Override
    public Object visitVariableDeclaratorId(FunnyScriptParser.VariableDeclaratorIdContext ctx) {
        Symbol symbol = at.symbolOfNode.get(ctx);
        Object rtn = getLValue((Variable) symbol);
        return rtn;
    }

//    @Override
//    public Object visitVariableInitializer(FunnyScriptParser.VariableInitializerContext ctx) {
//
//    }

    private void dumpStackFrame() {
        System.out.println("\nStack Frames ----------------");
        for (StackFrame frame : stack) {
            System.out.println(frame);
        }
        System.out.println("-----------------------------\n");
    }

    //////////////////////////////////////////
    // 自己实现的左值对象

    private final class MyLValue implements LValue {
        private Variable variable;
        private FunnyObject valueContainer;

        public MyLValue(FunnyObject valueContainer, Variable variable) {
            this.valueContainer = valueContainer;
            this.variable = variable;
        }

        @Override
        public Object getValue() {
            // 对于this或super关键字，直接返回这个对象，应该是ClassObject
            if (variable instanceof This || variable instanceof Super) {
                return valueContainer;
            }

            return valueContainer.getValue(variable);
        }

        @Override
        public void setValue(Object value) {
            valueContainer.setValue(variable, value);

            // 如果variable是函数型变量，那改变functionObject.receiver
            if (value instanceof FunctionObject) {
                ((FunctionObject) value).receiver = variable;
            }
        }

        @Override
        public Variable getVariable() {
            return variable;
        }

        @Override
        public String toString() {
            return "LValue of " + variable.name + " : " + getValue();
        }

        @Override
        public FunnyObject getValueContainer() {
            return valueContainer;
        }
    }
}
