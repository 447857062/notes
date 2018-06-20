package kelijun.com.notes.demo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import kelijun.com.notes.R;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Inject MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Dagger2 使用
        DaggerAppComponent.builder().appModule(new AppModule(this)).build().inject(this);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
