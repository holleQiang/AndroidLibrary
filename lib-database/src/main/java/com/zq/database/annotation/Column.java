package com.zq.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhangqiang on 17-6-8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String name() default "";

    Type type();

    boolean primaryKey() default false;

    boolean autoIncrement() default false;

    int length() default 0;
}
