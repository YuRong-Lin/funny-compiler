package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.LinkedList;
import java.util.List;

/**
 * 命令空间
 *
 * @Author LinYuRong
 * @Date 2020/12/24 16:35
 * @Version 1.0
 */
public class NameSpace extends BlockScope {
    private String name;
    private NameSpace parent;
    private List<NameSpace> subNameSpaces = new LinkedList<>();

    protected NameSpace(String name, Scope enclosingScope, ParserRuleContext ctx) {
        super(enclosingScope, ctx);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
