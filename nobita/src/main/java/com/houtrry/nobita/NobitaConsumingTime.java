package com.houtrry.nobita;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: houtrry
 * @date: 2020/6/29 20:02
 * @version: $
 * @description:
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface NobitaConsumingTime {

}
