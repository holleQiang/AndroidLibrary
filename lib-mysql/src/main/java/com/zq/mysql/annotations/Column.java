package com.zq.mysql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Types;

/**
 * Created by zhangqiang on 17-6-8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String name() default "";

    /**
     * {@link Type}
     * @return
     */
    Type type();

    boolean isPrimaryKey() default false;

    boolean isAutoIncrement() default false;

    int length() default 0;
}
