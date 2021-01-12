package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.parser.FunnyScriptBaseVisitor;
import com.lyr.source.funnyscript.parser.FunnyScriptParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基于AST，产生汇编代码。 限制： 1.目前仅针对macos，64位； 2.仅支持整型，字符串仅支持字面量； 3.运算仅支持加减法。
 *
 * @Author LinYuRong
 * @Date 2021/1/12 18:02
 * @Version 1.0
 */
public class AsmGen extends FunnyScriptBaseVisitor<String> {

    // 之前的编译结果
    private AnnotatedTree at;

    public AsmGen(AnnotatedTree at) {
        this.at = at;
    }

    // 一些临时变量，用于保存扫描代码过程中的累计值
    private int rspOffset = 0; // rsp的偏移量，也就是栈桢的大小

    // 计算过程中的临时变量存放的位置，
    // key: AST的节点
    // value: 该节点存储的地址，可以是寄存器或栈
    private Map<ParserRuleContext, String> tempVars = new HashMap<>();

    // 本地变量存储的地址。目前只在栈中分配，不优化到寄存器中
    // key: 变量
    // value: 该节点存储的地址，在栈中
    private Map<Variable, String> localVars = new HashMap<>();

    // 可以使用的寄存器
    private String[] registersl = {"%eax", "%ebx", "%r10d", "%r11d", "%r12d", "%r13d", "%r14d", "%r15d"};
    private String[] paramRegisterl = {"%edi", "%esi", "%edx", "%ecx", "%r8d", "%r9d"};
    private String[] paramRegisterq = {"%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9"};

    // 扫描某个例程(函数)时生成的代码的缓存区
    private StringBuffer bodyAsm = new StringBuffer();

    // 字符串字面量
    private List<String> stringLiterals = new LinkedList<>();

    // 主控程序
    public String generate() {
        StringBuffer sb = new StringBuffer();

        // 1.代码段的头
        sb.append("\t.section	__TEXT,__text,regular,pure_instructions\n");

        // 2.生成函数的代码
        for (Type type : at.types) {
            if (type instanceof Function) {
                Function function = (Function) type;
                FunnyScriptParser.FunctionDeclarationContext fdc = (FunnyScriptParser.FunctionDeclarationContext) function.ctx;
                visitFunctionDeclaration(fdc); // 遍历，代码生成到bodyAsm中了。
                generateProcedure(function.name, sb);
            }
        }

        // 3.对主程序生成_main函数
        visitProg((FunnyScriptParser.ProgContext) at.ast);
        generateProcedure("main", sb);

        // 4.文本字面量
        sb.append("\n# 字符串字面量\n");
        sb.append("\t.section	__TEXT,__cstring,cstring_literals\n");
        for (int i = 0; i < stringLiterals.size(); i++) {
            sb.append("L.str." + i + ":\n");
            sb.append("\t.asciz\t\"").append(stringLiterals.get(i)).append("\"\n");
        }

        // 5.重置全局的一些临时变量
        stringLiterals.clear();

        return sb.toString();
    }

    /**
     * 生成过程体
     *
     * @param name
     * @param sb
     */
    private void generateProcedure(String name, StringBuffer sb) {
        // 1.函数标签
        sb.append("\n## 过程:").append(name).append("\n");
        sb.append("\t.globl _").append(name).append("\n");
        sb.append("_").append(name).append(":\n");

        // 2.序曲
        sb.append("\n\t# 序曲\n");
        sb.append("\tpushq\t%rbp\n");
        sb.append("\tmovq\t%rsp, %rbp\n");

        // 3.设置栈顶
        // 16字节对齐
        if ((rspOffset % 16) != 0) {
            rspOffset = (rspOffset / 16 + 1) * 16;
        }
        sb.append("\n\t# 设置栈顶\n");
        sb.append("\tsubq\t$").append(rspOffset).append(", %rsp\n");

        // 4.保存用到的寄存器的值
        saveRegisters();

        // 5.函数体
        sb.append("\n\t# 过程体\n");
        sb.append(bodyAsm);

        // 6.恢复受保护的寄存器的值
        restoreRegisters();

        // 7.恢复栈顶
        sb.append("\n\t# 恢复栈顶\n");
        sb.append("\taddq\t$").append(rspOffset).append(", %rsp\n");

        // 8.如果是main函数，设置返回值为0
        if (name.equals("main")) {
            sb.append("\n\t# 返回值\n");
            sb.append("\txorl\t%eax, %eax\n");
        }

        // 9.尾声
        sb.append("\n\t# 尾声\n");
        sb.append("\tpopq\t%rbp\n");
        sb.append("\tretq\n");

        // 10.重置临时变量
        rspOffset = 0;
        localVars.clear();
        tempVars.clear();
        bodyAsm = new StringBuffer();

    }

