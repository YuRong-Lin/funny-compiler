package com.lyr.source.frontendalgorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author LinYuRong
 * @Date 2021/1/6 16:18
 * @Version 1.0
 */
public class FirstFollowSet {

    /**
     * 计算First集合。
     * 采用了不动点法。
     */
    public static Map<GrammarNode, Set<String>> calcFirstSets(GrammarNode grammar) {
        Map<GrammarNode, Set<String>> firstSets = new HashMap<>();

        boolean stable = calcFirstSets(grammar, firstSets, new HashSet<>());
        int i = 1;
        System.out.println("calcFirstSets round : " + i++);
        while (!stable) {
            System.out.println("calcFirstSets round : " + i++);
            stable = calcFirstSets(grammar, firstSets, new HashSet<>());
        }

        return firstSets;
    }

    /**
     * 对First集合做一次计算。
     *
     * @param grammar
     * @param firstSets
     * @return 如果这次计算，First集合的成员都没有变动，则返回true。
     */
    private static boolean calcFirstSets(GrammarNode grammar,
                                         Map<GrammarNode, Set<String>> firstSets,
                                         Set<GrammarNode> calculated) {
        // 标记正在计算该节点，避免重复调用
        calculated.add(grammar);

        // 获取或创建First集合
        Set<String> firstSet = null;
        if (firstSets.containsKey(grammar)) {
            firstSet = firstSets.get(grammar);
        } else {
            firstSet = new HashSet<>();
            firstSets.put(grammar, firstSet);
        }

        boolean stable = true;

        if (!grammar.isLeaf()) {
            // 先把所有的子节点都计算一遍
            for (GrammarNode child : grammar.children()) {
                if (!child.isLeaf() && !calculated.contains(child)) {
                    // TODO 是否需要记录Stable
                    calcFirstSets(child, firstSets, calculated);
                }
            }

            List<GrammarNode> childToAdd = new LinkedList<>();

            if (grammar.getType() == GrammarNodeType.And) {
//                childToAdd.add(grammar.getChild(0));

                // 要一直找到一个不产生Epsilon的符号
                for (GrammarNode child : grammar.children()) {
                    childToAdd.add(child);
                    if (!child.isNullable()) {
                        break;
                    }
                }
            } else if (grammar.getType() == GrammarNodeType.Or) {
                for (GrammarNode child : grammar.children()) {
                    childToAdd.add(child);
                }
            }

            for (GrammarNode child : childToAdd) {
                if (!child.isLeaf()) {
                    Set<String> childSet = firstSets.get(child);

                    if (!firstSet.containsAll(childSet)) {
                        firstSet.addAll(childSet);
                        stable = false;
                    }
                } else if (child.isToken()) {
                    if (!firstSet.contains(child.getToken().getType())) {
                        stable = false;
                        firstSet.add(child.getToken().getType());
                    }
                }

                if (child.isNullable()) {
                    if (!firstSet.contains("EPSILON")) {
                        firstSet.add("EPSILON");
                    }
                }
            }
        }

        return stable;
    }


    /**
     * 计算Follow集合。
     * 对所有节点计算。
     *
     * @param grammar 入口语法节点。
     * @return
     */
    public static Map<GrammarNode, Set<String>> calcFollowSets(GrammarNode grammar,
                                                               Map<GrammarNode, Set<String>> firstSets) {
        Map<GrammarNode, Set<String>> followSets = new HashMap<>();
        Map<GrammarNode, Set<GrammarNode>> rightChildrenSets = new HashMap<>();

        // 不动点法计算Follow集合
        int i = 1;
        System.out.println("follow set round : " + i++);
        boolean stable = calcFollowSets(grammar, followSets, rightChildrenSets,
                firstSets, new HashSet<>());

        while (!stable) {
            System.out.println("follow set round : " + i++);
            stable = calcFollowSets(grammar, followSets, rightChildrenSets,
                    firstSets, new HashSet<>());
        }

        // 为根节点最右边的边，也就是所有可能出现在程序末尾的非终结符，加上$，也就是整个Token串结束符号
        Set<String> tempFollowSet = new HashSet<>();
        tempFollowSet.add("$");
        addToRightChild(grammar, tempFollowSet, followSets, rightChildrenSets, new HashSet<>());

        // 给根节点加上$。根节点如果没有被递归引用，不会出现在followSets中。
        Set<String> rootFollowSet = followSets.get(grammar);
        if (rootFollowSet == null) {
            rootFollowSet = new HashSet<>();
            followSets.put(grammar, rootFollowSet);
        }
        rootFollowSet.add("$");

        return followSets;
    }

