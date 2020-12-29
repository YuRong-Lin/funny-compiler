package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @Author LinYuRong
 * @Date 2020/12/24 17:30
 * @Version 1.0
 */
public class Function extends Scope implements FunctionType {

    protected Type returnType;
    protected List<Variable> parameters = new LinkedList<>();
    private List<Type> paramTypes;
    // 闭包变量，即它所引用的外部环境变量
    protected Set<Variable> closureVariables;

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
     * 参数类型列表
     *
     * @return
     */
    @Override
    public List<Type> getParamTypes() {
        if (paramTypes == null) {
            paramTypes = new LinkedList<>();
        }

        for (Variable param : parameters) {
            paramTypes.add(param.type);
        }

        return paramTypes;
    }

    /**
     * 检查改函数是否匹配所需的参数。
     *
     * @param paramTypes
     * @return
     */
    @Override
    public boolean matchParameterTypes(List<Type> paramTypes) {
        // 比较每个参数
        if (parameters.size() != paramTypes.size()) {
            return false;
        }

        boolean match = true;
        for (int i = 0; i < paramTypes.size(); i++) {
            Variable var = parameters.get(i);
            Type type = paramTypes.get(i);
            if (!var.type.isType(type)) {
                match = false;
                break;
            }
        }

        return match;
    }

    /**
     * @param type 目标类型
     * @return
     */
    @Override
    public boolean isType(Type type) {
        if (type instanceof FunctionType) {
            return DefaultFunctionType.isType(this, (FunctionType) type);
        }
        return false;
    }

    /**
     * 该函数是不是类的方法
     *
     * @return
     */
    public boolean isMethod() {
        return enclosingScope instanceof Class;
    }

    /**
     * 该函数是不是类的构建函数
     *
     * @return
     */
    public boolean isConstructor() {
        if (enclosingScope instanceof Class) {
            return enclosingScope.name.equals(name);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Function " + name;
    }
}