    // 保存调用者需要保护的寄存器
    // 前提，是某寄存器被用过
    private void saveRegisters() {

    }

    // 恢复被保护的寄存器
    private void restoreRegisters() {

    }

    @Override
    public String visitFunctionDeclaration(FunnyScriptParser.FunctionDeclarationContext ctx) {
        // 给所有参数确定地址
        Function function = (Function) at.node2Scope.get(ctx);
        for (int i = 0; i < function.parameters.size(); i++) {
            if (i < 6) {
                // 少于6个参数，使用寄存器
                localVars.put(function.parameters.get(i), paramRegisterl[i]);
            } else {
                int paramOffset = (i - 6) * 8 + 16; // 参数在栈中相对于%rbp的偏移量
                String paramAddress = "" + paramOffset + "(%rbp)";
                localVars.put(function.parameters.get(i), paramAddress);
            }
        }

        return visitFunctionBody(ctx.functionBody());
    }

    @Override
    public String visitFunctionBody(FunnyScriptParser.FunctionBodyContext ctx) {
        String value = null;
        if (ctx.block() != null) {
            value = visitBlock(ctx.block());
        }
        return value;
    }

    @Override
    public String visitBlock(FunnyScriptParser.BlockContext ctx) {
        return visitBlockStatements(ctx.blockStatements());
    }

    @Override
    public String visitBlockStatements(FunnyScriptParser.BlockStatementsContext ctx) {
        StringBuffer sb = new StringBuffer();
        for (FunnyScriptParser.BlockStatementContext child : ctx.blockStatement()) {
            sb.append(visitBlockStatement(child));
        }
        return sb.toString();
    }

    @Override
    public String visitBlockStatement(FunnyScriptParser.BlockStatementContext ctx) {
        StringBuffer sb = new StringBuffer();
        if (ctx.variableDeclarators() != null) {
            sb.append(visitVariableDeclarators(ctx.variableDeclarators()));
        } else if (ctx.statement() != null) {
            sb.append(visitStatement(ctx.statement()));
        }
        return sb.toString();
    }

    @Override
    public String visitVariableDeclarators(FunnyScriptParser.VariableDeclaratorsContext ctx) {
        StringBuffer sb = new StringBuffer();
        for (FunnyScriptParser.VariableDeclaratorContext child : ctx.variableDeclarator()) {
            sb.append(visitVariableDeclarator(child));
        }
        return sb.toString();
    }

