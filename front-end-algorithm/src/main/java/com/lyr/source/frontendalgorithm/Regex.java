package com.lyr.source.frontendalgorithm;

/**
 * @Author LinYuRong
 * @Date 2021/1/5 15:17
 * @Version 1.0
 */
public class Regex {

    public static void main(String[] args) {

        ///////////////////
        //第一个例子
        //正则表达式：int|[a-zA-Z][a-zA-Z0-9]*|[0-9]+
        //也就是：int关键字、标识符或数字字面量
        GrammarNode rootNode = sampleGrammar1();
        rootNode.dump();

        System.out.println("\nNFA states:");
        State[] states = regexToNFA(rootNode);
        states[0].dump();

        //用NFA来匹配
        matchWithNFA(states[0], "int");
        matchWithNFA(states[0], "intA");
        matchWithNFA(states[0], "23");
        matchWithNFA(states[0], "0A");
    }

    /**
     * 把正则表达式翻译成NFA
     *
     * @param node
     * @return
     */
    public static State[] regexToNFA(GrammarNode node) {
        State beginState = null;
        State endState = null;

        switch (node.getType()) {
            // 转换s|t
            case Or:
                beginState = new State();
                endState = new State(true);
                for (GrammarNode child : node.children()) {
                    // 递归，生成子图，返回头尾2个状态
                    State[] childState = regexToNFA(child);
                    // beginState 通过ε接到子图的开始状态
                    beginState.addTransition(new CharTransition(), childState[0]);
                    // 子图的结束状态，通过ε接到endState
                    childState[1].addTransition(new CharTransition(), endState);
                    childState[1].setAcceptable(false);
                }
                break;
            // 转换st
            case And:
                State[] lastChildState = null;
                int index = 0;
                for (GrammarNode child : node.children()) {
                    State[] childState = regexToNFA(child);
                    if (lastChildState != null) {
                        // 把前一个子图的接受状态和后一个子图的开始状态合并，把两个子图接到一起
                        lastChildState[1].copyTransitions(childState[0]);
                        lastChildState[1].setAcceptable(false);
                    }
                    lastChildState = childState;

                    // 整体的开始状态
                    if (index == 0) {
                        beginState = childState[0];
                    }

                    // 整体的结束状态
                    endState = childState[1];
                    index++;
                }
                break;
            // 处理普通的字符
            case Char:
                beginState = new State();
                endState = new State(true);
                // 图的边上是当前节点的charSet，也就是导致迁移的字符的集合，比如所有字母
                beginState.addTransition(new CharTransition(node.getCharSet()), endState);
                break;

        }

        State[] rtn = null;
        // 重复，增加必要的节点和边
        if (node.getMinTimes() != 1 || node.getMaxTimes() != 1) {
            rtn = addRepetition(beginState, endState, node);
        } else {
            rtn = new State[]{beginState, endState};
        }

        // 为命了名的语法节点做标记，后面将用来设置Token类型。
        if (node.getName() != null) {
            rtn[1].setGrammarNode(node);
        }

        return rtn;
    }

    /**
     * 看看str是否符合NFA
     *
     * @param state NFA的起始状态
     * @param str
     */
    private static boolean matchWithNFA(State state, String str) {
        System.out.println("NFA matching: '" + str + "'");
        char[] chars = str.toCharArray();
        int index = matchWithNFA(state, chars, 0);

        boolean match = index == chars.length;

        System.out.println("matched? : " + match + "\n");

        return match;
    }

