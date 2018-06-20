package kelijun.com.notes.injector.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import kelijun.com.notes.injector.ContextLifeCycle;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activiyt) {
        this.activity = activiyt;
    }

    @Provides
    @kelijun.com.notes.injector.Activity
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @kelijun.com.notes.injector.Activity
    @ContextLifeCycle
    Context provideContext() {
        return activity;
    }
}
