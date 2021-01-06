package com.lyr.source.frontendalgorithm;

/**
 * @Author LinYuRong
 * @Date 2021/1/6 10:51
 * @Version 1.0
 */
public class SampleLexerGrammar {

    /**
     * 创建一个示例用词法规则，支持：
     * 关键字：int, if
     * 操作符：+ - * / = == >= <= > <
     * 标识符
     *
     * @return
     */
    public static GrammarNode commonLexerGrammar() {
        GrammarNode rootNode = new GrammarNode(GrammarNodeType.Or);

        //int关键字
        GrammarNode intNode = rootNode.createChild("INT", GrammarNodeType.And);
        intNode.createChild(new CharSet('i'));
        intNode.createChild(new CharSet('n'));
        intNode.createChild(new CharSet('t'));

        //if关键字
        GrammarNode ifNode = rootNode.createChild("IF", GrammarNodeType.And);
        ifNode.createChild(new CharSet('i'));
        ifNode.createChild(new CharSet('f'));

        //else关键字
        GrammarNode elseNode = rootNode.createChild("ELSE", GrammarNodeType.And);
        elseNode.createChild(new CharSet('e'));
        elseNode.createChild(new CharSet('l'));
        elseNode.createChild(new CharSet('s'));
        elseNode.createChild(new CharSet('e'));

        //+
        GrammarNode add = rootNode.createChild("ADD", new CharSet('+'));

        //-
        GrammarNode minus = rootNode.createChild("SUB", new CharSet('-'));

        //*
        GrammarNode star = rootNode.createChild("MUL", new CharSet('*'));

        // /
        GrammarNode slash = rootNode.createChild("DIV", new CharSet('/'));

        // (
        GrammarNode leftParen = rootNode.createChild("LPAREN", new CharSet('('));

        // )
        GrammarNode rightParen = rootNode.createChild("RPAREN", new CharSet(')'));

        // (
        GrammarNode leftBrace = rootNode.createChild("LBRACE", new CharSet('{'));

        // )
        GrammarNode rightBrace = rootNode.createChild("RBRACE", new CharSet('}'));

        // ;
        GrammarNode semi = rootNode.createChild("SEMI", new CharSet(';'));

        // ;
        GrammarNode question = rootNode.createChild("QUESTION", new CharSet('?'));

        // :
        GrammarNode colon = rootNode.createChild("COLON", new CharSet(':'));

        // =
        GrammarNode assign = rootNode.createChild("ASSIGN", new CharSet('='));

        // ==
        GrammarNode equal = rootNode.createChild("EQUAL", GrammarNodeType.And);
        equal.createChild(new CharSet('='));
        equal.createChild(new CharSet('='));

        // ==
        GrammarNode notEqual = rootNode.createChild("NOTEQUAL", GrammarNodeType.And);
        notEqual.createChild(new CharSet('!'));
        notEqual.createChild(new CharSet('='));

        // >
        GrammarNode gt = rootNode.createChild("GT", new CharSet('>'));

        // >=
        GrammarNode ge = rootNode.createChild("GE", GrammarNodeType.And);
        ge.createChild(new CharSet('>'));
        ge.createChild(new CharSet('='));

        // <
        GrammarNode lt = rootNode.createChild("LT", new CharSet('<'));

        // <=
        GrammarNode le = rootNode.createChild("LE", GrammarNodeType.And);
        le.createChild(new CharSet('<'));
        le.createChild(new CharSet('='));

        //标识符
        GrammarNode id = rootNode.createChild("ID", GrammarNodeType.And);
        GrammarNode firstLetter = id.createChild(CharSet.letter);
        GrammarNode letterOrDigit = id.createChild(CharSet.letterOrDigit);
        letterOrDigit.setRepeatTimes(0, -1);

        //数字字面量
        GrammarNode intLiteral = rootNode.createChild("INT_LITERAL", CharSet.digit);
        intLiteral.setRepeatTimes(1, -1);

        //空白字符
        //不设置tokenType，不会生成Token，会忽略掉
        GrammarNode whiteSpace = rootNode.createChild("WHITE_SPACE", CharSet.whiteSpace);
        whiteSpace.setRepeatTimes(0, -1);
        whiteSpace.setNeglect(true);

        return rootNode;
    }
}
