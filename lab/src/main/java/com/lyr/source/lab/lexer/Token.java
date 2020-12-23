package com.lyr.source.lab.lexer;

/**
 * @Author LinYuRong
 * @Date 2020/12/17 14:00
 * @Version 1.0
 */
public interface Token {

    /**
     *Token的类型
     * @return
     */
    TokenType getType();

    /**
     * Token的文本值
     * @return
     */
    String getText();
}
