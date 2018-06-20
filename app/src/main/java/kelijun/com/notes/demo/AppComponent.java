package kelijun.com.notes.demo;

import dagger.Component;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
}
