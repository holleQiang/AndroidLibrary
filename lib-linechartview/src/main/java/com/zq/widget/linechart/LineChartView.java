package com.zq.widget.linechart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.zq.widget.AxisFrameView;
import com.zq.widget.linechart.line.Line;

import java.util.List;

/**
 * Created by zhangqiang on 2017/9/29.
 */

public class LineChartView extends AxisFrameView implements LineChart {

    private List<Line> lineList;
    private Paint paint;
    private float lineAnimateValue = 1;
    private ValueAnimator lineAnimator;
    private boolean isBeingDragged;
    private float lastMotionX, lastMotionY;
    private int mTouchSlop;
    private boolean touchable;

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        paint = new Paint();

        lineAnimator = ValueAnimator.ofFloat(1);
        lineAnimator.setDuration(2000);
        lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                lineAnimateValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onDrawContent(Canvas canvas, float xAxisLength, float yAxisLength) {
        super.onDrawContent(canvas, xAxisLength, yAxisLength);

        if (lineList == null) {
            return;
        }

        final int lineCount = lineList.size();
        for (int i = 0; i < lineCount; i++) {

            Line line = lineList.get(i);
            line.setLineAnimateValue(lineAnimateValue);
            final int saveCount = canvas.save();
            line.onDraw(canvas, paint, this);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!touchable){
            return super.onTouchEvent(event);
        }

        float currX = event.getX();
        float currY = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                lastMotionX = currX;
                lastMotionY = currY;
                isBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = lastMotionX - currX;
                float deltaY = lastMotionY - currY;


                if (!isBeingDragged && Math.abs(deltaX) > mTouchSlop && Math.abs(deltaY) < mTouchSlop) {

                    isBeingDragged = true;
                }
                if (isBeingDragged) {
                    onDragging(currX);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (isBeingDragged) {
                    onDragStopped();
                }
                isBeingDragged = false;
                break;
        }

        getParent().requestDisallowInterceptTouchEvent(isBeingDragged);

        return super.onTouchEvent(event);
    }

    private void onDragStopped() {

        if (lineList == null) {
            return;
        }
        for (Line line : lineList) {

            line.onDragStopped();
        }
        invalidate(getContentRegion());
    }

    protected void onDragging(float currentX) {

        if (lineList == null) {
            return;
        }
        for (Line line : lineList) {

            line.onDragging(currentX, this);
        }
        invalidate(getContentRegion());
    }

    public void startAnimation() {

        if (lineAnimator != null && !lineAnimator.isRunning()) {
            lineAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (lineAnimator != null && lineAnimator.isRunning()) {
            lineAnimator.end();
        }
    }

    public List<Line> getLineList() {
        return lineList;
    }

    public void setLineList(List<Line> lineList) {
        this.lineList = lineList;
        postInvalidate();
    }
}
