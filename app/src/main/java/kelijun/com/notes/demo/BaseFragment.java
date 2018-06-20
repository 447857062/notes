package kelijun.com.notes.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ${kelijun} on 2018/6/20.
 */

public class BaseFragment extends Fragment {
    public static BaseFragment getInstance(String name) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText(getArguments().getString("name"));
        tv.setTextSize(36);
        return tv;
    }
}
