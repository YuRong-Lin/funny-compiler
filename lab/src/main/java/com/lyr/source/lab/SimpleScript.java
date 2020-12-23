package com.lyr.source.lab;


import com.lyr.source.lab.parser.ASTNode;
import com.lyr.source.lab.parser.ASTNodeType;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author LinYuRong
 * @Date 2020/12/18 17:32
 * @Version 1.0
 */
public class SimpleScript {
    private Map<String, Integer> variables = new HashMap<>();

    public Integer evaluate(ASTNode node, String indent, boolean verbose) throws Exception {
        Integer result = null;
        if (verbose) {
            System.out.println(indent + "Calculating: " + node.getType());
        }

        switch (node.getType()) {
            case Programme:
                for (ASTNode child : node.getChildren()) {
                    result = evaluate(child, indent, verbose);
                }
                break;
            case Additive:
                ASTNode child1 = node.getChildren().get(0);
                Integer value1 = evaluate(child1, indent + "\t", verbose);

                ASTNode child2 = node.getChildren().get(1);
                Integer value2 = evaluate(child2, indent + "\t", verbose);
                if (node.getText().equals("+")) {
                    result = value1 + value2;
                } else {
                    result = value1 - value2;
                }
                break;
            case Multiplicative:
                child1 = node.getChildren().get(0);
                value1 = evaluate(child1, indent + "\t", verbose);

                child2 = node.getChildren().get(1);
                value2 = evaluate(child2, indent + "\t", verbose);
                if (node.getText().equals("*")) {
                    result = value1 * value2;
                } else {
                    result = value1 / value2;
                }
                break;
            case IntLiteral:
                result = Integer.parseInt(node.getText());
                break;
            case Identifier:
                String varName = node.getText();
                if (variables.containsKey(varName)) {
                    Integer value = variables.get(varName);
                    if (value != null) {
                        result = value;
                    } else {
                        throw new Exception("variable " + varName + " has not been set any value");
                    }
                } else {
                    throw new Exception("unknown variable: " + varName);
                }
                break;
            case AssignmentStmt:
                varName = node.getText();
                if (!variables.containsKey(varName)) {
                    throw new Exception("unknown variable: " + varName);
                }
                // 继续执行下面代码
            case IntDeclaration:
                varName = node.getText();
                Integer varValue = null;
                if (!node.getChildren().isEmpty()) {
                    ASTNode child = node.getChildren().get(0);
                    result = evaluate(child, indent + "\t", verbose);
                    varValue = result;
                }
                variables.put(varName, varValue);
                break;
            default:
                //
        }

        if (verbose) {
            System.out.println(indent + "Result: " + result);
        } else if (indent.equals("")) { // 顶层的语句
            if (node.getType() == ASTNodeType.IntDeclaration || node.getType() == ASTNodeType.AssignmentStmt) {
                System.out.println(node.getText() + ": " + result);
            } else if (node.getType() != ASTNodeType.Programme) {
                System.out.println(result);
            }
        }
        return result;
    }
}
