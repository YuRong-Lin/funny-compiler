package com.lyr.source.funnyscript.compiler;

/**
 * 对栈中的值的引用
 */
public interface LValue {

    Object getValue();

    void setValue(Object value);

    Variable getVariable();

    FunnyObject getValueContainer();
}