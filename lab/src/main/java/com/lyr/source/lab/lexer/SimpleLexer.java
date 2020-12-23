package com.lyr.source.lab.lexer;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 词法解析器
 *
 * @Author LinYuRong
 * @Date 2020/12/17 11:26
 * @Version 1.0
 */
public class SimpleLexer {

    //是否是字母
    private boolean isAlpha(int ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    //是否是数字
    private boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }

    //是否是空白字符
    private boolean isBlank(int ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }

    /**
     * 解析入口
     *
     * @param code
     * @return token流
     */
    public SimpleTokenReader tokenize(String code) {
        // 1. 转成字符流
        CharArrayReader charReader = new CharArrayReader(code.toCharArray());
        int ich = 0;
        char ch = 0;

        // 2. 遍历字符流，状态转移
        List<Token> tokens = new LinkedList<>();
        StringBuffer tokenText = new StringBuffer();
        SimpleToken token = new SimpleToken();
        TokenizeHelper helper = new TokenizeHelper(token, tokenText, tokens);
        DfaState state = DfaState.Initial;

        try {
            while ((ich = charReader.read()) != -1) {
                ch = (char) ich;
                switch (state) {
                    case Initial:
                    case GE:
                    case Assignment:
                    case Plus:
                    case Minus:
                    case Star:
                    case Slash:
                    case SemiColon:
                    case LeftParen:
                    case RightParen:
                        state = initToken(ch, helper);
                        break;
                    case Id:
                        if (isAlpha(ch) || isDigit(ch)) {
                            helper.append(ch);
                        } else {
                            state = initToken(ch, helper);
                        }
                        break;
                    case GT:
                        if (ch == '=') {
                            helper.setTokenType(TokenType.GE);
                            state = DfaState.GE;
                            helper.append(ch);
                        } else {
                            state = initToken(ch, helper);
                        }
                        break;
                    case Id_int1:
                        if (ch == 'n') {
                            state = DfaState.Id_int2;
                            helper.append(ch);
                        } else if (isAlpha(ch) || isDigit(ch)) {
                            state = DfaState.Id;
                            helper.append(ch);
                        } else {
                            state = initToken(ch, helper);
                        }
                        break;
                    case Id_int2:
                        if (ch == 't') {
                            state = DfaState.Id_int3;
                            helper.append(ch);
                        } else if (isAlpha(ch) || isDigit(ch)) {
                            state = DfaState.Id;
                            helper.append(ch);
                        } else {
                            state = initToken(ch, helper);
                        }
                        break;
                    case Id_int3:
                        if (isBlank(ch)) {
                            helper.setTokenType(TokenType.Int);
                            state = initToken(ch, helper);
                        } else {
                            state = DfaState.Id;
                            helper.append(ch);
                        }
                        break;
                    case IntLiteral:
                        if (isDigit(ch)) {
                            helper.append(ch);
                        } else {
                            state = initToken(ch, helper);
                        }
                        break;
                    default:

                }
            }
            // last token
            if (helper.getTokenText().length() > 0) {
                initToken(ch, helper);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SimpleTokenReader(helper.getTokens());
    }

    private DfaState initToken(char ch, TokenizeHelper helper) {
        if (helper.getTokenText().length() > 0) {
            helper.saveAndResetState();
        }

        DfaState newState;
        if (isAlpha(ch)) {
            if (ch == 'i') {
                newState = DfaState.Id_int1;
            } else {
                newState = DfaState.Id;
            }
            helper.setTokenType(TokenType.Identifier);
            helper.append(ch);
        } else if (isDigit(ch)) {
            newState = DfaState.IntLiteral;
            helper.setTokenType(TokenType.IntLiteral);
            helper.append(ch);
        } else if (ch == '>') {
            newState = DfaState.GT;
            helper.setTokenType(TokenType.GT);
            helper.append(ch);
        } else if (ch == '=') {
            newState = DfaState.Assignment;
            helper.setTokenType(TokenType.Assignment);
            helper.append(ch);
        } else if (ch == '+') {
            newState = DfaState.Plus;
            helper.setTokenType(TokenType.Plus);
            helper.append(ch);
        } else if (ch == '-') {
            newState = DfaState.Minus;
            helper.setTokenType(TokenType.Minus);
            helper.append(ch);
        } else if (ch == '*') {
            newState = DfaState.Star;
            helper.setTokenType(TokenType.Star);
            helper.append(ch);
        } else if (ch == '/') {
            newState = DfaState.Slash;
            helper.setTokenType(TokenType.Slash);
            helper.append(ch);
        } else if (ch == ';') {
            newState = DfaState.SemiColon;
            helper.setTokenType(TokenType.SemiColon);
            helper.append(ch);
        } else if (ch == '(') {
            newState = DfaState.LeftParen;
            helper.setTokenType(TokenType.LeftParen);
            helper.append(ch);
        } else if (ch == ')') {
            newState = DfaState.RightParen;
            helper.setTokenType(TokenType.RightParen);
            helper.append(ch);
        } else {
            // 跳过其他模式
            newState = DfaState.Initial;
        }

        return newState;
    }

    /**
     * 打印所有的Token
     *
     * @param tokenReader
     */
    public static void dump(SimpleTokenReader tokenReader) {
        System.out.println("text\ttype");
        Token token = null;
        while ((token = tokenReader.read()) != null) {
            System.out.println(token.getText() + "\t\t" + token.getType());
        }
    }

    /**
     * 解析辅助类(保存解析过程中的状态)
     */
    private final class TokenizeHelper {
        /**
         * 正在解析的文本值
         */
        private StringBuffer tokenText;

        /**
         * 正在解析的token
         */
        private SimpleToken token;

        /**
         * 解析到的token列表
         */
        private List<Token> tokens;

        public TokenizeHelper(SimpleToken token, StringBuffer tokenText, List<Token> tokens) {
            this.token = token;
            this.tokenText = tokenText;
            this.tokens = tokens;
        }

        public StringBuffer getTokenText() {
            return tokenText;
        }

        public List<Token> getTokens() {
            return tokens;
        }

        public void saveAndResetState() {
            token.text = tokenText.toString();
            this.tokens.add(token);

            token = new SimpleToken();
            tokenText = new StringBuffer();
        }

        public void append(char ch) {
            tokenText.append(ch);
        }

        public void setTokenType(TokenType type) {
            token.type = type;
        }
    }

    private final class SimpleToken implements Token {
        private TokenType type;
        private String text;

        public TokenType getType() {
            return type;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * 有限状态机枚举
     */
    private enum DfaState {
        Initial,

        If, Id_if1, Id_if2, Else, Id_else1, Id_else2, Id_else3, Id_else4, Int, Id_int1, Id_int2, Id_int3, Id, GT, GE,

        Assignment,

        Plus, Minus, Star, Slash,

        SemiColon,
        LeftParen,
        RightParen,

        IntLiteral
    }

}
