package com.zq.func.shadow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.zq.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 2018/4/19.
 */
public class ShadowTestActivity extends AppCompatActivity {

    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow_test);
        ButterKnife.bind(this);

        button2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {



                return false;
            }
        });

        Log.i("Test","================" + button2.getViewTreeObserver().equals(button3.getViewTreeObserver()));
    }
}
