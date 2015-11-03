#自动类属性映射工具比较

## beanUtils

> beanUtils只能支持简单的java基本类型、对嵌套类及两个model类型不同的属性都无能为力。
> 并且性能也不是很好，因为它的实现是基于java 反射来的


## Orika

> 使用code generation的方式来实现bean的创建与赋值操作，开发人员只需要定义好映射的关系，
> 其他事情Orika会帮你完成


## 相关资料

http://blog.sokolenko.me/2013/05/dozer-vs-orika-vs-manual.html

http://www.javaworld.com/article/2075801/java-se/reflection-vs--code-generation.html




