package com.lyr.source.frontendalgorithm;

/**
 * 状态迁移
 *
 * @Author LinYuRong
 * @Date 2021/1/5 15:13
 * @Version 1.0
 */
public abstract class Transition {
    // 对于重复的情况，最多可以重复几次。
    // 这是把GrammarNode中的maxTimes属性转义到这里来了。
    // 对于 ？maxTimes = 1，对于+和*，maxTimes=-1
    protected int maxTimes = 1;

    /**
     * 是否满足迁移条件
     *
     * @param obj
     * @return
     */
    public abstract boolean match(Object obj);

    /**
     * 是否是一个Epsilon转换
     *
     * @return
     */
    public abstract boolean isEpsilon();

    public int getMaxTimes() {
        return maxTimes;
    }
}
