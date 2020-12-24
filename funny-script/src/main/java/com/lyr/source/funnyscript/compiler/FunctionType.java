package com.lyr.source.funnyscript.compiler;

import java.util.List;

/**
 * 函数类型
 *
 * @Author LinYuRong
 * @Date 2020/12/24 17:31
 * @Version 1.0
 */
public interface FunctionType extends Type {

    Type getReturnType();

    List<Type> getParamTypes();

    boolean matchParameterTypes(List<Type> paramTypes);
}
