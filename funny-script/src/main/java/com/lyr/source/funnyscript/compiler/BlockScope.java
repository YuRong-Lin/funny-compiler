package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * 块作用域
 *
 * @Author LinYuRong
 * @Date 2020/12/24 16:36
 * @Version 1.0
 */
public class BlockScope extends Scope {
    // 用于标识block编号
    private static int index = 1;
    private static String PREFIX = "block";

    protected BlockScope() {
        this.name = PREFIX + index++;
    }

    protected BlockScope(Scope enclosingScope, ParserRuleContext ctx) {
        this();
        this.enclosingScope = enclosingScope;
        this.ctx = ctx;
    }

    @Override
    public String toString() {
        return "Block " + name;
    }
}
