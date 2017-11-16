package com.zq.rotateanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.zq.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 2017/10/26.
 */

public class RotateViewActivity extends AppCompatActivity {


    @BindView(R.id.back_view)
    FrameLayout backView;
    @BindView(R.id.front_view)
    FrameLayout frontView;

    private boolean frontVisible = true;

    int duration = 2000;
    int halfDuration = duration/2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_view);
        ButterKnife.bind(this);


    }


    @OnClick({R.id.back_view, R.id.front_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_view:

                change();
                break;
            case R.id.front_view:

                change();
                break;
        }
    }

    private void change(){

        frontView.setPivotX(frontView.getWidth()/2);
        frontView.setCameraDistance(26000);
        backView.setPivotX(backView.getWidth()/2);
        backView.setCameraDistance(26000);

        if(frontVisible){

            AnimatorSet rightOutAnimator = new AnimatorSet();

            ObjectAnimator rightOutAnimatorAlpha = ObjectAnimator.ofFloat(frontView, "alpha", 1, 0);
            rightOutAnimatorAlpha.setStartDelay(halfDuration);
            rightOutAnimatorAlpha.setDuration(0);
            ObjectAnimator rightOutAnimatorRotate = ObjectAnimator.ofFloat(frontView, "rotationY", 0, 180);
            rightOutAnimatorRotate.setDuration(duration);
            rightOutAnimator.playTogether(rightOutAnimatorRotate, rightOutAnimatorAlpha );

            AnimatorSet leftInAnimator = new AnimatorSet();
            ObjectAnimator leftInAnimatorAlpha = ObjectAnimator.ofFloat(backView, "alpha", 0, 1);
            leftInAnimatorAlpha.setStartDelay(halfDuration);
            leftInAnimatorAlpha.setDuration(0);
            ObjectAnimator leftInAnimatorRotate = ObjectAnimator.ofFloat(backView, "rotationY", -180, 0);
            leftInAnimatorRotate.setDuration(duration);
            leftInAnimator.playTogether(leftInAnimatorAlpha,
                    leftInAnimatorRotate);

            rightOutAnimator.start();
            leftInAnimator.start();
        }else{

            AnimatorSet rightOutAnimator = new AnimatorSet();

            ObjectAnimator rightOutAnimatorAlpha = ObjectAnimator.ofFloat(backView, "alpha", 1, 0);
            rightOutAnimatorAlpha.setStartDelay(halfDuration);
            rightOutAnimatorAlpha.setDuration(0);
            ObjectAnimator rightOutAnimatorRotate = ObjectAnimator.ofFloat(backView, "rotationY", 0, 180);
            rightOutAnimatorRotate.setDuration(duration);
            rightOutAnimator.playTogether(rightOutAnimatorRotate, rightOutAnimatorAlpha );

            AnimatorSet leftInAnimator = new AnimatorSet();
            ObjectAnimator leftInAnimatorAlpha = ObjectAnimator.ofFloat(frontView, "alpha", 0, 1);
            leftInAnimatorAlpha.setStartDelay(halfDuration);
            leftInAnimatorAlpha.setDuration(0);
            ObjectAnimator leftInAnimatorRotate = ObjectAnimator.ofFloat(frontView, "rotationY", -180, 0);
            leftInAnimatorRotate.setDuration(duration);
            leftInAnimator.playTogether(leftInAnimatorAlpha,
                    leftInAnimatorRotate);

            rightOutAnimator.start();
            leftInAnimator.start();
        }
        frontVisible = !frontVisible;
    }
}
