package com.lyr.source.lab.lexer;

/**
 * @Author LinYuRong
 * @Date 2020/12/17 15:46
 * @Version 1.0
 */
public class LexerTest {

    public static void main(String[] args) {
        SimpleLexer lexer = new SimpleLexer();

        String script = "int age = 45;";
        System.out.println("parse :" + script);
        SimpleTokenReader tokenReader = lexer.tokenize(script);
        lexer.dump(tokenReader);

        //测试inta的解析
        script = "inta age = 45;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        lexer.dump(tokenReader);

        //测试in的解析
        script = "in age = 45;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        lexer.dump(tokenReader);

        //测试>=的解析
        script = "age >= 45;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        lexer.dump(tokenReader);

        //测试>的解析
        script = "age > 45;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        lexer.dump(tokenReader);
    }
}
