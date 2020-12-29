package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.parser.FunnyScriptBaseListener;
import com.lyr.source.funnyscript.parser.FunnyScriptParser;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.LinkedList;
import java.util.List;

/**
 * 语义解析的第三步：引用消解和类型推断
 * 1.解析所有的本地变量引用、函数调用和类成员引用。
 * 2.类型推断：从下而上推断表达式的类型。
 * <p>
 * 这两件事要放在一起做，因为：
 * (1)对于变量，只有做了消解，才能推断出类型来。
 * (2)对于FunctionCall，只有把参数（表达式)的类型都推断出来，才能匹配到正确的函数（方法)。
 * (3)表达式里包含FunctionCall,所以要推导表达式的类型，必须知道是哪个Function，从而才能得到返回值。
 *
 * @Author LinYuRong
 * @Date 2020/12/28 10:25
 * @Version 1.0
 */
public class RefResolver extends FunnyScriptBaseListener {

    private AnnotatedTree at;

    // 用于把本地变量添加到符号表，并计算类型
    private ParseTreeWalker typeResolverWalker = new ParseTreeWalker();
    private TypeResolver typeResolver;

    // this()和super()构造函数留到最后去消解，因为它可能引用别的构造函数，必须等这些构造函数都消解完。
    private List<FunnyScriptParser.FunctionCallContext> thisConstructorList = new LinkedList<>();
    private List<FunnyScriptParser.FunctionCallContext> superConstructorList = new LinkedList<>();

    protected RefResolver(AnnotatedTree at) {
        this.at = at;
        this.typeResolver = new TypeResolver(at, true);
    }

    /**
     * 把本地变量加到符号表。本地变量必须是边添加，边解析，不能先添加后解析，否则会引起引用消解的错误。
     *
     * @param ctx
     */
    @Override
    public void enterVariableDeclarators(FunnyScriptParser.VariableDeclaratorsContext ctx) {
        Scope scope = at.enclosingScopeOfNode(ctx);
        // TODO 函数本地变量？？
        if (scope instanceof BlockScope) {
            typeResolverWalker.walk(typeResolver, ctx);
        }
    }

