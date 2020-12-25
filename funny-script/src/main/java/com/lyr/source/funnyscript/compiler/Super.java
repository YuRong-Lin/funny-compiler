package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @Author LinYuRong
 * @Date 2020/12/25 13:51
 * @Version 1.0
 */
public class Super extends Variable {

    protected Super(Class theClass, ParserRuleContext ctx) {
        super("super", theClass, ctx);
    }
}
