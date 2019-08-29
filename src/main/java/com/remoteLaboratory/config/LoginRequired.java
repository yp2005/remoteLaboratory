package com.remoteLaboratory.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在需要登录验证的Controller的类或者方法上使用此注解
 *
 * @Author: yupeng
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
    String adminRequired() default "0";

    String teacherRequired() default "0";

    String studentRequired() default "0";
}