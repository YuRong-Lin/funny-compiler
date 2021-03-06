package com.lyr.source.funnyscript.compiler;

/**
 * 栈帧
 *
 * @Author LinYuRong
 * @Date 2020/12/29 16:26
 * @Version 1.0
 */
public class StackFrame {

    // 该frame所对应的scope
    protected Scope scope;

    /**
     * 放parent scope所对应的frame的指针，就叫parentFrame吧，便于提高查找效率。
     *
     * 规则：
     * 1）如果是同一级函数调用，跟上一级的parentFrame相同；
     * 2）如果是下一级的函数调用或For、If等block，parentFrame是自己；
     * 3）如果是一个闭包（如何判断？），那么要带一个存放在堆里的环境（因为外部函数失效后，闭包仍要能正常访问外部函数的变量）。
     */
    protected StackFrame parentFrame;

    // 实际存放变量的地方（用Map存储）
    protected FunnyObject object;

    public StackFrame(BlockScope scope) {
        this.scope = scope;
        this.object = new FunnyObject();
    }

    public StackFrame(ClassObject object) {
        this.scope = object.type;
        this.object = object;
    }

    /**
     * 为函数调用创建一个StackFrame
     *
     * @param object
     */
    public StackFrame(FunctionObject object) {
        this.scope = object.function;
        this.object = object;
    }

    /**
     * 本栈桢里有没有包含某个变量的数据
     *
     * @param variable
     * @return
     */
    protected boolean contains(Variable variable) {
        if (object != null && object.fields != null) {
            return object.fields.containsKey(variable);
        }
        return false;
    }

    @Override
    public String toString() {
        String rtn = "" + scope;
        if (parentFrame != null) {
            rtn += " -> " + parentFrame;
        }
        return rtn;
    }
}
