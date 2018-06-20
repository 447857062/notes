package kelijun.com.notes.injector;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextLifeCycle {
    String value() default "App";
}
