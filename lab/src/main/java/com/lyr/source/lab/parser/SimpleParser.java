package com.lyr.source.lab.parser;

import com.lyr.source.lab.lexer.SimpleLexer;
import com.lyr.source.lab.lexer.Token;
import com.lyr.source.lab.lexer.TokenReader;
import com.lyr.source.lab.lexer.TokenType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 一个简单的语法解析器。
 * 能够解析简单的表达式、变量声明和初始化语句、赋值语句。
 * 它支持的语法规则为：
 * <p>
 * programme -> intDeclare | expressionStatement | assignmentStatement
 * intDeclare -> 'int' Id ( = additive) ';'
 * expressionStatement -> additive ';'
 * assignmentStatement -> Id = additive ';'
 * additive -> multiplicative ( (+ | -) multiplicative)*
 * multiplicative -> primary ( (* | /) primary)*
 * primary -> IntLiteral | Id | (additive)
 *
 * @Author LinYuRong
 * @Date 2020/12/18 14:02
 * @Version 1.0
 */
public class SimpleParser {

    /**
     * 解析脚本，返回AST根节点
     *
     * @param script
     * @return
     */
    public ASTNode parse(String script) throws Exception {
        SimpleLexer lexer = new SimpleLexer();
        TokenReader tokens = lexer.tokenize(script);
        ASTNode rootNode = entry(tokens);
        return rootNode;
    }

    private ASTNode entry(TokenReader tokens) throws Exception {
        SimpleASTNode node = new SimpleASTNode(ASTNodeType.Programme, "Programme");

        SimpleASTNode child;
        while (tokens.peek() != null) {
            child = intDeclare(tokens);
            if (child != null) {
                node.addChild(child);
            }

            child = expressionStatement(tokens);
            if (child != null) {
                node.addChild(child);
            }

            child = assignmentStatement(tokens);
            if (child != null) {
                node.addChild(child);
            }
        }

        return node;
    }

    /**
     * int类型变量声明（初始化）
     * intDeclare -> 'int' Id ( = additive) ';'
     *
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode intDeclare(TokenReader tokens) throws Exception {
        SimpleASTNode node = null;
        Token token = tokens.peek();
        if (token != null && token.getType() == TokenType.Int) {
            tokens.read();
            token = tokens.peek();
            if (token != null && token.getType() == TokenType.Identifier) {
                token = tokens.read();
                node = new SimpleASTNode(ASTNodeType.IntDeclaration, token.getText());
                if (tokens.peek() != null && tokens.peek().getType() == TokenType.Assignment) {
                    tokens.read();
                    SimpleASTNode child = additive(tokens);
                    if (child == null) {
                        throw new Exception("invalid variable initialization, expecting an expression");
                    } else {
                        // 消费;
                        tokens.read();
                        node.addChild(child);
                    }
                }
            } else {
                throw new Exception("variable name expected");
            }
        }
        return node;
    }

    /**
     * 表达式
     * expressionStatement -> additive ';'
     *
     * @param tokens
     * @return
     */
    private SimpleASTNode expressionStatement(TokenReader tokens) throws Exception {
        int pos = tokens.getPosition();
        SimpleASTNode node = additive(tokens);
        if (node != null) {
            Token token = tokens.peek();
            if (token != null && token.getType() == TokenType.SemiColon) {
                tokens.read();
            } else {
                node = null;
                // 回溯
                tokens.setPosition(pos);
            }
        }
        return node;
    }

    /**
     * 赋值
     * assignmentStatement -> Id = additive ';'
     *
     * @param tokens
     * @return
     */
    private SimpleASTNode assignmentStatement(TokenReader tokens) throws Exception {
        SimpleASTNode node = null;
        Token token = tokens.peek();
        if (token != null && token.getType() == TokenType.Identifier) {
            token = tokens.read();
            node = new SimpleASTNode(ASTNodeType.AssignmentStmt, token.getText());
            token = tokens.peek();
            if (token != null && token.getType() == TokenType.Assignment) {
                tokens.read();
                SimpleASTNode child = additive(tokens);
                if (child == null) {
                    throw new Exception("invalid assignment statement, expecting an expression");
                } else {
                    node.addChild(child);
                    token = tokens.peek();
                    if (token != null && token.getType() == TokenType.SemiColon) {
                        tokens.read();
                    } else {
                        throw new Exception("invalid statement, expecting semicolon");
                    }
                }
            } else {
                tokens.unread();
                node = null;
            }
        }
        return node;
    }

