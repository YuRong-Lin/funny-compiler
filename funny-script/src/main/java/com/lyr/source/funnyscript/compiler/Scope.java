package com.lyr.source.funnyscript.compiler;

import java.util.LinkedList;
import java.util.List;

/**
 * 作用域
 *
 * @Author LinYuRong
 * @Date 2020/12/23 17:37
 * @Version 1.0
 */
public abstract class Scope extends Symbol {
    // 该Scope中的成员，包括变量、方法、类等。
    protected List<Symbol> symbols = new LinkedList<>();

    /**
     * 向scope添加符号，并设置好enclosingScope
     * @param symbol
     */
    protected void addSymbol(Symbol symbol) {
        symbols.add(symbol);
        symbol.enclosingScope = this;
    }

    /**
     * 是否包含某个Class
     * @param name
     * @return
     */
    protected Class getClass(String name){
        return getClass(this,name);
    }

    protected static Class getClass(Scope scope, String name){
        for (Symbol s : scope.symbols) {
            if (s instanceof Class && s.name.equals(name)){
                return (Class) s;
            }
        }
        return null;
    }
}
