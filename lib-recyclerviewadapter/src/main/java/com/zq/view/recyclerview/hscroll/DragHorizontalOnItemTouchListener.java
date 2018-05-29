package com.zq.view.recyclerview.hscroll;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.zq.view.recyclerview.hscroll.controller.HorizontalScrollController;

/**
 * 根据手势判断滚动子view中的RecyclerView
 * Created by zhangqiang on 2017/10/20.
 */

public class DragHorizontalOnItemTouchListener implements RecyclerView.OnItemTouchListener, Runnable {

    private VelocityTracker velocityTracker;
    private float mLastMotionX, mLastMotionY;
    private boolean isDraggedHorizontal;
    private int mTouchSlop;
    private int minFlingVelocity, maxFlingVelocity;
    private RecyclerView recyclerView;
    private int scrollX;
    private OverScroller overScroller;
    private int mLastScrollX;
    private boolean shouldChildIntercept;
    private HorizontalScrollController horizontalScrollController;

    public DragHorizontalOnItemTouchListener(RecyclerView recyclerView, HorizontalScrollController horizontalScrollController) {

        Context context = recyclerView.getContext();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        minFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        maxFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.recyclerView = recyclerView;
        overScroller = new OverScroller(context);
        this.horizontalScrollController = horizontalScrollController;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        final int action = e.getAction();

        if (action != MotionEvent.ACTION_DOWN && !shouldChildIntercept) {
            return false;
        }
        if ((action == MotionEvent.ACTION_MOVE) && (isDraggedHorizontal)) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mLastMotionX = e.getX();
                mLastMotionY = e.getY();

                //判断是否要拦截事件
                View childView = rv.findChildViewUnder(mLastMotionX, mLastMotionY);
                if (childView != null) {

                    RecyclerView.ViewHolder viewHolder =  recyclerView.getChildViewHolder(childView);
                    shouldChildIntercept = horizontalScrollController.shouldSyncHorizontalScroll(viewHolder);
                }

                initOrResetVelocityTracker();
                velocityTracker.addMovement(e);

                isDraggedHorizontal = false;

                if (shouldChildIntercept){

                    if(!overScroller.isFinished()){
                        overScroller.abortAnimation();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = mLastMotionX - e.getX();
                float deltaY = mLastMotionY - e.getY();

                if (!isDraggedHorizontal && Math.abs(deltaY) < mTouchSlop * 2 && Math.abs(deltaX) > mTouchSlop) {
                    isDraggedHorizontal = true;

                    if (deltaX > 0) {
                        mLastMotionX -= mTouchSlop;
                    } else {
                        mLastMotionX += mTouchSlop;
                    }

                    initVelocityTrackerIfNotExists();
                    velocityTracker.addMovement(e);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDraggedHorizontal = false;
                recycleVelocityTracker();
                shouldChildIntercept = false;
                break;
        }

        return shouldChildIntercept && isDraggedHorizontal;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent e) {

        initVelocityTrackerIfNotExists();

        switch (e.getAction()) {

            case MotionEvent.ACTION_MOVE:

                float deltaX = mLastMotionX - e.getX();
                float deltaY = mLastMotionY - e.getY();
                if (!isDraggedHorizontal && Math.abs(deltaY) < mTouchSlop && Math.abs(deltaX) > mTouchSlop) {
                    isDraggedHorizontal = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }

                if (isDraggedHorizontal) {

                    scrollX += deltaX;

                    childRecyclerViewScrollBy((int) deltaX);
                    mLastMotionX = e.getX();
                    mLastMotionY = e.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (isDraggedHorizontal) {

                    velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity);
                    float xVelocity = velocityTracker.getXVelocity();
                    if (Math.abs(xVelocity) > minFlingVelocity) {
                        fling(-(int) (xVelocity));
                    }
                    recycleVelocityTracker();
                    isDraggedHorizontal = false;
                }

                break;
        }
        if (velocityTracker != null) {
            velocityTracker.addMovement(e);
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private void childRecyclerViewScrollBy(int deltaX) {

        if (shouldChildIntercept && horizontalScrollController != null) {
            horizontalScrollController.syncHorizontalScroll(recyclerView,deltaX, scrollX);
        }
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private void initOrResetVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public void run() {

        if (overScroller.computeScrollOffset()) {

            scrollX = overScroller.getCurrX();

            int deltaX = scrollX - mLastScrollX;
            mLastScrollX = scrollX;
            childRecyclerViewScrollBy(deltaX);
            ViewCompat.postOnAnimation(recyclerView, this);
        }
    }


    private void fling(int xVelocity) {

        mLastScrollX = scrollX;
        overScroller.fling(scrollX, 0, xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        ViewCompat.postOnAnimation(recyclerView, this);
    }

}
