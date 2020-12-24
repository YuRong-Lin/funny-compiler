package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.compiler.Scope;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * 符号
 *
 * @Author LinYuRong
 * @Date 2020/12/23 17:36
 * @Version 1.0
 */
public abstract class Symbol {
    //符号的名称
    protected String name = null;

    //所属作用域
    protected Scope enclosingScope = null;

    //可见性，比如public还是private
    protected int visibility = 0;

    //Symbol关联的AST节点
    protected ParserRuleContext ctx = null;

    public String getName(){
        return name;
    }

    public Scope getEnclosingScope(){
        return enclosingScope;
    }
}
