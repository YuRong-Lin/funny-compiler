package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @Author LinYuRong
 * @Date 2020/12/24 17:42
 * @Version 1.0
 */
public class Variable extends Symbol {

    protected Type type;

    protected Object defaultValue = null;

    protected Variable(String name, Scope enclosingScope, ParserRuleContext ctx) {
        this.name = name;
        this.enclosingScope = enclosingScope;
        this.ctx = ctx;
    }

    @Override
    public String toString() {
        return "Variable " + name + " -> "+ type;
    }
}
