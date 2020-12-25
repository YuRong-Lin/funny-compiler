package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @Author LinYuRong
 * @Date 2020/12/25 13:49
 * @Version 1.0
 */
public class This extends Variable {

    protected This(Class theClass, ParserRuleContext ctx) {
        super("this", theClass, ctx);
    }
}
