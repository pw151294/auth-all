package com.iflytek.auth.manager.annotations;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */

import java.lang.annotation.*;

@Documented  //有关java doc的注解
@Retention(RetentionPolicy.RUNTIME)  //保留时间，这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.
@Target(ElementType.METHOD) //针对方法
public @interface GrantLogger {

    //被授权对象的类型
    int targetType() default -1;

    //被授权内容的类型
    int grantType() default -1;

}
