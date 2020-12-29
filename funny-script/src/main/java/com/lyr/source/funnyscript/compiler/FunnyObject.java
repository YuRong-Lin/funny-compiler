package com.lyr.source.funnyscript.compiler;

import java.util.HashMap;
import java.util.Map;

/**
 * FunnyScript的对象
 *
 * @Author LinYuRong
 * @Date 2020/12/29 16:22
 * @Version 1.0
 */
public class FunnyObject {
    // 成员变量
    protected Map<Variable, Object> fields = new HashMap<>();

    public Object getValue(Variable variable){
        Object rtn = fields.get(variable);
        //TODO 父类的属性如何返回？还是说都在这里了？

        //替换成自己的NullObject
        if (rtn == null){
            rtn = NullObject.instance();
        }
        return rtn;
    }

    public void setValue(Variable variable, Object value){
        fields.put(variable, value);
    }
}
