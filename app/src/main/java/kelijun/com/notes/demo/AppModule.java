package kelijun.com.notes.demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ${kelijun} on 2018/6/20.
 */
@Module
public class AppModule {
    private AppCompatActivity appCompatActivity;

    public AppModule(AppCompatActivity mContext) {
        this.appCompatActivity = mContext;
    }

    @Provides
    FragmentManager providesFragmentManager() {
        return appCompatActivity.getSupportFragmentManager();
    }

    @Provides
    List<String> providesTitles() {
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            titles.add("张三:" + i);
        }
        return titles;
    }

    @Provides
    List<Fragment> providesFragmentList(List<String> titles) {
        List<Fragment> fragments = new ArrayList<>();
        for (String title : titles
                ) {
            fragments.add(BaseFragment.getInstance(title));
        }
        return fragments;
    }
}
