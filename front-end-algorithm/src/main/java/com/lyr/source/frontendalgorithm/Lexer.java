package com.lyr.source.frontendalgorithm;

import org.springframework.util.Assert;

import java.io.CharArrayReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 一个词法分析器。可以根据正则表达式做词法分析。
 * 原理：
 * 1.把正则表达式转换成NFA，再转成DFA。
 * 2.要在NFA的State上标记关联的GrammarNode，以便区分被DFA识别出来的是哪种Token。这在Regex的regexToNFA中已经做了。
 *
 * @Author LinYuRong
 * @Date 2021/1/5 15:18
 * @Version 1.0
 */
public class Lexer extends Regex {

    public static void main(String[] args) {
        GrammarNode lexerGrammar = SampleLexerGrammar.commonLexerGrammar();
        State[] nfaStates = regexToNFA(lexerGrammar);
        List<DFAState> dfaStates = NFA2DFA(nfaStates[0], CharSet.ascii);

        System.out.println("\ndump NFA:");
        nfaStates[0].dump();

        System.out.println("\ndump DFA:");
        dfaStates.get(0).dump();

        String code = "int i = 0; i + 100; if (a == 10) println(a); a <= b;";
        List<Token> tokens = tokenize(code, dfaStates.get(0), lexerGrammar);

        System.out.println("\nTokens:");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    /**
     * 逻辑：
     * 1.找到能消化接下来的字符的DFA；
     * 2.针对这个DFA一直给它发字符，直到不能接受；
     * 3.查看是否是处于结束状态。
     *
     * @param code
     * @param startState
     * @param root
     * @return
     */
    private static List<Token> tokenize(String code, DFAState startState, GrammarNode root) {
        List<Token> tokens = new LinkedList<>();

        CharArrayReader reader = new CharArrayReader(code.toCharArray());

        DFAState currentState = startState;
        DFAState nextState = null;

        int ich = 0;
        char ch = 0;

        StringBuilder tokenText = new StringBuilder();
        try {
            // 第二个条件，是为了生成最后一个Token
            while ((ich = reader.read()) != -1 || tokenText.length() > 0) {
                ch = (char) ich;

                boolean consumed = false;
                while (!consumed) {
                    nextState = currentState.getNextState(ch);
                    if (nextState == null) {
                        if (currentState == startState) {
                            // TODO 不认识的字符，暂时忽略
                            consumed = true;
                        } else if (currentState.isAcceptable()) {
                            // 查找对应的词法规则
                            GrammarNode grammar = getGrammar(currentState, root);
                            Assert.notNull(grammar, "不合法token");

                            if (!grammar.isNeglect()) {
                                Token token = new Token(grammar.getName(), tokenText.toString());
                                tokens.add(token);
                            }
                            tokenText = new StringBuilder();

                            // 回到初始状态，重新匹配
                            currentState = startState;
                        } else {
                            // 遇到了不认识的字符，没有到达结束态，但也无法迁移  //TODO 暂时忽略
                            consumed = true;
                        }
                    } else {
                        // 做状态迁移
                        currentState = nextState;
                        // 累计tokenText
                        tokenText.append(ch);
                        consumed = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tokens;
    }

    /**
     * 检查DFAState中的各个NFAState，看是否是某个词法规则的结束节点。
     * 注意：如果有符合两个词法规则，那么以宣布顺序的先后算。比如关键字和标识符就会重叠。
     *
     * @param currentState
     * @param root
     * @return
     */
    private static GrammarNode getGrammar(DFAState currentState, GrammarNode root) {
        // 找出state符合的所有词法
        Set<GrammarNode> validGrammars = new HashSet<>();
        for (State state : currentState.states()) {
            if (state.getGrammarNode() != null) {
                validGrammars.add(state.getGrammarNode());
            }
        }

        // 按顺序遍历词法规则，声明在前的优先级更高
        GrammarNode rtn = null;
        for (GrammarNode grammar : root.children()) {
            if (grammar.getName() != null) {
                if (validGrammars.contains(grammar)) {
                    rtn = grammar;
                    break;
                }
            }
        }
        return rtn;
    }
}
