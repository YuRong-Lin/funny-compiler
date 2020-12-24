package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @Author LinYuRong
 * @Date 2020/12/24 18:02
 * @Version 1.0
 */
public class Class extends Scope implements Type {

    protected Class(String name, ParserRuleContext ctx) {
        this.name = name;
        this.ctx = ctx;
    }

    /**
     * TODO
     *
     * @param type 目标类型
     * @return
     */
    @Override
    public boolean isType(Type type) {
        return false;
    }

    @Override
    public String toString(){
        return "Class " + name;
    }
}
