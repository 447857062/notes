package kelijun.com.notes.injector.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import kelijun.com.notes.APP;
import kelijun.com.notes.injector.ContextLifeCycle;
import kelijun.com.notes.injector.module.AppModule;
import kelijun.com.sqlite.FinalDb;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    APP app();
    @ContextLifeCycle("App")
    Context context();
    FinalDb finalDb();
    FinalDb.DaoConfig daoConfig();
}
