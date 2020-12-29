package com.lyr.source.funnyscript.compiler;

/**
 * @Author LinYuRong
 * @Date 2020/12/29 16:25
 * @Version 1.0
 */
public class ReturnObject {
    // 真正的返回值
    Object returnValue;

    public ReturnObject(Object value) {
        this.returnValue = value;
    }

    // 在打印时输出ReturnObject。
    @Override
    public String toString() {
        return "ReturnObject";
    }
}
