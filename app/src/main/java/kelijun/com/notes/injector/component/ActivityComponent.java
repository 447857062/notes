package kelijun.com.notes.injector.component;

import android.content.Context;

import dagger.Component;
import kelijun.com.notes.demo.MainActivity;
import kelijun.com.notes.injector.Activity;
import kelijun.com.notes.injector.ContextLifeCycle;
import kelijun.com.notes.injector.module.ActivityModule;
import kelijun.com.notes.ui.NoteActivity;
import kelijun.com.notes.ui.SettingActivity;
import kelijun.com.sqlite.FinalDb;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Activity
@Component(dependencies = AppComponent.class,modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(NoteActivity activity);
    void inject(SettingActivity activity);
    android.app.Activity activity();
    FinalDb finalDb();
    @ContextLifeCycle("Activity")
    Context activityContext();
    @ContextLifeCycle("App") Context appContext();
}
