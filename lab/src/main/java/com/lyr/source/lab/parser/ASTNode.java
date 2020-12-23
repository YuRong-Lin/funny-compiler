package com.lyr.source.lab.parser;

import java.util.List;

/**
 * AST的节点。
 * 属性包括AST的类型、文本值、下级子节点和父节点
 */
public interface ASTNode{
    //父节点
    ASTNode getParent();

    //子节点
    List<ASTNode> getChildren();

    //AST类型
    ASTNodeType getType();

    //文本值
    String getText();
}