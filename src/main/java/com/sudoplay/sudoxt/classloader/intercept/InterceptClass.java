package com.sudoplay.sudoxt.classloader.intercept;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by codetaylor on 3/7/2017.
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptClass {
  //
}
