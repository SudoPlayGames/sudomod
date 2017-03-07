package com.sudoplay.sudoext.classloader.intercept;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Created by codetaylor on 3/7/2017.
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectStaticField {
  String value() default "";
}
