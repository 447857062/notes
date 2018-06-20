package kelijun.com.notes.injector.component;

import dagger.Component;
import kelijun.com.notes.injector.Fragment;
import kelijun.com.notes.injector.module.FragmentModule;
import kelijun.com.notes.ui.fragments.SettingFragment;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Fragment
@Component(dependencies = {ActivityComponent.class}, modules = {FragmentModule.class})
public interface FragmentComponent {
    void inject(SettingFragment fragment);
}
