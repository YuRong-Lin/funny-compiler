# funny-compiler
编译原理实战

本实战内容基于极客时间上宫老师的课程[《编译原理之美》](https://github.com/RichardGong/PlayWithCompiler)的实战项目，修复了原项目的一些问题。

## 目录
### [lab](lab)
手工实现的简易词法解析器、语法解析器、脚本解释器、REPL，能实现整型变量声明、赋值、表达式计算基本功能。


### [funny-script](funny-script)
java版本的脚本语言的一个实现。借助antlr工具生成词法和语法解析器，用java实现了语义分析、解释执行器。

* 支持的类型：
    + 基本类型：boolean、char、short、int、long、float、double、string基本类型；
    + class
    + function （函数作为一等公民）

* 支持的特性
    + 面向对象编程：封装、继承、多态
    + 闭包
    + 函数作为一等公民，支持函数声明、变量赋值、函数传参等
