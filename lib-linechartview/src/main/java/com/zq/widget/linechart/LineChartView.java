package com.zq.widget.linechart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.zq.widget.AxisFrameView;
import com.zq.widget.R;
import com.zq.widget.linechart.line.Line;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhangqiang on 2017/9/29.
 */

public class LineChartView extends AxisFrameView implements LineChart {

    private List<Line> lineList;
    private Paint paint;
    private float lineAnimateValue = 1;
    private ValueAnimator lineAnimator;


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
            if(line == null){
                continue;
            }
            line.setLineAnimateValue(lineAnimateValue);
            final int saveCount = canvas.save();
            line.onDraw(canvas, paint, this);
            canvas.restoreToCount(saveCount);
        }
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


    @Override
    protected void onDragStart(float currX, float currY) {
        super.onDragStart(currX, currY);
        if (lineList == null) {
            return;
        }
        for (Line line : lineList) {

            line.onDragStart(currX,currY,this);
        }
    }

    @Override
    protected void onDragging(float currX, float currY) {
        super.onDragging(currX, currY);
        if (lineList == null) {
            return;
        }
        for (Line line : lineList) {

            line.onDragging(currX, currY, this);
        }
    }

    @Override
    protected void onDragStopped() {
        super.onDragStopped();
        if (lineList == null) {
            return;
        }
        for (Line line : lineList) {

            line.onDragStopped();
        }
    }

}
