package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

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
    public boolean isAncestor(Class theClass){
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
    public String toString(){
        return "Class " + name;
    }
}
