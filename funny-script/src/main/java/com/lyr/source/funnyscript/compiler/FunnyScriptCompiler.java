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

        // pass2: 把变量、类继承、函数声明的类型都解析出来。也就是所有声明时用到类型的地方。
        TypeResolver pass2 = new TypeResolver(at);
        walker.walk(pass2, at.ast);

        // pass3: pass3：消解有的变量应用、函数引用。另外还做了类型的推断。
        RefResolver pass3 = new RefResolver(at);
        walker.walk(pass3, at.ast);

        // pass4: 类型检查
        TypeChecker pass4 = new TypeChecker(at);
        walker.walk(pass4, at.ast);

        // pass5: 其他语义检查
        SemanticValidator pass5 = new SemanticValidator(at);
        walker.walk(pass5, at.ast);

        // pass6: 闭包分析
        ClosureAnalyzer closureAnalyzer = new ClosureAnalyzer(at);
        closureAnalyzer.analyzeClosures();

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

    /**
     * 输出编译信息
     */
    public void dumpCompilationLogs() {
        if (at != null) {
            for (CompilationLog log : at.logs) {
                System.out.println(log);
            }
        }
    }

    public static void main(String[] args) {
        String script =
                "class Mammal{\n" +
                        "  //类属性\n" +
                        "  string name = \"\";\n" +
                        "\n" +
                        "  //构造方法\n" +
                        "  Mammal(string str){\n" +
                        "    name = str;\n" +
                        "  }\n" +
                        "\n" +
                        "  //方法\n" +
                        "  void speak(){\n" +
                        "    int a = 1;" +
                        "    println(\"mammal \" + name +\" speaking...\");\n" +
                        "  }\n" +
                        "}\n" +
                        "\n" +
                        "Mammal mammal = Mammal(\"dog\");           //playscript特别的构造方法，不需要用new关键字。\n" +
                        "mammal.speak();                          //访问对象方法\n" +
                        "println(\"mammal.name = \" + mammal.name); //访问对象的属性";

        FunnyScriptCompiler compiler = new FunnyScriptCompiler();
        compiler.compile(script, true, true);
    }
}
