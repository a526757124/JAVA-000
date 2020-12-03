package com.billy.starter;

import java.lang.annotation.*;

/**
 * Created by duanxufei on 2018/12/12.
 *
 * 自定义注解，作为TokenCheck切入点
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenCheck {
    String value() default "";
}

