package com.zq.widget.rulerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * 刻度尺view
 * Created by zhangqiang on 2017/12/29.
 */

public class RulerView extends View {

    private Paint mPaint = new Paint();
    private int minValue, maxValue;
    private int bottomLineColor;
    private int bottomLineWidth;
    private int dividerWidth;
    private int dividerColor;
    private int dividerHeight;
    private int dividerValue;
    private int dividerLength;
    private int groupDivideHeight;
    private int groupValue;
    private int groupTextColor;
    private int groupTextSize;
    private int textSpacing;
    private float offset;
    private boolean isDragHorizontal;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mTouchSlop;
    private int indicatorLineColor;
    private int indicatorLineWidth;
    private int indicatorLineHeight;
    private int indicateValue;
    private int minIndicateValue = 1000;
    private SmoothScrollHelper smoothScrollHelper;
    private VelocityTracker mVelocityTracker;
    private float maxVelocity, minVelocity;
    private OnIndicateValueChangeListener onIndicateValueChangeListener;


    private static final float MILLISECONDS_PER_INCH = 25f;
    private float MILLISECONDS_PER_PX;

    public RulerView(Context context) {
        super(context);
        init(context, null);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        MILLISECONDS_PER_PX = calculateSpeedPerPixel(context.getResources().getDisplayMetrics());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        if (attrs != null) {

            TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.RulerView, R.attr.RulerViewStyle, 0);
            minValue = t.getInt(R.styleable.RulerView_minValue, 0);
            maxValue = t.getInt(R.styleable.RulerView_maxValue, 10000);
            bottomLineColor = t.getColor(R.styleable.RulerView_bottomLineColor, Color.GRAY);
            bottomLineWidth = t.getDimensionPixelSize(R.styleable.RulerView_bottomLineWidth, dipToPx(1));
            dividerWidth = t.getDimensionPixelSize(R.styleable.RulerView_dividerWidth, dipToPx(1));
            dividerColor = t.getColor(R.styleable.RulerView_dividerColor, Color.GRAY);
            dividerHeight = t.getDimensionPixelSize(R.styleable.RulerView_dividerHeight, dipToPx(5));
            dividerValue = t.getInt(R.styleable.RulerView_dividerValue, 100);
            dividerLength = t.getDimensionPixelSize(R.styleable.RulerView_dividerLength, dipToPx(5));
            groupDivideHeight = t.getDimensionPixelSize(R.styleable.RulerView_groupDivideHeight, dipToPx(10));
            groupValue = t.getInt(R.styleable.RulerView_groupValue, 1000);
            groupTextColor = t.getColor(R.styleable.RulerView_groupTextColor, Color.GRAY);
            groupTextSize = t.getDimensionPixelSize(R.styleable.RulerView_groupTextSize, dipToPx(12));
            textSpacing = t.getDimensionPixelSize(R.styleable.RulerView_textSpacing, dipToPx(5));
            indicatorLineColor = t.getColor(R.styleable.RulerView_indicatorLineColor, Color.RED);
            indicatorLineWidth = t.getDimensionPixelSize(R.styleable.RulerView_indicatorLineWidth, dipToPx(1));
            indicatorLineHeight = t.getDimensionPixelSize(R.styleable.RulerView_indicatorLineHeight, dipToPx(50));
            t.recycle();
        }
        indicateValue = minValue;
        smoothScrollHelper = new SmoothScrollHelper(context);
    }

    private int dipToPx(int i) {
        return (int) (getResources().getDisplayMetrics().density * i + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画底部线
        drawBottomLine(canvas);

        //画刻度
        drawDivider(canvas);

        //画指示线
        drawIndicatorLine(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mPaint.setTextSize(groupTextSize);
        float textHeight = mPaint.descent() - mPaint.ascent();
        int height = (int) (bottomLineWidth
                + Math.max(indicatorLineHeight, Math.max(groupDivideHeight + textSpacing + textHeight, dividerHeight)))
                + getPaddingTop() + getPaddingBottom();
        int width = getPaddingLeft() + getPaddingRight() + (maxValue - minValue) * dividerLength;
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidateWhenMinValueChange();
    }

    private void drawIndicatorLine(Canvas canvas) {

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(indicatorLineWidth);
        mPaint.setColor(indicatorLineColor);
        float startX = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2f + getPaddingLeft() - indicatorLineWidth / 2f;
        float startY = getHeight() - getPaddingBottom() - bottomLineWidth - indicatorLineHeight;
        float stopY = startY + indicatorLineHeight;
        canvas.drawLine(startX, startY, startX, stopY, mPaint);
    }

    private void drawDivider(Canvas canvas) {

        final int count = maxValue - minValue + 1;

        final int start = getStartIndex(count);

        float minCloseIndicatorDistance = Integer.MAX_VALUE;
        int minCloseIndex = 0;
        for (int i = start; i >= 0; i--) {
            boolean isDivider = i % dividerValue == 0;
            if (!isDivider) {
                continue;
            }
            int dividerIndex = i / dividerValue;
            float dividerOffset = dividerLength * dividerIndex;
            float locationX = offset + dividerOffset + getPaddingLeft();

            if (locationX < getPaddingLeft()) {
                break;
            }

            float indicatorX = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2f;
            float distance = Math.abs(indicatorX - locationX);
            if (minCloseIndicatorDistance > distance) {
                minCloseIndicatorDistance = distance;
                minCloseIndex = i;
            }

            if (i % groupValue == 0) {

                drawGroupDivider(canvas, locationX);
                drawGroupText(canvas, locationX, i);
            } else if (i % dividerValue == 0) {

                drawGeneralDivider(canvas, locationX);
            }
        }

        for (int i = start + 1; i < count; i++) {

            boolean isDivider = i % dividerValue == 0;
            if (!isDivider) {
                continue;
            }
            int dividerIndex = i / dividerValue;
            float dividerOffset = dividerLength * dividerIndex;
            float locationX = offset + dividerOffset + getPaddingLeft();

            if (locationX > getWidth() - getPaddingRight()) {
                break;
            }

            float indicatorX = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2f;
            float distance = Math.abs(indicatorX - locationX);
            if (minCloseIndicatorDistance > distance) {
                minCloseIndicatorDistance = distance;
                minCloseIndex = i;
            }

            if (i % groupValue == 0) {

                drawGroupDivider(canvas, locationX);
                drawGroupText(canvas, locationX, i);
            } else if (i % dividerValue == 0) {

                drawGeneralDivider(canvas, locationX);
            }
        }

        int minCloseValue = minCloseIndex + minValue;
        if (indicateValue != minCloseValue) {
            indicateValue = minCloseValue;
            if (onIndicateValueChangeListener != null) {
                onIndicateValueChangeListener.onIndicateValueChange(indicateValue);
            }
        }
    }

    private void drawGeneralDivider(Canvas canvas, float startX) {

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dividerWidth);
        mPaint.setColor(dividerColor);
        startX -= dividerWidth / 2;
        float startY = getHeight() - getPaddingBottom() - bottomLineWidth - dividerHeight;
        float endY = startY + dividerHeight;
        canvas.drawLine(startX, startY, startX, endY, mPaint);
    }

    private void drawGroupText(Canvas canvas, float startX, int i) {

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(groupTextColor);
        mPaint.setTextSize(groupTextSize);
        String text = String.valueOf(i);
        startX -= mPaint.measureText(text) / 2;
        float baseLine = getHeight() - getPaddingBottom() - bottomLineWidth - groupDivideHeight - textSpacing - mPaint.descent();
        canvas.drawText(text, startX, baseLine, mPaint);
    }

    private void drawGroupDivider(Canvas canvas, float startX) {

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dividerWidth);
        mPaint.setColor(dividerColor);
        startX -= dividerWidth / 2f;
        float startY = getHeight() - getPaddingBottom() - bottomLineWidth - groupDivideHeight;
        float endY = startY + groupDivideHeight;
        canvas.drawLine(startX, startY, startX, endY, mPaint);
    }

    private void drawBottomLine(Canvas canvas) {

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(bottomLineWidth);
        mPaint.setColor(bottomLineColor);
        float startX = getPaddingLeft();
        float startY = getHeight() - getPaddingBottom() - bottomLineWidth / 2f;
        float endX = getWidth() - getPaddingRight();
        canvas.drawLine(startX, startY, endX, startY, mPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        float currentX = event.getX();
        float currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                isDragHorizontal = false;
                mLastMotionX = currentX;
                mLastMotionY = currentY;
                if (smoothScrollHelper.isRunning()) {
                    smoothScrollHelper.stop();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = mLastMotionX - currentX;
                if (!isDragHorizontal) {

                    float deltaY = Math.abs(mLastMotionY - currentY);
                    float deltaXAbs = Math.abs(deltaX);
                    if (deltaXAbs > mTouchSlop && deltaY < mTouchSlop) {
                        isDragHorizontal = true;
                        if (deltaX > 0) {
                            deltaX -= mTouchSlop;
                        } else {
                            deltaX += mTouchSlop;
                        }
                    }
                }
                if (isDragHorizontal) {
                    float nextOffset = offset - deltaX;
                    if (isOutRange(nextOffset)) {
                        deltaX = deltaX / 3;
                    }
                    offset -= deltaX;
                    mLastMotionX = currentX;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragHorizontal = false;
                mVelocityTracker.computeCurrentVelocity(1000, maxVelocity);
                float xVelocity = mVelocityTracker.getXVelocity();
                float yVelocity = mVelocityTracker.getYVelocity();
                if (Math.abs(xVelocity) >= minVelocity) {

                    xVelocity = Math.max(-maxVelocity, Math.min(xVelocity, maxVelocity));
                    yVelocity = Math.max(-maxVelocity, Math.min(yVelocity, maxVelocity));
                    smoothScrollHelper.fling((int) xVelocity, (int) yVelocity);
                } else {
                    //滚动到最近的刻度
                    smoothScrollToCloseDivider();
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(event);
        }
        getParent().requestDisallowInterceptTouchEvent(isDragHorizontal);
        return true;
    }

    private void smoothScrollToCloseDivider() {

        smoothScrollHelper.smoothScrollToValue(findClosestDividerValue(offset, getIndicatorLocation()));
    }


    private float getLocationOfValue(int value) {

        int dividerIndex = value / dividerValue;
        float dividerOffset = dividerLength * dividerIndex;
        return dividerOffset + getPaddingLeft();
    }

    /**
     * 寻找指定位置最近的刻度的value
     *
     * @param offset         偏移量
     * @param targetLocation 指定位置
     * @return 寻找最近的刻度的value
     */
    private int findClosestDividerValue(float offset, float targetLocation) {

        float minCloseDistanceAbs = Integer.MAX_VALUE;
        int closestIndex = 0;
        final int count = maxValue - minValue + 1;
        final int start = getStartIndex(count);
        for (int i = start; i >= 0; i--) {

            if (i < minIndicateValue) {
                continue;
            }
            boolean isDivider = i % dividerValue == 0;
            if (!isDivider) {
                continue;
            }
            int dividerIndex = i / dividerValue;
            float dividerOffset = dividerLength * dividerIndex;
            float locationX = offset + dividerOffset + getPaddingLeft();

            if (locationX < getPaddingLeft()) {
                continue;
            }

            float distance = targetLocation - locationX;
            float distanceAbs = Math.abs(distance);
            if (minCloseDistanceAbs > distanceAbs) {
                minCloseDistanceAbs = distanceAbs;
                closestIndex = i;
            }
        }
        for (int i = start + 1; i < count; i++) {

            if (i < minIndicateValue) {
                continue;
            }
            boolean isDivider = i % dividerValue == 0;
            if (!isDivider) {
                continue;
            }
            int dividerIndex = i / dividerValue;
            float dividerOffset = dividerLength * dividerIndex;
            float locationX = offset + dividerOffset + getPaddingLeft();

            if (locationX > getWidth() - getPaddingRight()) {
                break;
            }

            float distance = targetLocation - locationX;
            float distanceAbs = Math.abs(distance);
            if (minCloseDistanceAbs > distanceAbs) {
                minCloseDistanceAbs = distanceAbs;
                closestIndex = i;
            }
        }
        return closestIndex + minValue;
    }

    /**
     * 获取循环开始的位置
     *
     * @param count
     * @return
     */
    private int getStartIndex(int count) {

        final int min = minIndicateValue > minValue ? minIndicateValue - minValue : 0;
        return findStartIndex(indicateValue - minValue,
                min,
                count);
    }

    class SmoothScrollHelper implements Runnable {

        private boolean isRunning;
        private OverScroller mScroller;
        private Scroller computeScroller;

        SmoothScrollHelper(Context context) {
            mScroller = new OverScroller(context, new DecelerateInterpolator());
            computeScroller = new Scroller(context);
            computeScroller.setFriction(0.1f);
        }

        @Override
        public void run() {

            if (mScroller.computeScrollOffset()) {

                offset = mScroller.getCurrX();
                invalidate();
                if (isRunning) {
                    post(this);
                }
            } else {
                isRunning = false;
            }
        }

        void smoothScrollToValue(int value) {

            if (value < minValue || value > maxValue) {
                return;
            }
            int location = (int) (-getLocationOfValue(value) + getIndicatorLocation());
            smoothScrollTo(location);
        }

        void smoothScrollTo(int location) {

            int startX = (int) offset;
            int dx = location - startX;
            int duration = calculateTimeForScrolling(dx);
            duration = Math.max(300, duration);
            mScroller.startScroll(startX, 0, dx, 0, duration);
            post(this);
            isRunning = true;
        }

        void fling(int xVelocity, int yVelocity) {

            computeScroller.fling(0, yVelocity, xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            int deltaOffset = computeScroller.getFinalX();
            int finalOffset = (int) (offset + deltaOffset);
            int closestDividerValue = findClosestDividerValue(finalOffset, getIndicatorLocation());
            smoothScrollToValue(closestDividerValue);
        }

        void stop() {

            isRunning = false;
            removeCallbacks(this);
        }

        boolean isRunning() {
            return isRunning;
        }
    }

    private float getIndicatorLocation() {
        return getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2f;
    }

    private int getTotalLength() {

        int dividerCount = (maxValue - minValue) / dividerValue;
        return dividerCount * dividerLength;
    }

    private boolean isOutRange(float offset) {

        return offset < getMinOffsetRange() || offset > getMaxOffsetRange();
    }

    private int getMinOffsetRange() {
        return (int) (-getTotalLength() + getIndicatorLocation());
    }

    private int getMaxOffsetRange() {
        return (int) getIndicatorLocation();
    }

    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
    }

    protected int calculateTimeForScrolling(int dx) {
        // In a case where dx is very small, rounding may return 0 although dx > 0.
        // To avoid that issue, ceil the result so that if dx > 0, we'll always return positive
        // time.
        return (int) Math.ceil(Math.abs(dx) * MILLISECONDS_PER_PX);
    }

    public void scrollToValue(int value) {

        if (value < minValue || value > maxValue) {
            return;
        }
        offset = -getLocationOfValue(value) + getIndicatorLocation();
        postInvalidate();
    }

    public void setOnIndicateValueChangeListener(OnIndicateValueChangeListener onIndicateValueChangeListener) {
        this.onIndicateValueChangeListener = onIndicateValueChangeListener;
    }

    public interface OnIndicateValueChangeListener {

        void onIndicateValueChange(int indicateValue);
    }

    /**
     * 寻找循环起点
     *
     * @param index 猜测的起点
     * @param min   最小起点
     * @param max   最大起点
     * @return 合适的起点
     */
    private int findStartIndex(int index, int min, int max) {

        try {

            if (index <= min) {
                return min;
            }
            if (index >= max) {
                return max;
            }

            int dividerIndex = index / dividerValue;
            float dividerOffset = dividerLength * dividerIndex;
            float locationX = offset + dividerOffset + getPaddingLeft();
            final int minFix = (getWidth() - getPaddingRight() - getPaddingLeft()) / dividerLength - 1;

            if (locationX < getPaddingLeft()) {

                index = Math.min(max, index + Math.max(minFix, (max - index) / 2));
                return findStartIndex(index, min, max);
            } else if (locationX > getWidth() - getPaddingRight()) {

                index = Math.max(min, index - Math.max(minFix, (index - min) / 2));
                return findStartIndex(index, min, max);
            } else {
                return index;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            Log.i("Test", "==================index=" + index);
        }

        return index;
    }

    /**
     * 当最小值或者最小指示值变化调用
     */
    private void invalidateWhenMinValueChange() {

        if (minIndicateValue >= minValue) {
            scrollToValue(minIndicateValue);
        } else {
            scrollToValue(findClosestDividerValue(offset, getIndicatorLocation()));
        }
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        if (this.minValue == minValue) {
            return;
        }
        this.minValue = minValue;
        if (getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
            requestLayout();
        }
        invalidateWhenMinValueChange();
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        if (maxValue == this.maxValue) {
            return;
        }
        this.maxValue = maxValue;
        if (getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
            requestLayout();
        }
        invalidateWhenMinValueChange();
    }

    public int getMinIndicateValue() {
        return minIndicateValue;
    }

    public void setMinIndicateValue(int minIndicateValue) {
        this.minIndicateValue = minIndicateValue;
        invalidateWhenMinValueChange();
    }

    public int getIndicateValue() {
        return indicateValue;
    }
}
