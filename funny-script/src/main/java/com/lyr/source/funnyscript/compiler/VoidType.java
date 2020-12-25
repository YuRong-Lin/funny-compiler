package com.lyr.source.funnyscript.compiler;

/**
 * @Author LinYuRong
 * @Date 2020/12/25 16:29
 * @Version 1.0
 */
public final class VoidType implements Type {

    private VoidType(){}

    //只保留一个实例即可。
    private static VoidType voidType = new VoidType();

    public static VoidType instance(){
        return voidType;
    }

    @Override
    public String getName() {
        return "void";
    }

    @Override
    public Scope getEnclosingScope() {
        return null;
    }

    @Override
    public boolean isType(Type type) {
        return type == this;
    }

    @Override
    public String toString(){
        return "void";
    }
}