    /**
     * 计算一遍Follow节点
     *
     * @param grammar
     * @param followSets
     * @return
     */
    private static boolean calcFollowSets(GrammarNode grammar,
                                          Map<GrammarNode, Set<String>> followSets,
                                          Map<GrammarNode, Set<GrammarNode>> rightChildrenSets,
                                          Map<GrammarNode, Set<String>> firstSets,
                                          Set<GrammarNode> calculated) {

        calculated.add(grammar);
        boolean stable = true;

        if (!grammar.isLeaf()) {
            Set<GrammarNode> rightChildren = rightChildrenSets.get(grammar);
            if (rightChildren == null) {
                rightChildren = new HashSet<>();
                rightChildrenSets.put(grammar, rightChildren);
            }

            if (grammar.getType() == GrammarNodeType.And) {
                for (int i = 0; i < grammar.getChildCount(); i++) {
                    GrammarNode left = grammar.getChild(i);

                    if (!left.isLeaf()) {
                        if (!calculated.contains(left)) {
                            if (!calcFollowSets(left, followSets, rightChildrenSets, firstSets, calculated)) {
                                stable = false;
                            }
                        }

                        Set<String> followSet = followSets.get(left);
                        if (followSet == null) {
                            followSet = new HashSet();
                            followSets.put(left, followSet);
                        }

                        if (i == grammar.getChildCount() - 1) {
                            rightChildren.add(left);
                        } else {
                            boolean foundNotNull = false;
                            for (int j = i + 1; j < grammar.getChildCount(); j++) {
                                GrammarNode right = grammar.getChild(j);
                                Set<String> tempFollowSet = new HashSet<>();
                                if (!right.isLeaf()) {
                                    if (firstSets.get(right) == null) {
                                        // TODO ???
                                        System.out.println("");
                                    }
                                    tempFollowSet.addAll(firstSets.get(right));
                                } else if (right.isToken()) {
                                    tempFollowSet.add(right.getToken().getType());
                                }

                                assert (tempFollowSet.size() > 0);
                                if (!followSet.containsAll(tempFollowSet)) {
                                    followSet.addAll(tempFollowSet);
                                    stable = false;
                                }

                                if (!addToRightChild(left, tempFollowSet, followSets, rightChildrenSets, new HashSet<>())) {
                                    stable = false;
                                }

                                //必须找到一个非空的
                                if (!right.isNullable()) {
                                    foundNotNull = true;
                                    break;
                                }
                            }
                            // 本节点也是最右节点
                            if (!foundNotNull) {
                                rightChildren.add(left);
                            }
                        }
                    }
                }
            } else if (grammar.getType() == GrammarNodeType.Or) {
                for (GrammarNode child : grammar.children()) {
                    if (!child.isLeaf()) {
                        rightChildren.add(child);
                        if (!calculated.contains(child)) {
                            if (!calcFollowSets(child, followSets, rightChildrenSets, firstSets, calculated)) {
                                stable = false;
                            }
                        }
                    }
                }
            }

        }

        return stable;
    }


    /**
     * 把某个节点的Follow集合，也给它所有右边分枝的后代节点。
     *
     * @param grammar
     * @param followSet
     * @param followSets
     * @param rightChildrenSets
     * @param added
     * @return
     */
    private static boolean addToRightChild(GrammarNode grammar,
                                           Set<String> followSet,
                                           Map<GrammarNode, Set<String>> followSets,
                                           Map<GrammarNode, Set<GrammarNode>> rightChildrenSets,
                                           Set<GrammarNode> added) {

        added.add(grammar);

        boolean stable = true;
        Set<GrammarNode> rightChildren = rightChildrenSets.get(grammar);
        for (GrammarNode rightChild : rightChildren) {
            if (!rightChild.isLeaf() && !added.contains(rightChild)) {
                Set<String> childFollowSet = followSets.get(rightChild);
                if (childFollowSet == null) {
                    childFollowSet = new HashSet<>();
                    followSets.put(rightChild, childFollowSet);
                }
                if (!childFollowSet.containsAll(followSet)) {
                    childFollowSet.addAll(followSet);
                    stable = false;
                }
                // 递归向下
                if (!addToRightChild(rightChild, followSet, followSets, rightChildrenSets, added)) {
                    stable = false;
                }
            }
        }

        return stable;
    }


    /**
     * 打印输出First或Follow集合。
     *
     * @param sets
     */
    public static void dumpFirstFollowSets(Map<GrammarNode, Set<String>> sets) {
        for (GrammarNode node : sets.keySet()) {
            Set<String> set = sets.get(node);
            String str = node.toString() + " : ";
            for (String tokenName : set) {
                str += " " + tokenName;
            }
            System.out.println(str);
        }
    }
}
