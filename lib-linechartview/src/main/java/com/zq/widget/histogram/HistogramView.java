package com.zq.widget.histogram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.zq.widget.AxisFrameView;
import com.zq.widget.R;
import com.zq.widget.histogram.rect.Rect;
import com.zq.widget.linechart.LineChart;
import com.zq.widget.linechart.point.Point;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public class HistogramView extends AxisFrameView implements Histogram {

    private List<Rect> rectList;
    private int rectWidth;
    private int minRectSpacing;
    private int maxRectWidth;
    private Paint paint;
    private int rectSpacing;
    private OnRectSelectListener onRectSelectListener;

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        if (attrs != null) {

            float density = context.getResources().getDisplayMetrics().density;
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistogramView);
            minRectSpacing = typedArray.getDimensionPixelSize(R.styleable.HistogramView_minRectSpacing, (int) (density * 1 + 0.5f));
            maxRectWidth = typedArray.getDimensionPixelOffset(R.styleable.HistogramView_maxRectWidth, (int) (5 * density + 0.5f));
            typedArray.recycle();
        }

        paint = new Paint();
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDrawContent(Canvas canvas, float xAxisLength, float yAxisLength) {
        super.onDrawContent(canvas, xAxisLength, yAxisLength);
        if (rectList == null || rectList.isEmpty()) {
            return;
        }
        for (Rect rect :
                rectList) {

            float x = getXAxisSizeAt(rect.getxValue());
            float y = getYAxisSizeAt(rect.getyValue());
            int saveCount = canvas.save();
            canvas.clipRect(x - rectWidth / 2 - rectSpacing, getPaddingTop(), x + rectWidth / 2 + rectSpacing, getxAxisTranslation());
            rect.onDraw(canvas, paint, x, y, this);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        computeRectWidth();
    }

    private void computeRectWidth() {

        if (rectList == null || rectList.isEmpty()) {
            return;
        }
        final int count = rectList.size();
        final int spacingCount = count - 1;
        android.graphics.Rect contentRegion = getContentRegion();
        int contentWidth = contentRegion.width();
        rectWidth = (contentWidth - spacingCount * minRectSpacing) / count;
        rectWidth = Math.max(0, rectWidth);
        if (rectWidth > maxRectWidth) {
            rectWidth = maxRectWidth;
            if (spacingCount != 0) {
                rectSpacing = (contentWidth - rectWidth * count) / (count - 1);
            } else {
                rectSpacing = contentWidth - rectWidth;
            }
        }
    }

    @Override
    protected void onDragStart(float currX, float currY) {
        super.onDragStart(currX, currY);
        Rect minCloseRect = findRectAt(currX);

        if(minCloseRect != null){
            minCloseRect.setSelected(true);
            if(onRectSelectListener != null){
                onRectSelectListener.onRectSelect(minCloseRect);
            }
        }
    }

    @Override
    protected void onDragging(float currentX, float currY) {
        super.onDragging(currentX, currY);
        Rect minCloseRect = findRectAt(currentX);

        if(minCloseRect != null){
            minCloseRect.setSelected(true);
            if(onRectSelectListener != null){
                onRectSelectListener.onRectSelect(minCloseRect);
            }
        }
    }

    @Override
    protected void onDragStopped() {
        super.onDragStopped();
        if (rectList == null) {
            return;
        }
        for (Rect rect : rectList) {
            rect.setSelected(false);
        }
    }

    public List<Rect> getRectList() {
        return rectList;
    }

    public void setRectList(List<Rect> rectList) {
        this.rectList = rectList;
        computeRectWidth();
        invalidate();
    }

    @Override
    public float getRectWidth() {
        return rectWidth;
    }

    @Override
    public int getRectSpacing() {
        return rectSpacing;
    }

    public void setMinRectSpacing(int minRectSpacing) {
        this.minRectSpacing = minRectSpacing;
        computeRectWidth();
        invalidate();
    }

    public int getMaxRectWidth() {
        return maxRectWidth;
    }

    public void setMaxRectWidth(int maxRectWidth) {
        this.maxRectWidth = maxRectWidth;
        computeRectWidth();
        invalidate();
    }

    public Rect findRectAt(float currentX) {

        List<Rect> rectList = getRectList();
        if (rectList == null || rectList.isEmpty()) {
            return null;
        }

        float minDistance = Float.MAX_VALUE;
        Rect minCloseRect = null;
        for (Rect rect : rectList) {

            rect.setSelected(false);
            float distance = Math.abs(currentX - getXAxisSizeAt(rect.getxValue()));
            if (minDistance > distance) {
                minDistance = distance;
                minCloseRect = rect;
            }
        }
        return minCloseRect;
    }

    public interface OnRectSelectListener{

        void onRectSelect(Rect rect);
    }

    public void setOnRectSelectListener(OnRectSelectListener onRectSelectListener) {
        this.onRectSelectListener = onRectSelectListener;
    }
}
