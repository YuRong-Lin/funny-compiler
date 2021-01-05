package com.lyr.source.frontendalgorithm;

/**
 * 语法节点的类型。
 *
 * @Author LinYuRong
 * @Date 2021/1/5 15:25
 * @Version 1.0
 */
public enum GrammarNodeType {
    And,             // 并运算
    Or,              // 或运算
    Char,            // 字符，用于表达词法规则
    Token,           // 一个Token，用于表达语法规则
    Epsilon          // 空集
}
