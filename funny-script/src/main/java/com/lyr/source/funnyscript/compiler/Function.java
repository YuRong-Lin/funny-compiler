package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author LinYuRong
 * @Date 2020/12/24 17:30
 * @Version 1.0
 */
public class Function extends Scope implements FunctionType {

    protected Type returnType;
    protected List<Variable> params = new LinkedList<>();

    protected Function(String name, Scope enclosingScope, ParserRuleContext ctx) {
        this.name = name;
        this.enclosingScope = enclosingScope;
        this.ctx = ctx;
    }

    @Override
    public Type getReturnType() {
        return returnType;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public List<Type> getParamTypes() {
        return null;
    }

    /**
     * TODO
     *
     * @param paramTypes
     * @return
     */
    @Override
    public boolean matchParameterTypes(List<Type> paramTypes) {
        return false;
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
        return "Function " + name;
    }
}
