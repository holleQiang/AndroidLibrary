package com.zq.redpoint;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zq.R;
import com.zq.utils.RedDotUtil;
import com.zq.utils.ViewUtil;
import com.zq.widget.smartnumber.SmartNumberView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 2017/10/17.
 */

public class RedPointActivity extends AppCompatActivity {


    int count = 0;
    @BindView(R.id.target_view)
    ImageView targetView;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.reduce)
    Button reduce;
    @BindView(R.id.clear)
    Button clear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_point);
        ButterKnife.bind(this);
    }


    private RedDotUtil.NumberViewAdapter<SmartNumberView> adapter = new RedDotUtil.NumberViewAdapter<SmartNumberView>() {

        @Override
        public SmartNumberView getView() {

            SmartNumberView smartNumberView = new SmartNumberView(RedPointActivity.this);
            float density = getResources().getDisplayMetrics().density;

            int horizontalPadding = (int) (density * 6 + 0.5f);
            int verticalPadding = (int) (density * 2 + 0.5f);
            smartNumberView.setTextSize(ViewUtil.dp2px(RedPointActivity.this, 12));
            smartNumberView.setTextColor(Color.WHITE);
            smartNumberView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
            smartNumberView.setBackgroundResource(R.drawable.bg_red_point);
            smartNumberView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                    int viewHeight = v.getHeight();
                    if (viewHeight > 0) {

                        float corner = viewHeight / 2f;
                        GradientDrawable drawable = (GradientDrawable) v.getBackground();
                        drawable.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
                    }
                }
            });
            return smartNumberView;
        }

        @Override
        public void onNumberChange(SmartNumberView view, int number) {
            view.setNumber(number);
        }
    };

    @OnClick({R.id.add, R.id.reduce, R.id.clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add:

                count++;
                RedDotUtil.attachToView(targetView, count, adapter);
                break;
            case R.id.reduce:

                count--;
                RedDotUtil.attachToView(targetView, count, adapter);
                break;
            case R.id.clear:

                RedDotUtil.clear(targetView);
                break;
        }
    }
}
