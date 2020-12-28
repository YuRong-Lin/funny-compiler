package com.lyr.source.funnyscript.compiler;

import com.lyr.source.funnyscript.parser.FunnyScriptBaseListener;
import com.lyr.source.funnyscript.parser.FunnyScriptParser;

/**
 * 第二遍扫描。把变量、类继承、函数声明的类型都解析出来。
 * 也就是所有用到typeType的地方。
 *
 * 注：
 * 实际运行时，把变量添加到符号表，是分两步来做的。
 * 第一步，是把类成员变量和函数的参数加进去
 *
 * 第二步，是在变量引用消解的时候再添加。这个时候是边Enter符号表，边消解，会避免变量引用到错误的定义。
 *
 * @Author LinYuRong
 * @Date 2020/12/25 10:43
 * @Version 1.0
 */
public class TypeResolver extends FunnyScriptBaseListener {

    private AnnotatedTree at;

    // 是否把本地变量加入符号表
    private boolean enterLocalVariable = false;

    protected TypeResolver(AnnotatedTree at) {
        this.at = at;
    }

    protected TypeResolver(AnnotatedTree at, boolean enterLocalVariable) {
        this(at);
        this.enterLocalVariable = enterLocalVariable;
    }

    /**
     * 设置父类(解析extends)
     *
     * @param ctx
     */
    @Override
    public void enterClassDeclaration(FunnyScriptParser.ClassDeclarationContext ctx) {
        Class theClass = (Class) at.node2Scope.get(ctx);

        // parent
        if (ctx.EXTENDS() != null) {
            String parentClassName = ctx.typeType().getText();
            Type type = at.lookupClassType(parentClassName);
            if (type != null) {
                theClass.setParentClass((Class) type);
            } else {
                at.log("unknown class: " + parentClassName, ctx);
            }
        }
    }

    /**
     * 1、将类成员的变量和函数参数加入符号表
     * 2、将本地变量加入到符号表
     *
     * @param ctx
     */
    @Override
    public void enterVariableDeclaratorId(FunnyScriptParser.VariableDeclaratorIdContext ctx) {
        String idName = ctx.IDENTIFIER().getText();
        Scope scope = at.enclosingScopeOfNode(ctx);

        // 第一步只把类的成员变量入符号表。在变量消解(第三步)时，再把本地变量加入符号表，一边Enter，一边消解。
        // 理解：本地变量有可能依赖全局变量或类成员变量，故分步处理
        if (scope instanceof Class || ctx.parent instanceof FunnyScriptParser.FormalParameterContext || enterLocalVariable) {
            Variable variable = new Variable(idName, scope, ctx);

            if (Scope.getVariable(scope, idName) != null) {
                at.log("Variable or parameter already Declared: " + idName, ctx);
            }

            scope.addSymbol(variable);
            at.symbolOfNode.put(ctx, variable);
        }
    }