    /**
     * 用NFA来匹配字符串
     *
     * @param state 当前所在的状态
     * @param chars 要匹配的字符串，用数组表示
     * @param index 当前匹配字符开始的位置。
     * @return 匹配后，新index的位置。指向匹配成功的字符的下一个字符。
     */
    private static int matchWithNFA(State state, char[] chars, int index) {
        System.out.println("trying state : " + state.getName() + ", index =" + index);

        int index2 = index;
        for (Transition transition : state.transitions()) {
            State nextState = state.getState(transition);
            // epsilon转换
            if (transition.isEpsilon()) {
                index2 = matchWithNFA(nextState, chars, index);
                if (index2 == chars.length) {
                    break;
                }
            } else if (transition.match(chars[index])) {
                index2++;
                if (index2 < chars.length) {
                    index2 = matchWithNFA(nextState, chars, index + 1);
                } else {
                    // 如果已经扫描完所有字符
                    // 检查当前状态是否是接受状态，或者可以通过epsilon到达接受状态
                    // 如果状态机还没有到达接受状态，本次匹配失败
                    if (acceptable(nextState)) {
                        break;
                    } else {
                        index2 = -1;
                    }
                }
            }
        }
        return index2;
    }

    /**
     * 支持 * ？ +
     * 在两边增加额外的状态，并增加额外的连线
     *
     * @param state1
     * @param state2
     * @param node
     * @return
     */
    private static State[] addRepetition(State state1, State state2, GrammarNode node) {
        State beginState = null;
        State endState = null;

        // 允许循环
        if (node.getMaxTimes() == -1 || node.getMaxTimes() > 1) {
            state2.addTransition(new CharTransition(node.getMaxTimes()), state1);
        }

        // 允许0次，再加2个节点
        if (node.getMinTimes() == 0) {
            beginState = new State();
            endState = new State(true);
            beginState.addTransition(new CharTransition(), state1);
            state2.addTransition(new CharTransition(), endState);
            state2.setAcceptable(false);

            beginState.addTransition(new CharTransition(), endState);
        } else {
            beginState = state1;
            endState = state2;
        }

        return new State[]{beginState, endState};
    }

    /**
     * 查找当前状态是不是一个接受状态，或者可以通过epsilon迁移到一个接受状态。
     *
     * @param state
     * @return
     */
    private static boolean acceptable(State state) {
        if (state.isAcceptable()) {
            return true;
        }

        boolean rtn = false;

        for (Transition transition : state.transitions()) {
            if (transition.isEpsilon()) {
                State nextState = state.getState(transition);
                if (nextState.isAcceptable()) {
                    rtn = true;
                    break;
                } else {
                    rtn = acceptable(nextState);
                    if (rtn) {
                        break;
                    }
                }
            }
        }

        return rtn;
    }

    /**
     * 创建一个示例用的正则表达式：
     * int | [a-zA-z][a-zA-Z0-9]* | [0-9]*
     *
     * @return
     */
    private static GrammarNode sampleGrammar1() {
        GrammarNode node = new GrammarNode("regex1", GrammarNodeType.Or);

        // int关键字
        GrammarNode intNode = node.createChild(GrammarNodeType.And);
        intNode.createChild(new CharSet('i'));
        intNode.createChild(new CharSet('n'));
        intNode.createChild(new CharSet('t'));

        // 标识符
        GrammarNode idNode = node.createChild(GrammarNodeType.And);
        GrammarNode firstLetter = idNode.createChild(CharSet.letter);

        GrammarNode letterOrDigit = idNode.createChild(CharSet.letterOrDigit);
        letterOrDigit.setRepeatTimes(0, -1);

        // 数字字面量
        GrammarNode literalNode = node.createChild(CharSet.digit);
        literalNode.setRepeatTimes(1, -1);

        return node;
    }

    /**
     * 正则表达式：a[a-zA-Z0-9]*bc
     *
     * @return
     */
    private static GrammarNode sampleGrammar2() {
        GrammarNode node = new GrammarNode("regex2", GrammarNodeType.And);

        node.createChild(new CharSet('a'));
        GrammarNode letterOrDigit = node.createChild(CharSet.letterOrDigit);
        letterOrDigit.setRepeatTimes(0, -1);

        node.createChild(new CharSet('b'));
        node.createChild(new CharSet('c'));

        return node;
    }
}