    /**
     * 根据字面量推断
     *
     * @param ctx
     */
    @Override
    public void exitLiteral(FunnyScriptParser.LiteralContext ctx) {
        if (ctx.BOOL_LITERAL() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Boolean);
        } else if (ctx.CHAR_LITERAL() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Char);
        } else if (ctx.NULL_LITERAL() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Null);
        } else if (ctx.STRING_LITERAL() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.String);
        } else if (ctx.integerLiteral() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Integer);
        } else if (ctx.floatLiteral() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Float);
        }
    }

    @Override
    public void exitPrimary(FunnyScriptParser.PrimaryContext ctx) {
        Type type = null;

        if (ctx.IDENTIFIER() != null) {
            Scope scope = at.enclosingScopeOfNode(ctx);
            String idName = ctx.IDENTIFIER().getText();

            Variable variable = at.lookupVariable(scope, idName);
            if (variable == null) {
                // 看看是不是函数，因为函数可以作为值来传递。这个时候，函数重名没法区分。
                // 因为普通Scope中的函数是不可以重名的，所以这应该是没有问题的。  //TODO 但如果允许重名，那就不行了。
                // TODO 注意，查找function的时候，可能会把类的方法包含进去
                Function function = at.lookupFunction(scope, idName);
                if (function != null) {
                    at.symbolOfNode.put(ctx, function);
                    type = function;
                } else {
                    at.log("unknown variable or function: " + idName, ctx);
                }
            } else {
                at.symbolOfNode.put(ctx, variable);
                type = variable.type;
            }
        } else if (ctx.literal() != null) {
            type = at.typeOfNode.get(ctx.literal());
        } else if (ctx.expression() != null) {
            type = at.typeOfNode.get(ctx.expression());
        } else if (ctx.THIS() != null) {
            Class theClass = at.enclosingClassOfNode(ctx);
            if (theClass != null) {
                This variable = theClass.getThis();
                at.symbolOfNode.put(ctx, variable);
                type = theClass;
            } else {
                at.log("keyword \"this\" can only be used inside a class", ctx);
            }
        } else if (ctx.SUPER() != null) {
            Class theClass = at.enclosingClassOfNode(ctx);
            if (theClass != null) {
                Super variable = theClass.getSuper();
                at.symbolOfNode.put(ctx, variable);
                type = theClass;
            } else {
                at.log("keyword \"super\" can only be used inside a class", ctx);
            }
        }
        // 类型推断，冒泡
        at.typeOfNode.put(ctx, type);
    }

    /**
     * @param ctx
     */
    @Override
    public void exitExpression(FunnyScriptParser.ExpressionContext ctx) {
        Type type = null;

        // 消解处理点符号表达式的层层引用
        if (ctx.bop != null && ctx.bop.getType() == FunnyScriptParser.DOT) {
            // 这是个左递归，要不断的把左边的节点的计算结果存到symbolOfNode，所以要在exitExpression里操作
            Symbol symbol = at.symbolOfNode.get(ctx.expression(0));
            if (symbol instanceof Variable && ((Variable) symbol).type instanceof Class) {
                Class theClass = (Class) ((Variable) symbol).type;
                // 引用类的属性
                if (ctx.IDENTIFIER() != null) {
                    String idName = ctx.IDENTIFIER().getText();
                    Variable variable = at.lookupVariable(theClass, idName);
                    if (variable != null) {
                        at.symbolOfNode.put(ctx, variable);
                        type = variable.type;
                    } else {
                        at.log("unable to find field " + idName + " in Class " + theClass.name, ctx);
                    }
                } else if (ctx.functionCall() != null) {
                    type = at.typeOfNode.get(ctx.functionCall());
                }
            } else {
                at.log("symbol is not a qualified object：" + symbol, ctx);
            }
        }

        //变量引用冒泡： 如果下级是一个变量，往上冒泡传递，以便在点符号表达式中使用
        //也包括This和Super的冒泡
        if (ctx.primary() != null) {
            Symbol symbol = at.symbolOfNode.get(ctx.primary());
            at.symbolOfNode.put(ctx, symbol);

            type = at.typeOfNode.get(ctx.primary());
        } else if (ctx.functionCall() != null) {
            type = at.typeOfNode.get(ctx.functionCall());
        } else if (ctx.bop != null && ctx.expression().size() >= 2) {
            Type type1 = at.typeOfNode.get(ctx.expression(0));
            Type type2 = at.typeOfNode.get(ctx.expression(1));

            switch (ctx.bop.getType()) {
                case FunnyScriptParser.ADD:
                    if (type1 == PrimitiveType.String || type2 == PrimitiveType.String) {
                        type = PrimitiveType.String;
                    } else if (type1 instanceof PrimitiveType && type2 instanceof PrimitiveType) {
                        //类型“向上”对齐，比如一个int和一个float，取float
                        type = PrimitiveType.getUpperType(type1, type2);
                    } else {
                        at.log("operand should be PrimitiveType for additive and multiplicative operation", ctx);
                    }
                    break;
                case FunnyScriptParser.SUB:
                case FunnyScriptParser.MUL:
                case FunnyScriptParser.DIV:
                    if (type1 instanceof PrimitiveType && type2 instanceof PrimitiveType) {
                        //类型“向上”对齐，比如一个int和一个float，取float
                        type = PrimitiveType.getUpperType(type1, type2);
                    } else {
                        at.log("operand should be PrimitiveType for additive and multiplicative operation", ctx);
                    }

                    break;
                case FunnyScriptParser.EQUAL:
                case FunnyScriptParser.NOTEQUAL:
                case FunnyScriptParser.LE:
                case FunnyScriptParser.LT:
                case FunnyScriptParser.GE:
                case FunnyScriptParser.GT:
                case FunnyScriptParser.AND:
                case FunnyScriptParser.OR:
                case FunnyScriptParser.BANG:
                    type = PrimitiveType.Boolean;
                    break;
                case FunnyScriptParser.ASSIGN:
                case FunnyScriptParser.ADD_ASSIGN:
                case FunnyScriptParser.SUB_ASSIGN:
                case FunnyScriptParser.MUL_ASSIGN:
                case FunnyScriptParser.DIV_ASSIGN:
                case FunnyScriptParser.AND_ASSIGN:
                case FunnyScriptParser.OR_ASSIGN:
                case FunnyScriptParser.XOR_ASSIGN:
                case FunnyScriptParser.MOD_ASSIGN:
                case FunnyScriptParser.LSHIFT_ASSIGN:
                case FunnyScriptParser.RSHIFT_ASSIGN:
                case FunnyScriptParser.URSHIFT_ASSIGN:
                    type = type1;
                    break;
            }
        }

        //类型冒泡
        at.typeOfNode.put(ctx, type);
    }

    /**
     * 对变量初始化部分也做一下类型推断
     *
     * @param ctx
     */
    @Override
    public void exitVariableInitializer(FunnyScriptParser.VariableInitializerContext ctx) {
        if (ctx.expression() != null) {
            at.typeOfNode.put(ctx, at.typeOfNode.get(ctx.expression()));
        }
    }

    @Override
    public void exitFunctionCall(FunnyScriptParser.FunctionCallContext ctx) {
        if (ctx.THIS() != null) {
            thisConstructorList.add(ctx);
            return;
        } else if (ctx.SUPER() != null) {
            superConstructorList.add(ctx);
            return;
        }

        // TODO 临时代码，支持println
        if (ctx.IDENTIFIER().getText().equals("println")) {
            return;
        }

        String idName = ctx.IDENTIFIER().getText();

        List<Type> paramTypes = getParamTypes(ctx);

        boolean found = false;

        // .表达式调用类的方法
        if (ctx.parent != null && ctx.parent instanceof FunnyScriptParser.ExpressionContext) {
            FunnyScriptParser.ExpressionContext exp = (FunnyScriptParser.ExpressionContext) ctx.parent;
            if (exp.bop != null && exp.bop.getType() == FunnyScriptParser.DOT) {
                Symbol symbol = at.symbolOfNode.get(exp.expression(0));
                if (symbol instanceof Variable && ((Variable) symbol).type instanceof Class) {
                    Class theClass = (Class) ((Variable) symbol).type;
                    Function function = theClass.getFunction(idName, paramTypes);
                    if (function != null) {
                        found = true;
                        at.symbolOfNode.put(ctx, function);
                        at.typeOfNode.put(ctx, function.getReturnType());
                    } else {
                        Variable funcVar = theClass.getFunctionVariable(idName, paramTypes);
                        if (funcVar != null) {
                            found = true;
                            at.symbolOfNode.put(ctx, funcVar);
                            at.typeOfNode.put(ctx, ((FunctionType) funcVar.type).getReturnType());
                        } else {
                            at.log("unable to find method " + idName + " in Class " + theClass.name, exp);
                        }
                    }
                } else {
                    at.log("unable to resolve a class", ctx);
                }
            }
        }

        Scope scope = at.enclosingScopeOfNode(ctx);

        if (!found) {
            Function function = at.lookupFunction(scope, idName, paramTypes);
            if (function != null) {
                found = true;
                at.symbolOfNode.put(ctx, function);
                at.typeOfNode.put(ctx, function.getReturnType());
            }
        }

        if (!found) {
            // 看看是不是类的构建函数，用相同的名称查找一个class
            Class theClass = at.lookupClass(scope, idName);
            if (theClass != null) {
                Function function = theClass.findConstructor(paramTypes);
                if (function != null) {
                    at.symbolOfNode.put(ctx, function);

                    // 如果是与类名相同的方法，并且没有参数，那么就是缺省构造方法
                } else if (ctx.expressionList() == null) {
                    at.symbolOfNode.put(ctx, theClass.defaultConstructor());
                } else {
                    at.log("unknown class constructor: " + ctx.getText(), ctx);
                }
                at.typeOfNode.put(ctx, theClass);
            } else {
                // 看看是不是一个函数型的变量
                Variable variable = at.lookupFunctionVariable(scope, idName, paramTypes);
                if (variable != null && variable.type instanceof FunctionType) {
                    at.symbolOfNode.put(ctx, variable);
                    at.typeOfNode.put(ctx, variable.type);
                } else {
                    at.log("unknown function or function variable: " + ctx.getText(), ctx);
                }
            }
        }
    }

    /**
     * 在结束扫描之前，把this()和super()构造函数消解掉
     *
     * @param ctx
     */
    @Override
    public void exitProg(FunnyScriptParser.ProgContext ctx) {
        for (FunnyScriptParser.FunctionCallContext fcc : thisConstructorList) {
            resolveThisConstructorCall(fcc);
        }

        for (FunnyScriptParser.FunctionCallContext fcc : superConstructorList) {
            resolveSuperConstructorCall(fcc);
        }
    }

    /**
     * this构造函数消解
     *
     * @param ctx
     */
    private void resolveThisConstructorCall(FunnyScriptParser.FunctionCallContext ctx) {
        Class theClass = at.enclosingClassOfNode(ctx);
        if (theClass != null) {
            Function function = at.enclosingFunctionOfNode(ctx);
            if (function != null && function.isConstructor()) {
                // 检查是不是构造函数中的第一句
                FunnyScriptParser.FunctionDeclarationContext fdx = (FunnyScriptParser.FunctionDeclarationContext) function.ctx;
                if (!firstStatementInFunction(fdx, ctx)) {
                    at.log("this() must be first statement in a constructor", ctx);
                    return;
                }

                List<Type> paramTypes = getParamTypes(ctx);
                Function referred = theClass.findConstructor(paramTypes);
                if (referred != null) {
                    at.symbolOfNode.put(ctx, referred);
                    at.typeOfNode.put(ctx, theClass);
                    at.thisConstructorRef.put(function, referred);
                    // 默认构造函数
                } else if (paramTypes.size() == 0) {
                    at.symbolOfNode.put(ctx, theClass.defaultConstructor());
                    at.typeOfNode.put(ctx, theClass);
                    at.thisConstructorRef.put(function, theClass.defaultConstructor());
                } else {
                    at.log("can not find a constructor matches this()", ctx);
                }
            } else {
                at.log("this() should only be called inside a class constructor", ctx);
            }
        } else {
            at.log("this() should only be called inside a class", ctx);
        }
    }

    /**
     * super构造函数消解
     * TODO 对于调用super()是有要求的，比如：
     * (1)必须出现在构造函数的第一行，
     * (2)this()和super不能同时出现，等等。
     *
     * @param ctx
     */
    private void resolveSuperConstructorCall(FunnyScriptParser.FunctionCallContext ctx) {
        Class theClass = at.enclosingClassOfNode(ctx);
        if (theClass != null) {
            Function function = at.enclosingFunctionOfNode(ctx);
            if (function != null && function.isConstructor()) {
                Class parentClass = theClass.getParentClass();
                if (parentClass != null) {
                    //检查是不是构造函数中的第一句
                    FunnyScriptParser.FunctionDeclarationContext fdx = (FunnyScriptParser.FunctionDeclarationContext) function.ctx;
                    if (!firstStatementInFunction(fdx, ctx)) {
                        at.log("super() must be first statement in a constructor", ctx);
                        return;
                    }

                    List<Type> paramTypes = getParamTypes(ctx);
                    Function referred = parentClass.findConstructor(paramTypes);
                    if (referred != null) {
                        at.symbolOfNode.put(ctx, referred);
                        at.typeOfNode.put(ctx, theClass);
                        at.superConstructorRef.put(function, referred);
                    } else if (paramTypes.size() == 0) {
                        //缺省构造函数
                        at.symbolOfNode.put(ctx, parentClass.defaultConstructor());
                        at.typeOfNode.put(ctx, theClass);
                        at.superConstructorRef.put(function, theClass.defaultConstructor());
                    } else {
                        at.log("can not find a constructor matches this()", ctx);
                    }
                } else {
                    //父类是最顶层的基类。
                    // TODO 这里暂时不处理
                }
            } else {
                at.log("super() should only be called inside a class constructor", ctx);
            }
        } else {
            at.log("super() should only be called inside a class", ctx);
        }
    }

    /**
     * 是否是函数中的第一句
     *
     * @param fdx
     * @param ctx
     * @return
     */
    private boolean firstStatementInFunction(FunnyScriptParser.FunctionDeclarationContext fdx,
                                             FunnyScriptParser.FunctionCallContext ctx) {
        FunnyScriptParser.StatementContext statementContext =
                fdx.functionBody().block().blockStatements().blockStatement(0).statement();
        if (statementContext != null
                && statementContext.expression() != null
                && statementContext.expression().functionCall() == ctx) {
            return true;
        }
        return false;
    }

    /**
     * 获得函数的参数列表
     *
     * @param ctx
     * @return
     */
    private List<Type> getParamTypes(FunnyScriptParser.FunctionCallContext ctx) {
        List<Type> paramTypes = new LinkedList<>();
        if (ctx.expressionList() != null) {
            for (FunnyScriptParser.ExpressionContext exp : ctx.expressionList().expression()) {
                Type type = at.typeOfNode.get(exp);
                paramTypes.add(type);
            }
        }
        return paramTypes;
    }
}