    /**
     * 变量的类型（类或接口）
     *
     * @param ctx
     */
    @Override
    public void enterClassOrInterfaceType(FunnyScriptParser.ClassOrInterfaceTypeContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            Scope scope = at.enclosingScopeOfNode(ctx);
            String idName = ctx.getText();
            Class theClass = at.lookupClass(scope, idName);
            at.typeOfNode.put(ctx, theClass);
        }
    }

    /**
     * 基本数据类型
     *
     * @param ctx
     */
    @Override
    public void exitPrimitiveType(FunnyScriptParser.PrimitiveTypeContext ctx) {
        Type type = null;
        if (ctx.BOOLEAN() != null) {
            type = PrimitiveType.Boolean;
        } else if (ctx.INT() != null) {
            type = PrimitiveType.Integer;
        } else if (ctx.LONG() != null) {
            type = PrimitiveType.Long;
        } else if (ctx.FLOAT() != null) {
            type = PrimitiveType.Float;
        } else if (ctx.DOUBLE() != null) {
            type = PrimitiveType.Double;
        } else if (ctx.BYTE() != null) {
            type = PrimitiveType.Byte;
        } else if (ctx.SHORT() != null) {
            type = PrimitiveType.Short;
        } else if (ctx.CHAR() != null) {
            type = PrimitiveType.Char;
        } else if (ctx.STRING() != null) {
            type = PrimitiveType.String;
        }
        at.typeOfNode.put(ctx, type);
    }

    @Override
    public void exitTypeType(FunnyScriptParser.TypeTypeContext ctx) {
        // 冒泡，将下级的属性标注在本级
        if (ctx.classOrInterfaceType() != null) {
            Type type = at.typeOfNode.get(ctx.classOrInterfaceType());
            at.typeOfNode.put(ctx, type);
        } else if (ctx.functionType() != null) {
            Type type = at.typeOfNode.get(ctx.functionType());
            at.typeOfNode.put(ctx, type);
        } else if (ctx.primitiveType() != null) {
            Type type = at.typeOfNode.get(ctx.primitiveType());
            at.typeOfNode.put(ctx, type);
        }
    }

    @Override
    public void exitTypeTypeOrVoid(FunnyScriptParser.TypeTypeOrVoidContext ctx) {
        if (ctx.VOID() != null) {
            at.typeOfNode.put(ctx, VoidType.instance());
        } else if (ctx.typeType() != null) {
            at.typeOfNode.put(ctx, at.typeOfNode.get(ctx.typeType()));
        }
    }

    /**
     * 函数类型
     *
     * @param ctx
     */
    @Override
    public void exitFunctionType(FunnyScriptParser.FunctionTypeContext ctx) {
        DefaultFunctionType functionType = new DefaultFunctionType();
        at.types.add(functionType);

        at.typeOfNode.put(ctx, functionType);

        // 返回值类型
        functionType.returnType = at.typeOfNode.get(ctx.typeTypeOrVoid());

        // 参数的类型
        if (ctx.typeList() != null) {
            FunnyScriptParser.TypeListContext tcl = ctx.typeList();
            for (FunnyScriptParser.TypeTypeContext ttc : tcl.typeType()) {
                Type type = at.typeOfNode.get(ttc);
                functionType.paramTypes.add(type);
            }
        }
    }

    /**
     * 设置函数的参数的类型，这些参数已经在enterVariableDeclaratorId中作为变量声明了，现在设置它们的类型
     *
     * @param ctx
     */
    @Override
    public void exitFormalParameter(FunnyScriptParser.FormalParameterContext ctx) {
        // 设置参数类型
        Type type = at.typeOfNode.get(ctx.typeType());
        Variable variable = (Variable) at.symbolOfNode.get(ctx.variableDeclaratorId());
        variable.type = type;

        // 添加到函数的参数列表里
        Scope scope = at.enclosingScopeOfNode(ctx);
        //TODO 从目前的语法来看，只有function才会使用FormalParameter
        if (scope instanceof Function) {
            ((Function) scope).parameters.add(variable);
        }
    }

    /**
     * 设置声明的变量的类型
     *
     * @param ctx
     */
    @Override
    public void exitVariableDeclarators(FunnyScriptParser.VariableDeclaratorsContext ctx) {
        Scope scope = at.enclosingScopeOfNode(ctx);

        if (scope instanceof Class || enterLocalVariable) {
            // 设置变量类型
            Type type = at.typeOfNode.get(ctx.typeType());

            for (FunnyScriptParser.VariableDeclaratorContext child : ctx.variableDeclarator()) {
                Variable variable = (Variable) at.symbolOfNode.get(child.variableDeclaratorId());
                variable.type = type;
            }
        }
    }

    /**
     * 设置函数的返回类型及查重
     *
     * @param ctx
     */
    @Override
    public void exitFunctionDeclaration(FunnyScriptParser.FunctionDeclarationContext ctx) {
        Function function = (Function) at.node2Scope.get(ctx);
        if (ctx.typeTypeOrVoid() != null) {
            function.returnType = at.typeOfNode.get(ctx.typeTypeOrVoid());
        } else {
            // TODO 如果是类的构建函数，返回值应该是一个类吧？
        }

        // 函数查重，检查名称和参数（这个时候参数已经齐了）
        Scope scope = at.enclosingScopeOfNode(ctx);
        Function found = Scope.getFunction(scope, function.name, function.getParamTypes());
        if (found != null && found != function) {
            at.log("Function or method already Declared: " + ctx.getText(), ctx);
        }
    }
}
