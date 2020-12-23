package com.lyr.source.lab.lexer;

import java.util.List;

/**
 * @Author LinYuRong
 * @Date 2020/12/17 11:47
 * @Version 1.0
 */
public class SimpleTokenReader implements TokenReader {
    private List<Token> tokens;
    private int pos;

    public SimpleTokenReader(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token read() {
        if (pos < tokens.size()) {
            return tokens.get(pos++);
        }
        return null;
    }

    public Token peek() {
        if (pos < tokens.size()) {
            return tokens.get(pos);
        }
        return null;
    }

    public void unread() {
        if (pos > 0) {
            pos--;
        }
    }

    public int getPosition() {
        return pos;
    }

    public void setPosition(int position) {
        if (position >= 0 && position < tokens.size()) {
            this.pos = position;
        }
    }
}
