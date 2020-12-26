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
     *
     * @param symbol
     */
    protected void addSymbol(Symbol symbol) {
        if (symbol != null) {
            symbols.add(symbol);
            symbol.enclosingScope = this;
        }
    }

    /**
     * 是否包含某个Class
     *
     * @param name
     * @return
     */
    protected Class getClass(String name) {
        return getClass(this, name);
    }

    protected static Class getClass(Scope scope, String name) {
        for (Symbol s : scope.symbols) {
            if (s instanceof Class && s.name.equals(name)) {
                return (Class) s;
            }
        }
        return null;
    }

    /**
     * 是否包含某个Variable
     *
     * @param name
     * @return
     */
    protected Variable getVariable(String name) {
        return getVariable(this, name);
    }

    protected static Variable getVariable(Scope scope, String name) {
        for (Symbol s : scope.symbols) {
            if (s instanceof Variable && s.name.equals(name)) {
                return (Variable) s;
            }
        }
        return null;
    }

    /**
     * 是否包含某个Function
     *
     * @param name
     * @param paramTypes 参数类型。不允许为空。该参数不允许为空。如果没有参数，需要传入一个0长度的列表。
     * @return
     */
    protected Function getFunction(String name, List<Type> paramTypes) {
        return getFunction(this, name, paramTypes);
    }

    /**
     * 是否包含某个Function。这是个静态方法，可以做为工具方法重用。避免类里要超找父类的情况。
     *
     * @param scope
     * @param name
     * @param paramTypes
     * @return
     */
    protected static Function getFunction(Scope scope, String name, List<Type> paramTypes) {
        Function rtn = null;
        for (Symbol s : scope.symbols) {
            if (s instanceof Function && s.name.equals(name)) {
                Function function = (Function) s;
                if (function.matchParameterTypes(paramTypes)) {
                    rtn = function;
                    break;
                }
            }
        }

        return rtn;
    }
}
