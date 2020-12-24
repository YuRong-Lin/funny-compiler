package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.parser.FunnyScriptLexer;
import com.lyr.source.funnyscript.parser.FunnyScriptParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Funny Script解释器
 *
 * @Author LinYuRong
 * @Date 2020/12/24 14:19
 * @Version 1.0
 */
public class FunnyScriptCompiler {

    private AnnotatedTree at;
    private FunnyScriptLexer lexer;
    private FunnyScriptParser parser;

    /**
     * 编译
     *
     * @param script   脚本
     * @param verbose  输出详细信息
     * @param ast_dump 打印ast
     * @return
     */
    public AnnotatedTree compile(String script, boolean verbose, boolean ast_dump) {
        at = new AnnotatedTree();

        // 词法分析
        lexer = new FunnyScriptLexer(CharStreams.fromString(script));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // 语法分析
        parser = new FunnyScriptParser(tokens);
        at.ast = parser.prog();

        // 语义分析
        ParseTreeWalker walker = new ParseTreeWalker();

        // pass1：识别所有类型（包括类和函数）、score
        TypeAndScopeScanner pass1 = new TypeAndScopeScanner(at);
        walker.walk(pass1, at.ast);

        //打印AST
        if (verbose || ast_dump) {
            dumpAST();
        }

        //打印符号表
        if (verbose) {
            dumpSymbols();
        }

        return at;
    }

    /**
     * 打印AST，以lisp格式
     */
    public void dumpAST() {
        if (at != null) {
            System.out.println(at.ast.toStringTree(parser));
        }
    }

    /**
     * 打印符号表
     */
    public void dumpSymbols() {
        if (at != null) {
            System.out.println(at.getScopeTreeString());
        }
    }

    public static void main(String[] args) {
        String script = "class A { int foo() {fun(); return 1;} void fun() {}} A a = A();a.foo();";

        FunnyScriptCompiler compiler = new FunnyScriptCompiler();
        compiler.compile(script, true, true);
    }
}