    /**
     * 加法表达式
     * additive -> multiplicative ( (+ | -) multiplicative)*
     *
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode additive(TokenReader tokens) throws Exception {
        SimpleASTNode child1 = multiplicative(tokens);
        SimpleASTNode node = child1;
        if (child1 != null) {
            while (true) {
                Token token = tokens.peek();
                if (token != null && (token.getType() == TokenType.Plus || token.getType() == TokenType.Minus)) {
                    token = tokens.read();
                    SimpleASTNode child2 = multiplicative(tokens);
                    node = new SimpleASTNode(ASTNodeType.Additive, token.getText());
                    if (child2 != null) {
                        node.addChild(child1);
                        node.addChild(child2);
                        // 将新建节点作为新的左孩子
                        child1 = node;
                    } else {
                        throw new Exception("invalid additive expression, expecting the right part.");
                    }
                } else {
                    break;
                }
            }
        }
        return node;
    }

    /**
     * 乘法表达式
     * multiplicative -> primary ( (* | /) primary)*
     *
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode multiplicative(TokenReader tokens) throws Exception {
        SimpleASTNode child1 = primary(tokens);
        SimpleASTNode node = child1;
        if (child1 != null) {
            while (true) {
                Token token = tokens.peek();
                if (token != null && (token.getType() == TokenType.Star || token.getType() == TokenType.Slash)) {
                    token = tokens.read();
                    SimpleASTNode child2 = primary(tokens);
                    if (child2 != null) {
                        node = new SimpleASTNode(ASTNodeType.Multiplicative, token.getText());
                        node.addChild(child1);
                        node.addChild(child2);
                        child1 = node;
                    } else {
                        throw new Exception("invalid multiplicative expression, expecting the right part.");
                    }
                } else {
                    break;
                }
            }
        }
        return node;
    }

    /**
     * 基础表达式
     * primary -> IntLiteral | Id | (additive)
     *
     * @param tokens
     * @return
     */
    private SimpleASTNode primary(TokenReader tokens) throws Exception {
        SimpleASTNode node = null;
        Token token = tokens.peek();
        if (token != null) {
            if (token.getType() == TokenType.IntLiteral) {
                token = tokens.read();
                node = new SimpleASTNode(ASTNodeType.IntLiteral, token.getText());
            } else if (token.getType() == TokenType.Identifier) {
                token = tokens.read();
                node = new SimpleASTNode(ASTNodeType.Identifier, token.getText());
            } else if (token.getType() == TokenType.LeftParen) {
                // 消费(
                tokens.read();
                node = additive(tokens);
                if (node != null) {
                    token = tokens.peek();
                    if (token != null && token.getType() == TokenType.RightParen) {
                        // 消费)
                        tokens.read();
                    } else {
                        throw new Exception("expecting right parenthesis");
                    }
                } else {
                    throw new Exception("expecting an additive expression inside parenthesis");
                }
            }
        }
        return node;
    }

    private final class SimpleASTNode implements ASTNode {
        private SimpleASTNode parent;
        private List<ASTNode> children = new ArrayList<>();
        private List<ASTNode> readonlyChildren = Collections.unmodifiableList(children);
        private ASTNodeType type;
        private String text;

        public SimpleASTNode(ASTNodeType type, String text) {
            this.type = type;
            this.text = text;
        }

        @Override
        public ASTNode getParent() {
            return parent;
        }

        @Override
        public List<ASTNode> getChildren() {
            return readonlyChildren;
        }

        @Override
        public ASTNodeType getType() {
            return type;
        }

        @Override
        public String getText() {
            return text;
        }

        public void addChild(SimpleASTNode child) {
            children.add(child);
            child.parent = this;
        }
    }

    /**
     * 打印输出AST的树状结构
     *
     * @param node
     * @param indent 缩进字符，由tab组成，每一级多一个tab
     */
    public void dumpAST(ASTNode node, String indent) {
        System.out.println(indent + node.getType() + " " + node.getText());
        for (ASTNode child : node.getChildren()) {
            dumpAST(child, indent + "\t");
        }
    }
}
