package com.zq.viewflipper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.zq.R;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 2017/11/2.
 */

public class ViewFlipperActivity extends AppCompatActivity {

    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    GestureDetector gestureDetector;
    @BindView(R.id.last)
    Button last;
    @BindView(R.id.next)
    Button next;

    private Stack<View> viewStack = new Stack<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper);
        ButterKnife.bind(this);

        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        AnimatorSet outAnimatorSet = new AnimatorSet();
        outAnimatorSet.setDuration(1000);
        outAnimatorSet.playTogether();

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @OnClick({R.id.last, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.last:

                viewFlipper.showPrevious();
                break;
            case R.id.next:

                viewFlipper.showNext();

                makeOutAnimator();
                break;
        }
    }

    private void makeOutAnimator(){

        View view = viewFlipper.getCurrentView();
        view.setCameraDistance(30000);
        view.setPivotY(view.getHeight()/2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(view,"rotationX",0,180);
        animatorSet.playTogether(outAnimator);

        animatorSet.start();
    }

}
