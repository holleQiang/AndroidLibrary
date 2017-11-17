package com.zq.view.recyclerview.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.zq.view.recyclerview.adapter.R;

/**
 * 侧滑布局
 * Created by zhangqiang on 2017/11/16.
 */

public class HorizontalSlidLayout extends ViewGroup {

    private int mTouchSlop;
    private int leftScrollRange, rightScrollRange;
    private int leftInitX, rightInitX;
    private OverScroller mOverScroller;
    private int maximumFlingVelocity, minimumFlingVelocity;
    private VelocityTracker mVelocityTracker;
    private static final int LOCATION_INVALID = -1;

    public HorizontalSlidLayout(Context context) {
        super(context);
        init(context);
    }

    public HorizontalSlidLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalSlidLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    void init(Context context) {

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        maximumFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        minimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity() * 3;
        mOverScroller = new OverScroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        leftScrollRange = 0;
        rightScrollRange = r;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            int mHeight = childView.getMeasuredHeight();
            int mWidth = childView.getMeasuredWidth();

            int top = t + getPaddingTop() + layoutParams.topMargin;
            int bottom = top + mHeight;
            if (layoutParams.location == LayoutParams.LOCATION_LEFT) {

                int left = l - (mWidth + layoutParams.rightMargin);
                int right = left + mWidth;
                childView.layout(left, top, right, bottom);
                leftScrollRange = left - layoutParams.leftMargin;
                leftInitX = Math.max(leftScrollRange, l - r);
            } else if (layoutParams.location == LayoutParams.LOCATION_CENTER) {

                int left = l + getPaddingLeft() + layoutParams.leftMargin;
                int right = left + mWidth;
                childView.layout(left, top, right, bottom);
            } else if (layoutParams.location == LayoutParams.LOCATION_RIGHT) {

                int left = r + layoutParams.leftMargin;
                int right = left + mWidth;
                childView.layout(left, top, right, bottom);
                rightScrollRange = right + layoutParams.rightMargin;
                rightInitX = Math.min(rightScrollRange, r - l);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int childCount = getChildCount();
        int maxChildHeight = Integer.MIN_VALUE;
        int centerChildWidth = 0;
        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            int mHeight = childView.getMeasuredHeight();
            int mWidth = childView.getMeasuredWidth();
            maxChildHeight = Math.max(maxChildHeight, mHeight + layoutParams.topMargin + layoutParams.bottomMargin);
            if (layoutParams.location == LayoutParams.LOCATION_CENTER) {
                centerChildWidth = mWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            }
        }

        int realHeight = maxChildHeight + getPaddingTop() + getPaddingBottom();
        int realWidth = centerChildWidth + getPaddingLeft() + getPaddingBottom();
        setMeasuredDimension(realWidth, realHeight);

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            final int childHeightMeasureSpec;
            if (layoutParams.height == LayoutParams.MATCH_PARENT) {

                final int height = Math.max(0, getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - layoutParams.topMargin - layoutParams.bottomMargin);
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            } else {

                childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin
                        , layoutParams.height);
            }
            final int childWidthMeasureSpec;
            if (layoutParams.location == LayoutParams.LOCATION_CENTER) {

                if (layoutParams.height == LayoutParams.MATCH_PARENT) {

                    final int width = Math.max(0, getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - layoutParams.leftMargin - layoutParams.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                } else {

                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin
                            , layoutParams.width);
                }
            } else {

                childWidthMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        0
                        , layoutParams.width);
            }
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private float mLastX, mLastY;
    private boolean isDraggedHorizontal;
    private int startX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            clear();
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        if (action != MotionEvent.ACTION_DOWN && isDraggedHorizontal) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                startX = getScrollX();
                mLastX = ev.getX();
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float cx = ev.getX();
                float cy = ev.getY();
                float deltaX = mLastX - cx;
                float deltaXAbs = Math.abs(deltaX);
                float deltaYAbs = Math.abs(mLastY - cy);
                if (!isDraggedHorizontal && deltaXAbs > deltaYAbs && deltaXAbs > mTouchSlop) {
                    isDraggedHorizontal = true;
                    if (deltaX > 0) {
                        ev.offsetLocation(mTouchSlop, 0);
                    } else {
                        ev.offsetLocation(-mTouchSlop, 0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                onRelease();
                break;
        }
        return isDraggedHorizontal;
    }

    private void clear() {

        mLastX = 0;
        mLastY = 0;

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                float cx = event.getX();
                float cy = event.getY();
                float deltaX = mLastX - cx;
                float deltaXAbs = Math.abs(deltaX);
                float deltaYAbs = Math.abs(mLastY - cy);
                if (!isDraggedHorizontal && deltaXAbs > deltaYAbs && deltaXAbs > mTouchSlop) {
                    isDraggedHorizontal = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (isDraggedHorizontal) {

                    mLastX = cx;
                    final int scrollX = getScrollX();
                    final int nextScrollX = (int) (scrollX + deltaX);

                    scrollTo(getFixedScrollX(nextScrollX, startX), 0);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                onRelease();
                break;
        }
        getParent().requestDisallowInterceptTouchEvent(isDraggedHorizontal);
        return true;
    }


    private void onRelease(){

        if (isDraggedHorizontal && !isOutOfRange(getScrollX(), startX)) {
            isDraggedHorizontal = false;

            int xVelocity = 0;
            if (mVelocityTracker != null) {
                mVelocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
                xVelocity = (int) mVelocityTracker.getXVelocity();
            }

            if (Math.abs(xVelocity) > minimumFlingVelocity) {

                if (getTargetLocation() != LOCATION_INVALID) {
                    fling(-xVelocity);
                }
            } else {

                if (needScrollToPosition(startX, getScrollX())) {
                    smoothScrollToTargetLocation();
                }

//                        smoothScrollToTargetLocation();
            }
        }
        clear();
    }

    private boolean needScrollToPosition(int startX, int currentX) {

        Log.i("Test", "======isInCenterRange======" + isInCenterRange(startX) + startX);

        if (isInLeftExtraRange(startX) && isInLeftExtraRange(currentX) ||
                isInRightExtraRange(startX) && isInRightExtraRange(currentX)) {
            //如果位置没有发生变化，不需要滚动到指定位置
            return false;
        }

        if (isInLeftExtraRange(startX) && !isInLeftExtraRange(currentX)  ||
                isInRightExtraRange(startX) && !isInRightExtraRange(currentX) ) {
            //如果位置没有发生变化，不需要滚动到指定位置
            return false;
        }

        if (!isInLeftExtraRange(startX) && isInLeftExtraRange(currentX)  ||
                !isInRightExtraRange(startX) && isInRightExtraRange(currentX) ) {
            //如果位置没有发生变化，不需要滚动到指定位置
            return false;
        }

        return true;
    }

    private boolean isInLeftExtraRange(int scrollX) {

        return scrollX >= leftScrollRange && scrollX <= leftInitX;
    }

    private boolean isInRightExtraRange(int scrollX) {

        return scrollX >= rightInitX && scrollX <= rightScrollRange;
    }

    private boolean isInCenterRange(int scrollX) {

        return scrollX == 0;
    }


    public void reset() {

        if (!mOverScroller.isFinished()) {
            mOverScroller.abortAnimation();
        }
        if (getScrollX() != 0 || getScrollY() != 0) {
            scrollTo(0, 0);
        }
    }

    public static class LayoutParams extends MarginLayoutParams {

        public static final int LOCATION_LEFT = 0;

        public static final int LOCATION_CENTER = 1;

        public static final int LOCATION_RIGHT = 2;

        public int location;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray t = c.obtainStyledAttributes(attrs, R.styleable.HorizontalSlidLayout_Layout);
            location = t.getInt(R.styleable.HorizontalSlidLayout_Layout_location, LOCATION_CENTER);
            t.recycle();
        }
    }

    private boolean isOutOfRange(int scrollX, int startX) {

        return scrollX < leftScrollRange ||
                scrollX + getWidth() > rightScrollRange ||
                startX < 0 && scrollX > 0 || startX > 0 && scrollX < 0;
    }

    public void smoothScrollTo(int targetX) {

        final int scrollX = getScrollX();
        final int deltaX = targetX - getScrollX();
        mOverScroller.startScroll(scrollX, getScrollY(), deltaX, 0, Math.min(300, Math.abs(deltaX)));
        postInvalidate();
    }

    public void smoothScrollToLeft() {

        smoothScrollTo(leftInitX);
    }

    public void smoothScrollToRight() {

        smoothScrollTo(rightInitX);
    }

    public void smoothScrollToCenter() {

        smoothScrollTo(0);
    }

    public void fling(int xVelocity) {

        final int startX = getScrollX();
        mOverScroller.fling(startX, 0, xVelocity, 0, leftScrollRange, rightScrollRange, 0, 0);
        postInvalidate();
    }

    private int getFixedScrollX(int scrollX, int startX) {

        if (!isOutOfRange(scrollX, startX)) {
            return scrollX;
        }
        if (scrollX - startX > 0) {
            if (startX < 0) {
                return 0;
            }
            return rightScrollRange - getWidth();
        } else {
            if (startX > 0) {
                return 0;
            }
            return leftScrollRange;
        }
    }

    /**
     * 获取需要滚动的位置
     *
     * @return
     */
    private int getTargetLocation() {

        int scrollX = getScrollX();
        if (scrollX == leftInitX || scrollX == 0 || scrollX == rightInitX) {
            return LOCATION_INVALID;
        }
        if (scrollX >= leftScrollRange && scrollX < leftInitX / 2) {
            return LayoutParams.LOCATION_LEFT;
        } else if (scrollX > rightInitX / 2 && scrollX <= rightScrollRange) {
            return LayoutParams.LOCATION_RIGHT;
        } else if (scrollX >= leftInitX / 2 && scrollX <= rightInitX / 2) {
            return LayoutParams.LOCATION_CENTER;
        }
        return LOCATION_INVALID;
    }


    /**
     * 根据当前位置判断滚动到左中右定位
     */
    private void smoothScrollToTargetLocation() {

        if (isDraggedHorizontal) {

            //如果正在拖动 不滚动
            return;
        }

        int location = getTargetLocation();
        if (location == LayoutParams.LOCATION_LEFT) {
            smoothScrollToLeft();
        } else if (location == LayoutParams.LOCATION_RIGHT) {
            smoothScrollToRight();
        } else if (location == LayoutParams.LOCATION_CENTER) {
            smoothScrollToCenter();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset()) {
            int scrollX = mOverScroller.getCurrX();
            final int startX = mOverScroller.getStartX();
            int fixedScrollX = getFixedScrollX(scrollX, startX);
            scrollTo(fixedScrollX, mOverScroller.getCurrY());

            if (fixedScrollX != scrollX) {
                mOverScroller.abortAnimation();
                if (needScrollToPosition(startX, fixedScrollX)) {
                    smoothScrollToTargetLocation();
                }
                return;
            }
            postInvalidate();
        } else {

            //fling 停止后再滚动到指定view
//            Log.i("Test","==========" + mOverScroller.getCurrX() + "============" + getScrollX());
            if (needScrollToPosition(getScrollX(), getScrollX())) {
                smoothScrollToTargetLocation();
            }
        }
    }
}
