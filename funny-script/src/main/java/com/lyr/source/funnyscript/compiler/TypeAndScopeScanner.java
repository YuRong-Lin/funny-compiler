package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.parser.FunnyScriptBaseListener;
import com.lyr.source.funnyscript.parser.FunnyScriptParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Stack;

/**
 * 第一遍扫描：识别出所有类型（包括类和函数)，以及Scope。
 * 注：
 *  1、但函数的参数信息要等到下一个阶段才会添加进去。
 *  2、函数作为一等公民，支持变量声明、赋值、参数传递等
 *
 * @Author LinYuRong
 * @Date 2020/12/24 15:49
 * @Version 1.0
 */
public class TypeAndScopeScanner extends FunnyScriptBaseListener {
    // 注释树，保存语义分析的结果
    private AnnotatedTree at;

    private Stack<Scope> scopeStack = new Stack<>();

    public TypeAndScopeScanner(AnnotatedTree at) {
        this.at = at;
    }

    /**
     * 遍历AST过程中，当前的Scope
     *
     * @return
     */
    private Scope currentScope() {
        if (scopeStack.size() > 0) {
            return scopeStack.peek();
        }
        return null;
    }

    /**
     * 弹出当前scope
     *
     * @return
     */
    private Scope popScope() {
        return scopeStack.pop();
    }

    /**
     * 1、建立节点跟作用域的对应关系
     * 2、通过栈构建scope树结构
     *
     * @param scope 所在作用域
     * @param ctx AST节点
     * @return
     */
    private Scope pushScope(Scope scope, ParserRuleContext ctx) {
        at.node2Scope.put(ctx, scope);
        scope.ctx = ctx;

        scopeStack.push(scope);
        return scope;
    }

    /**
     * AST的根(程序入口)
     *
     * @param ctx
     */
    @Override
    public void enterProg(FunnyScriptParser.ProgContext ctx) {
        NameSpace nameSpace = new NameSpace("", currentScope(), ctx);
        // scope 的根
        at.nameSpace = nameSpace;
        pushScope(nameSpace, ctx);
    }

    @Override
    public void exitProg(FunnyScriptParser.ProgContext ctx) {
        popScope();
    }

    @Override
    public void enterBlock(FunnyScriptParser.BlockContext ctx) {
        // 对于函数，不需要再建一个新的scope
        if (!(ctx.parent instanceof FunnyScriptParser.FunctionBodyContext)) {
            BlockScope scope = new BlockScope(currentScope(), ctx);
            currentScope().addSymbol(scope);
            pushScope(scope, ctx);
        }
    }

    @Override
    public void exitBlock(FunnyScriptParser.BlockContext ctx) {
        if (!(ctx.parent instanceof FunnyScriptParser.FunctionBodyContext)) {
            popScope();
        }
    }

    @Override
    public void enterStatement(FunnyScriptParser.StatementContext ctx) {
        if (ctx.FOR() != null) {
            BlockScope scope = new BlockScope(currentScope(), ctx);
            currentScope().addSymbol(scope);
            pushScope(scope, ctx);
        }
    }

    @Override
    public void exitStatement(FunnyScriptParser.StatementContext ctx) {
        if (ctx.FOR() != null) {
            popScope();
        }
    }

    @Override
    public void enterFunctionDeclaration(FunnyScriptParser.FunctionDeclarationContext ctx) {
        String idName = ctx.IDENTIFIER().getText();

        // 注意：目前function的信息并不完整，参数要等到TypeResolver.java中去确定。
        Function function = new Function(idName, currentScope(), ctx);

        at.types.add(function);

        currentScope().addSymbol(function);

        pushScope(function, ctx);
    }

    @Override
    public void exitFunctionDeclaration(FunnyScriptParser.FunctionDeclarationContext ctx) {
        popScope();
    }

    @Override
    public void enterClassDeclaration(FunnyScriptParser.ClassDeclarationContext ctx) {
        String idName = ctx.IDENTIFIER().getText();

        Class theClass = new Class(idName, ctx);

        at.types.add(theClass);

        if (at.lookupClass(currentScope(), idName) != null) {
            // 只是报警，但仍然继续解析
            at.log("duplicate class name:" + idName, ctx);
        }

        currentScope().addSymbol(theClass);
        pushScope(theClass, ctx);
    }

    @Override
    public void exitClassDeclaration(FunnyScriptParser.ClassDeclarationContext ctx) {
        popScope();
    }
}
