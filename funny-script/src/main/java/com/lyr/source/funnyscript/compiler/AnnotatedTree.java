package com.lyr.source.funnyscript.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 注释树
 * 保存语义分析的结果，包括：
 * 1.类型信息，包括基本类型和用户自定义类型；
 * 2.变量和函数调用的消解；
 * 3.作用域Scope。在Scope中包含了该作用域的所有符号。Variable、Function、Class等都是符号。
 *
 * @Author LinYuRong
 * @Date 2020/12/24 14:26
 * @Version 1.0
 */
public class AnnotatedTree {

    // AST
    protected ParseTree ast;

    // 命名空间
    NameSpace nameSpace;

    // AST节点对应的scope，如for、函数调用会启动新的scope
    protected Map<ParserRuleContext, Scope> node2Scope = new HashMap<>();

    // 解析出来的所有类型，包括类和函数，以后还可以包括数组和枚举。类的方法也作为单独的要素放进去。
    protected List<Type> types = new LinkedList<>();

    // AST节点对应的Symbol
    protected Map<ParserRuleContext, Symbol> symbolOfNode = new HashMap<>();

    // 用于做类型推断，每个节点推断出来的类型
    protected Map<ParserRuleContext, Type> typeOfNode = new HashMap<>();

    //语义分析过程中生成的信息，包括普通信息、警告和错误
    protected List<CompilationLog> logs = new LinkedList<>();

    /**
     * 记录编译错误和警告
     *
     * @param message
     * @param type    信息类型，ComplilationLog中的INFO、WARNING和ERROR
     * @param ctx
     */
    protected void log(String message, int type, ParserRuleContext ctx) {
        CompilationLog log = new CompilationLog();
        log.ctx = ctx;
        log.message = message;
        log.line = ctx.getStart().getLine();
        log.positionInLine = ctx.getStart().getStartIndex();
        log.type = type;

        logs.add(log);

        System.out.println(log);
    }

    public void log(String message, ParserRuleContext ctx) {
        this.log(message, CompilationLog.ERROR, ctx);
    }

    /**
     * 通过名称查找Class。逐级Scope查找。
     *
     * @param scope
     * @param idName
     * @return
     */
    protected Class lookupClass(Scope scope, String idName) {
        Class rtn = scope.getClass(idName);

        if (rtn == null && scope.enclosingScope != null) {
            rtn = lookupClass(scope.enclosingScope, idName);
        }
        return rtn;
    }

    /**
     * 查找对应名称的Class类型
     * TODO：如何识别内部类idName？ -> 外部类.内部类?(目前不支持内部类)
     *
     * @param idName
     * @return
     */
    protected Type lookupClassType(String idName) {
        Type rtn = null;
        for (Type type : types) {
            if (type.getName().equals(idName) && type instanceof Class) {
                rtn = type;
                break;
            }
        }
        return rtn;
    }

    /**
     * 查找某节点所在的Scope
     * 算法：逐级查找父节点，找到一个对应着Scope的上级节点
     *
     * @param node
     * @return
     */
    protected Scope enclosingScopeOfNode(ParserRuleContext node) {
        Scope rtn = null;
        ParserRuleContext parent = node.getParent();
        if (parent != null) {
            rtn = node2Scope.get(parent);
            if (rtn == null) {
                rtn = enclosingScopeOfNode(parent);
            }
        }
        return rtn;
    }

    /**
     * 输出本Scope中的内容，包括每个变量的名称、类型。
     *
     * @return 树状显示的字符串
     */
    public String getScopeTreeString() {
        StringBuffer sb = new StringBuffer();
        scopeToString(sb, nameSpace, "");
        return sb.toString();
    }

    private void scopeToString(StringBuffer sb, Scope scope, String indent) {
        sb.append(indent).append(scope).append('\n');
        for (Symbol symbol : scope.symbols) {
            if (symbol instanceof Scope) {
                scopeToString(sb, (Scope) symbol, indent + "\t");
            } else {
                sb.append(indent).append("\t").append(symbol).append('\n');
            }
        }
    }

}
