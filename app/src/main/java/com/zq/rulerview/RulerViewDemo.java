package com.zq.rulerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zq.R;
import com.zq.widget.rulerview.RulerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 2017/12/29.
 */

public class RulerViewDemo extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.m_ruler_view)
    RulerView mRulerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler_view);
        ButterKnife.bind(this);
        mRulerView.setOnIndicateValueChangeListener(new RulerView.OnIndicateValueChangeListener() {
            @Override
            public void onIndicateValueChange(int indicateValue) {
                textView.setText(indicateValue +"");
            }
        });
        mRulerView.post(new Runnable() {
            @Override
            public void run() {
                mRulerView.scrollToValue(100000* 10000);
            }
        });
    }
}
