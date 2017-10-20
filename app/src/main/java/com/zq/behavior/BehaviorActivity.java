package com.zq.behavior;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

import com.zq.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangqiang on 2017/10/17.
 */

public class BehaviorActivity extends AppCompatActivity {

    @InjectView(R.id.left_nested_scroll_view)
    NestedScrollView leftNestedScrollView;
    @InjectView(R.id.right_nested_scroll_view)
    NestedScrollView rightNestedScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        ButterKnife.inject(this);
        leftNestedScrollView.setTag("left");
        rightNestedScrollView.setTag("right");
    }
}