    @Override
    public String visitStatement(FunnyScriptParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public String visitVariableDeclarator(FunnyScriptParser.VariableDeclaratorContext ctx) {
        String varAddress = visitVariableDeclaratorId(ctx.variableDeclaratorId());
        if (ctx.variableInitializer() != null) {
            String value = visitVariableInitializer(ctx.variableInitializer());
            bodyAsm.append("\tmovl\t").append(value).append(", ").append(varAddress).append("\n");
        }
        return varAddress;
    }

    @Override
    public String visitVariableDeclaratorId(FunnyScriptParser.VariableDeclaratorIdContext ctx) {
        // 本地整型变量占4字节
        rspOffset += 4;
        String rtn = "-" + rspOffset + "(%rbp)";

        Symbol symbol = at.symbolOfNode.get(ctx);
        localVars.put((Variable) symbol, rtn);

        return rtn;
    }

    @Override
    public String visitVariableInitializer(FunnyScriptParser.VariableInitializerContext ctx) {
        String rtn = "";
        if (ctx.expression() != null) {
            rtn = visitExpression(ctx.expression());
        }
        return rtn;
    }

    @Override
    public String visitExpression(FunnyScriptParser.ExpressionContext ctx) {
        String address = "";
        // 二元运算
        if (ctx.bop != null && ctx.expression().size() >= 2) {
            String left = visitExpression(ctx.expression(0));
            String right = visitExpression(ctx.expression(1));
            switch (ctx.bop.getType()) {
                case FunnyScriptParser.ADD:
                    //为加法运算申请一个临时的存储位置，可以是寄存器和栈
                    address = allocForExpression(ctx);
                    if (!address.equals(left)) {
                        bodyAsm.append("\tmovl\t").append(left).append(", ").append(address).append("\n");
                    }
                    bodyAsm.append("\taddl\t").append(right).append(", ").append(address).append("\n");
                    break;
                case FunnyScriptParser.SUB:
                    address = allocForExpression(ctx);
                    bodyAsm.append("\tmovl\t").append(left).append(", ").append(address).append("\n");
                    bodyAsm.append("\tsubl\t").append(right).append(", ").append(address).append("\n");
                    break;
                case FunnyScriptParser.ASSIGN:
                    bodyAsm.append("\tmovl\t").append(right).append(", ").append(left).append("\n");
                    break;
            }
        } else if (ctx.primary() != null) {
            address = visitPrimary(ctx.primary());
        } else if (ctx.functionCall() != null) {// functionCall
            address = visitFunctionCall(ctx.functionCall());
        }
        return address;
    }

    @Override
    public String visitPrimary(FunnyScriptParser.PrimaryContext ctx) {
        String rtn = "";
        if (ctx.literal() != null) {
            rtn = visitLiteral(ctx.literal()); // 直接操作数
        } else if (ctx.IDENTIFIER() != null) {
            Symbol symbol = at.symbolOfNode.get(ctx);
            if (symbol instanceof Variable) {
                rtn = localVars.get(symbol); // TODO: 本地变量地址，暂时不支持上一级Scope的变量
            }
        }
        return rtn;
    }

    @Override
    public String visitFunctionCall(FunnyScriptParser.FunctionCallContext ctx) {
        return super.visitFunctionCall(ctx);
    }

    @Override
    public String visitLiteral(FunnyScriptParser.LiteralContext ctx) {
        String rtn = "";
        if (ctx.integerLiteral() != null) {
            rtn = visitIntegerLiteral(ctx.integerLiteral());
        } else if (ctx.STRING_LITERAL() != null) {
            String withQuotationMark = ctx.STRING_LITERAL().getText();
            String withoutQuotationMark = withQuotationMark.substring(1, withQuotationMark.length() - 1);
            rtn = getStringLiteralAddress(withoutQuotationMark);
        }
        return rtn;
    }

    @Override
    public String visitIntegerLiteral(FunnyScriptParser.IntegerLiteralContext ctx) {
        String rtn = "";
        if (ctx.DECIMAL_LITERAL() != null) {
            rtn = "$" + ctx.DECIMAL_LITERAL().getText();
        }
        return rtn;
    }

    private String allocForExpression(FunnyScriptParser.ExpressionContext ctx) {
        String rtn = null;

        //复用前序表达式的存储位置
        if (ctx.bop != null && ctx.expression().size() >= 2) {
            FunnyScriptParser.ExpressionContext left = ctx.expression(0);
            String leftAddress = tempVars.get(left);
            if (leftAddress != null) {
                tempVars.put(ctx, leftAddress);  //当前节点也跟这个地址关联起来。是否可以去掉上一个节点的？
                return leftAddress;
            }
        }

        int availableRegister = getAvailableRegister();
        if (availableRegister != -1) {
            rtn = registersl[availableRegister];
        } else {
            rspOffset += 4;
            rtn = "-" + rspOffset + "%rbp";
        }
        tempVars.put(ctx, rtn);
        return rtn;
    }

    // 获取下一个可用的寄存器的索引
    private int getAvailableRegister() {
        int rtn = -1;
        for (int i = 0; i < registersl.length; i++) {
            String r = registersl[i];
            if (!tempVars.containsValue(r)) {
                rtn = i;
                break;
            }
        }
        return rtn;
    }

    private String getStringLiteralAddress(String str) {
        int index = stringLiterals.indexOf(str);
        if (index == -1) {
            stringLiterals.add(str);
            index = stringLiterals.size() - 1;
        }

        return "ref:L.str." + index + "(%rip)";
    }
}
