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
                        "    println(\"mammal \" + name +\" speaking...\");\n" +
                        "  }\n" +
                        "}\n" +
                        "\n" +
                        "Mammal mammal = Mammal(\"dog\");           //playscript特别的构造方法，不需要用new关键字。\n" +
                        "mammal.speak();                          //访问对象方法\n" +
                        "println(\"mammal.name = \" + mammal.name); //访问对象的属性";

        FunnyScriptCompiler compiler = new FunnyScriptCompiler();
        compiler.compile(script, true, true);

        /**
         * AST.eg:
         * 1、
         * (prog
         * 	(blockStatements
         * 		(blockStatement
         * 			(variableDeclarators (typeType (primitiveType int))
         * 				(variableDeclarator (variableDeclaratorId i) = (variableInitializer (expression (primary (literal (integerLiteral 0))))))
         * 			) ;
         * 		)
         * 		(blockStatement
         * 			(statement (expression (functionCall println ( (expressionList (expression (primary i))) ))) ;)
         * 		)
         * 		(blockStatement
         * 			(statement
         * 				(block {
         * 					(blockStatements
         * 						(blockStatement (statement (expression (expression (primary i)) = (expression (primary (literal (integerLiteral 2))))) ;))
         * 						(blockStatement (statement (expression (functionCall println ( (expressionList (expression (primary i))) ))) ;))
         * 						(blockStatement (variableDeclarators (typeType (primitiveType int)) (variableDeclarator (variableDeclaratorId i) = (variableInitializer (expression (primary (literal (integerLiteral 3))))))) ;) (blockStatement (statement (expression (functionCall println ( (expressionList (expression (primary i))) ))) ;))
         * 					)
         *              })
         * 			)
         * 		)
         * 		(blockStatement (statement (expression (functionCall println ( (expressionList (expression (primary i))) ))) ;)))
         * )
         *
         * 2、
         * (prog
         * 	(blockStatements
         * 		(blockStatement
         * 			(classDeclaration class Mammal
         * 				(classBody {
         * 					(classBodyDeclaration
         * 						(memberDeclaration
         * 							(fieldDeclaration
         * 								(variableDeclarators (typeType (primitiveType string)) (variableDeclarator (variableDeclaratorId name) = (variableInitializer (expression (primary (literal "")))))) ;
         * 							)
         * 						)
         * 					)
         * 					(classBodyDeclaration
         * 						(memberDeclaration
         * 							(functionDeclaration Mammal
         * 								(formalParameters
         * 									(
         * 										(formalParameterList
         * 											(formalParameter (typeType (primitiveType string)) (variableDeclaratorId str)))
         * 									)
         * 								)
         * 								(functionBody
         * 									(block {
         * 										(blockStatements
         * 											(blockStatement (statement (expression (expression (primary name)) = (expression (primary str))) ;)))
         *                                                                        })
         * 								)
         * 							)
         * 						)
         * 					)
         * 					(classBodyDeclaration
         * 						(memberDeclaration
         * 							(functionDeclaration
         * 								(typeTypeOrVoid void) speak (formalParameters ( ))
         * 								(functionBody
         * 									(block {
         * 										(blockStatements
         * 											(blockStatement (statement (expression (functionCall println ( (expressionList (expression (expression (expression (primary (literal "mammal "))) + (expression (primary name))) + (expression (primary (literal " speaking..."))))) ))) ;))
         * 										)
         *                                    })
         * 								)
         * 							)
         * 						)
         * 					)
         * 				})
         * 			)
         * 		)
         * 		(blockStatement
         * 			(variableDeclarators
         * 				(typeType (classOrInterfaceType Mammal)) (variableDeclarator (variableDeclaratorId mammal) = (variableInitializer (expression (functionCall Mammal ( (expressionList (expression (primary (literal "dog")))) )))))) ;)
         * 		(blockStatement
         * 			(statement (expression (expression (primary mammal)) . (functionCall speak ( ))) ;))
         * 		(blockStatement
         * 			(statement (expression (functionCall println ( (expressionList (expression (expression (primary (literal "mammal.name = "))) + (expression (expression (primary mammal)) . name))) ))) ;))
         * 	)
         * )
         */
    }
}
