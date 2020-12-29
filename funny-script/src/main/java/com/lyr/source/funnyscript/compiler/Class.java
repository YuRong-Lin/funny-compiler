package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * @Author LinYuRong
 * @Date 2020/12/24 18:02
 * @Version 1.0
 */
public class Class extends Scope implements Type {

    private Class parentClass;

    // this变量
    private This thisRef;

    private Super superRef;

    // 默认空构造函数
    private DefaultConstructor defaultConstructor;

    //最顶层的基类
    private static Class rootClass = new Class("Object", null);

    protected Class(String name, ParserRuleContext ctx) {
        this.name = name;
        this.ctx = ctx;

        thisRef = new This(this, ctx);
        thisRef.type = this;
    }

    protected Class getParentClass() {
        return parentClass;
    }

    protected void setParentClass(Class parentClass) {
        this.parentClass = parentClass;

        // TODO: 这里ctx如何理解？
        // 其实superRef引用的也是自己
        superRef = new Super(parentClass, ctx);
        superRef.type = parentClass;
    }

    protected This getThis() {
        return thisRef;
    }

    protected Super getSuper() {
        return superRef;
    }

    /**
     * 找到某个构建函数。不需要往父类去找，在本级找就行了。
     *
     * @param paramTypes
     * @return
     */
    protected Function findConstructor(List<Type> paramTypes) {
        //TODO 是否要检查visibility?
        Function rtn = super.getFunction(name, paramTypes);
        return rtn;
    }

    /**
     * 在自身及父类中找到某个方法
     *
     * @param name
     * @param paramTypes 参数类型列表。该参数不允许为空。如果没有参数，需要传入一个0长度的列表。
     * @return
     */
    protected Function getFunction(String name, List<Type> paramTypes) {
        // 在本级查找这个这个方法
        // TODO 是否要检查visibility?
        Function rtn = super.getFunction(name, paramTypes);

        //如果在本级找不到，那么递归的从父类中查找
        if (rtn == null && parentClass != null) {
            rtn = parentClass.getFunction(name, paramTypes);
        }

        return rtn;
    }

    protected Variable getFunctionVariable(String name, List<Type> paramTypes) {
        // TODO 是否要检查visibility?
        Variable rtn = super.getFunctionVariable(name, paramTypes);

        if (rtn == null && parentClass != null) {
            rtn = parentClass.getFunctionVariable(name, paramTypes);
        }

        return rtn;
    }

    protected DefaultConstructor defaultConstructor() {
        if (defaultConstructor == null) {
            defaultConstructor = new DefaultConstructor(this.name, this);
        }
        return defaultConstructor;
    }

    /**
     * 当自身是目标类型的子类型的时候，返回true;
     *
     * @param type 目标类型
     * @return
     */
    @Override
    public boolean isType(Type type) {
        if (this == type) {
            return true;
        }

        if (type instanceof Class) {
            return ((Class) type).isAncestor(this);
        }

        return false;
    }

    /**
     * 本类型是不是另一个类型的祖先类型
     *
     * @param theClass
     * @return
     */
    public boolean isAncestor(Class theClass) {
        if (theClass.getParentClass() != null) {
            if (theClass.getParentClass() == this) {
                return true;
            } else {
                return isAncestor(theClass.getParentClass());
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Class " + name;
    }
}
