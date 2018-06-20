package kelijun.com.notes.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Scope @Retention(RetentionPolicy.RUNTIME)
public @interface Activity {
}
