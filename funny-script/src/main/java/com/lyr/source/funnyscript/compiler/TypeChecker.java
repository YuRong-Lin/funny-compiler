package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.parser.FunnyScriptBaseListener;
import com.lyr.source.funnyscript.parser.FunnyScriptParser;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * 类型检查。
 * <p>
 * 主要检查:
 * 1.赋值表达式；
 * 2.变量初始化；
 * 3.表达式里的一些运算，比如加减乘除，是否类型匹配；
 * 4.返回值的类型；
 *
 * @Author LinYuRong
 * @Date 2020/12/29 11:47
 * @Version 1.0
 */
public class TypeChecker extends FunnyScriptBaseListener {

    private AnnotatedTree at;

    public TypeChecker(AnnotatedTree at) {
        this.at = at;
    }

    @Override
    public void exitVariableDeclarator(FunnyScriptParser.VariableDeclaratorContext ctx) {
        if (ctx.variableInitializer() != null) {
            Variable variable = (Variable) at.symbolOfNode.get(ctx.variableDeclaratorId());
            Type lType = variable.type;
            Type rType = at.typeOfNode.get(ctx.variableInitializer());
            checkAssign(lType, rType, ctx, ctx.variableDeclaratorId(), ctx.variableInitializer());
        }
    }

    @Override
    public void exitExpression(FunnyScriptParser.ExpressionContext ctx) {
        if (ctx.bop != null && ctx.expression().size() >= 2) {

            Type type1 = at.typeOfNode.get(ctx.expression(0));
            Type type2 = at.typeOfNode.get(ctx.expression(1));

            switch (ctx.bop.getType()) {
                case FunnyScriptParser.ADD:
                    //字符串能够跟任何对象做 + 运算
                    if (type1 != PrimitiveType.String && type2 != PrimitiveType.String) {
                        checkNumericOperand(type1, ctx, ctx.expression(0));
                        checkNumericOperand(type2, ctx, ctx.expression(1));
                    }
                    break;
                case FunnyScriptParser.SUB:
                case FunnyScriptParser.MUL:
                case FunnyScriptParser.DIV:
                case FunnyScriptParser.LE:
                case FunnyScriptParser.LT:
                case FunnyScriptParser.GE:
                case FunnyScriptParser.GT:
                    checkNumericOperand(type1, ctx, ctx.expression(0));
                    checkNumericOperand(type2, ctx, ctx.expression(1));
                    break;
                case FunnyScriptParser.EQUAL:
                case FunnyScriptParser.NOTEQUAL:

                    break;

                case FunnyScriptParser.AND:
                case FunnyScriptParser.OR:
                    checkBooleanOperand(type1, ctx, ctx.expression(0));
                    checkBooleanOperand(type2, ctx, ctx.expression(1));
                    break;

                case FunnyScriptParser.ASSIGN:
                    checkAssign(type1, type2, ctx, ctx.expression(0), ctx.expression(1));
                    break;

                case FunnyScriptParser.ADD_ASSIGN:
                case FunnyScriptParser.SUB_ASSIGN:
                case FunnyScriptParser.MUL_ASSIGN:
                case FunnyScriptParser.DIV_ASSIGN:
                case FunnyScriptParser.AND_ASSIGN:
                case FunnyScriptParser.OR_ASSIGN:
                case FunnyScriptParser.XOR_ASSIGN:
                case FunnyScriptParser.MOD_ASSIGN:
                case FunnyScriptParser.LSHIFT_ASSIGN:
                case FunnyScriptParser.RSHIFT_ASSIGN:
                case FunnyScriptParser.URSHIFT_ASSIGN:
                    if (PrimitiveType.isNumeric(type2)) {
                        if (!checkNumericAssign(type2, type1)) {
                            at.log("can not assign " + ctx.expression(1).getText() + " of type " + type2 + " to " + ctx.expression(0) + " of type " + type1, ctx);
                        }
                    } else {
                        at.log("operand + " + ctx.expression(1).getText() + " should be numeric。", ctx);
                    }

                    break;
            }
        }

        //TODO 对各种一元运算做类型检查，比如NOT操作

    }

    /**
     * 检查类型是不是数值型的。
     *
     * @param type
     * @param exp
     * @param operand
     */
    private void checkNumericOperand(Type type, FunnyScriptParser.ExpressionContext exp, FunnyScriptParser.ExpressionContext operand) {
        if (!(PrimitiveType.isNumeric(type))) {
            at.log("operand for arithmetic operation should be numeric : " + operand.getText(), exp);
        }
    }

    /**
     * 检查类型是不是Boolean型的
     *
     * @param type
     * @param exp
     * @param operand
     */
    private void checkBooleanOperand(Type type, FunnyScriptParser.ExpressionContext exp, FunnyScriptParser.ExpressionContext operand) {
        if (!(type == PrimitiveType.Boolean)) {
            at.log("operand for logical operation should be boolean : " + operand.getText(), exp);
        }
    }

    /**
     * 检查是否能做赋值操作
     *
     * @param lType
     * @param rType
     * @param ctx
     * @param operand1
     * @param operand2
     */
    private void checkAssign(Type lType, Type rType, ParserRuleContext ctx, ParserRuleContext operand1, ParserRuleContext operand2) {
        if (PrimitiveType.isNumeric(rType)) {
            if (!checkNumericAssign(rType, lType)) {
                at.log("can not assign " + operand2.getText() + " of type " + rType + " to " + operand1.getText() + " of type " + lType, ctx);
            }
        } else if (rType instanceof Class) {
            //TODO 检查类的兼容性

        } else if (rType instanceof Function) {
            //TODO 检查函数的兼容性

        }
    }

    /**
     * TODO 完善
     * 看一个类型能否赋值成另一个类型，比如：
     * (1) 整型可以转成浮点型；
     * (2) 子类的对象可以赋给父类;
     * (3) 函数赋值，要求签名是一致的。
     *
     * @param from
     * @param to
     * @return
     */
    private boolean checkNumericAssign(Type from, Type to) {
        boolean canAssign = false;
        if (to == PrimitiveType.Double) {
            canAssign = PrimitiveType.isNumeric(from);
        } else if (to == PrimitiveType.Float) {
            canAssign = (from == PrimitiveType.Byte ||
                    from == PrimitiveType.Short ||
                    from == PrimitiveType.Integer ||
                    from == PrimitiveType.Long ||
                    from == PrimitiveType.Float);
        } else if (to == PrimitiveType.Long) {
            canAssign = (from == PrimitiveType.Byte ||
                    from == PrimitiveType.Short ||
                    from == PrimitiveType.Integer ||
                    from == PrimitiveType.Long);
        } else if (to == PrimitiveType.Integer) {
            canAssign = (from == PrimitiveType.Byte ||
                    from == PrimitiveType.Short ||
                    from == PrimitiveType.Integer);
        } else if (to == PrimitiveType.Short) {
            canAssign = (from == PrimitiveType.Byte ||
                    from == PrimitiveType.Short);
        } else if (to == PrimitiveType.Byte) {
            canAssign = (from == PrimitiveType.Byte);
        }

        return canAssign;
    }
}
