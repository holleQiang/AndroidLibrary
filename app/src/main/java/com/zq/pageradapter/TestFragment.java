package com.zq.pageradapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zq.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhangqiang on 2017/10/30.
 */

public class TestFragment extends Fragment {

    @BindView(R.id.textView)
    TextView textView;
    Unbinder unbinder;
    private String flag;

    public static TestFragment newInstance(String flag) {

        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flag", flag);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = getArguments().getString("flag");
        Log.i("Test", "=========onCreate=============" + flag);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Test", "=========onCreateView=============" + flag);
        View view = inflater.inflate(R.layout.page_empty, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView.setText(flag);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.i("Test", "=========onDestroyView=============" + flag);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Test", "=========onDestroy=============" + flag);
    }
}
