package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.parser.FunnyScriptBaseListener;
import com.lyr.source.funnyscript.parser.FunnyScriptParser;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * 语义解析的第三步：引用消解和类型推断
 * 1.解析所有的本地变量引用、函数调用和类成员引用。
 * 2.类型推断：从下而上推断表达式的类型。
 * <p>
 * 这两件事要放在一起做，因为：
 * (1)对于变量，只有做了消解，才能推断出类型来。
 * (2)对于FunctionCall，只有把参数（表达式)的类型都推断出来，才能匹配到正确的函数（方法)。
 * (3)表达式里包含FunctionCall,所以要推导表达式的类型，必须知道是哪个Function，从而才能得到返回值。
 *
 * @Author LinYuRong
 * @Date 2020/12/28 10:25
 * @Version 1.0
 */
public class RefResolver extends FunnyScriptBaseListener {

    private AnnotatedTree at;

    // 用于把本地变量添加到符号表，并计算类型
    private ParseTreeWalker typeResolverWalker = new ParseTreeWalker();
    private TypeResolver typeResolver;

    protected RefResolver(AnnotatedTree at) {
        this.at = at;
        this.typeResolver = new TypeResolver(at, true);
    }

    /**
     * 根据字面量推断
     *
     * @param ctx
     */
    @Override
    public void exitLiteral(FunnyScriptParser.LiteralContext ctx) {
        if (ctx.BOOL_LITERAL() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Boolean);
        } else if (ctx.CHAR_LITERAL() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Char);
        } else if (ctx.NULL_LITERAL() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Null);
        } else if (ctx.STRING_LITERAL() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.String);
        } else if (ctx.integerLiteral() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Integer);
        } else if (ctx.floatLiteral() != null) {
            at.typeOfNode.put(ctx, PrimitiveType.Float);
        }
    }

    @Override
    public void exitPrimary(FunnyScriptParser.PrimaryContext ctx) {
        Type type = null;

        if (ctx.IDENTIFIER() != null) {
            Scope scope = at.enclosingScopeOfNode(ctx);
            String idName = ctx.IDENTIFIER().getText();

            Variable variable = at.lookupVariable(scope, idName);
            if (variable == null) {
                // 看看是不是函数，因为函数可以作为值来传递。这个时候，函数重名没法区分。
                // 因为普通Scope中的函数是不可以重名的，所以这应该是没有问题的。  //TODO 但如果允许重名，那就不行了。
                // TODO 注意，查找function的时候，可能会把类的方法包含进去
                Function function = at.lookupFunction(scope, idName);
                if (function != null) {
                    at.symbolOfNode.put(ctx, function);
                    type = function;
                } else {
                    at.log("unknown variable or function: " + idName, ctx);
                }
            } else {
                at.symbolOfNode.put(ctx, variable);
                type = variable.type;
            }
        } else if (ctx.literal() != null) {
            type = at.typeOfNode.get(ctx.literal());
        } else if (ctx.expression() != null) {
            type = at.typeOfNode.get(ctx.expression());
        } else if (ctx.THIS() != null) {
            Class theClass = at.enclosingClassOfNode(ctx);
            if (theClass != null) {
                This variable = theClass.getThis();
                at.symbolOfNode.put(ctx, variable);
                type = theClass;
            } else {
                at.log("keyword \"this\" can only be used inside a class", ctx);
            }
        } else if (ctx.SUPER() != null) {
            Class theClass = at.enclosingClassOfNode(ctx);
            if (theClass != null) {
                Super variable = theClass.getSuper();
                at.symbolOfNode.put(ctx, variable);
                type = theClass;
            } else {
                at.log("keyword \"super\" can only be used inside a class", ctx);
            }
        }
        // 类型推断，冒泡
        at.typeOfNode.put(ctx, type);
    }

    /**
     * @param ctx
     */
    @Override
    public void exitExpression(FunnyScriptParser.ExpressionContext ctx) {
        Type type = null;

        // 消解处理点符号表达式的层层引用
        if (ctx.bop != null && ctx.bop.getType() == FunnyScriptParser.DOT) {
            // 这是个左递归，要不断的把左边的节点的计算结果存到symbolOfNode，所以要在exitExpression里操作
            Symbol symbol = at.symbolOfNode.get(ctx.expression(0));
            if (symbol instanceof Variable && ((Variable) symbol).type instanceof Class) {
                Class theClass = (Class) ((Variable) symbol).type;
                // 引用类的属性
                if (ctx.IDENTIFIER() != null) {
                    String idName = ctx.IDENTIFIER().getText();
                    Variable variable = at.lookupVariable(theClass, idName);
                    if (variable != null) {
                        at.symbolOfNode.put(ctx, variable);
                        type = variable.type;
                    } else {
                        at.log("unable to find field " + idName + " in Class " + theClass.name, ctx);
                    }
                } else if (ctx.functionCall() != null) {
                    type = at.typeOfNode.get(ctx.functionCall());
                }
            } else {
                at.log("symbol is not a qualified object：" + symbol, ctx);
            }
        }

        //变量引用冒泡： 如果下级是一个变量，往上冒泡传递，以便在点符号表达式中使用
        //也包括This和Super的冒泡
        if (ctx.primary() != null) {
            Symbol symbol = at.symbolOfNode.get(ctx.primary());
            at.symbolOfNode.put(ctx, symbol);

            type = at.typeOfNode.get(ctx.primary());
        } else if (ctx.functionCall() != null) {
            type = at.typeOfNode.get(ctx.functionCall());
        } else if (ctx.bop != null && ctx.expression().size() >= 2) {
            Type type1 = at.typeOfNode.get(ctx.expression(0));
            Type type2 = at.typeOfNode.get(ctx.expression(1));

            switch (ctx.bop.getType()) {
                case FunnyScriptParser.ADD:
                    if (type1 == PrimitiveType.String || type2 == PrimitiveType.String) {
                        type = PrimitiveType.String;
                    } else if (type1 instanceof PrimitiveType && type2 instanceof PrimitiveType) {
                        //类型“向上”对齐，比如一个int和一个float，取float
                        type = PrimitiveType.getUpperType(type1, type2);
                    } else {
                        at.log("operand should be PrimitiveType for additive and multiplicative operation", ctx);
                    }
                    break;
                case FunnyScriptParser.SUB:
                case FunnyScriptParser.MUL:
                case FunnyScriptParser.DIV:
                    if (type1 instanceof PrimitiveType && type2 instanceof PrimitiveType) {
                        //类型“向上”对齐，比如一个int和一个float，取float
                        type = PrimitiveType.getUpperType(type1, type2);
                    } else {
                        at.log("operand should be PrimitiveType for additive and multiplicative operation", ctx);
                    }

                    break;
                case FunnyScriptParser.EQUAL:
                case FunnyScriptParser.NOTEQUAL:
                case FunnyScriptParser.LE:
                case FunnyScriptParser.LT:
                case FunnyScriptParser.GE:
                case FunnyScriptParser.GT:
                case FunnyScriptParser.AND:
                case FunnyScriptParser.OR:
                case FunnyScriptParser.BANG:
                    type = PrimitiveType.Boolean;
                    break;
                case FunnyScriptParser.ASSIGN:
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
                    type = type1;
                    break;
            }
        }

        //类型冒泡
        at.typeOfNode.put(ctx, type);
    }

    /**
     * 把本地变量加到符号表。本地变量必须是边添加，边解析，不能先添加后解析，否则会引起引用消解的错误。
     *
     * @param ctx
     */
    @Override
    public void enterVariableDeclarators(FunnyScriptParser.VariableDeclaratorsContext ctx) {
        Scope scope = at.enclosingScopeOfNode(ctx);
        // TODO 函数本地变量？？
        if (scope instanceof BlockScope) {
            typeResolverWalker.walk(typeResolver, ctx);
        }
    }


}
