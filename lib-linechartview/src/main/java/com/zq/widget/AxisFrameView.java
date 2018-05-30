package com.zq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.zq.widget.axis.XAxis;
import com.zq.widget.axis.YAxis;
import com.zq.widget.linechart.line.Line;

import java.util.List;

/**
 * 坐标系view
 * Created by zhangqiang on 2017/9/29.
 */

public class AxisFrameView extends View implements AxisFrame {

    private static final boolean DEBUG = false;
    private static final int DRAG_DELAY_MILLIONS = 500;

    private XAxis xAxis;
    private YAxis yAxis;
    private Paint xAxisTextPaint, xAxisLinePaint, yAxisTextPaint, yAxisLinePaint;
    private Paint xAxisValueLinePaint, yAxisValueLinePaint;
    private float xAxisTranslation, yAxisTranslation;
    private int xAxisSpacing, yAxisSpacing;
    private float xAxisLength, yAxisLength;
    private boolean isYAxisInside;
    private boolean isDrawXAxisLine;
    private boolean isDrawYAxisLine;
    private int xAxisLineColor, yAxisLineColor;
    private int xAxisLineWidth, yAxisLineWidth;
    private int xAxisValueLineColor, yAxisValueLineColor;
    private int xAxisValueLineWidth, yAxisValueLineWidth;
    private int xAxisTextColor, yAxisTextColor;
    private float xAxisTextSize, yAxisTextSize;
    private float maxXAxisTextHeight;
    private float maxYAxisTextWidth;
    private boolean xAxisValueLineVisible, yAxisValueLineVisible;
    private Rect contentRectF = new Rect();
    private int yAxisValueLineStyle;
    private int yAxisValueLineDashWidth;
    private int yAxisValueLineDashGap;
    private int xAxisValueLineStyle;
    private int xAxisValueLineDashWidth;
    private int xAxisValueLineDashGap;
    private boolean xAxisVisible, yAxisVisible;
    private boolean dragEnable;
    private OnDragStateChangeListener onDragStateChangeListener;
    private int indicatorLineColor;
    private int indicatorLineWidth;
    private Paint indicatorPaint;
    private boolean needDrawIndicator;
    private float currX;
    private float currY;
    private boolean isBeingDragged;
    private int mTouchSlop;


    public AxisFrameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AxisFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    protected void init(Context context, AttributeSet attrs) {

        float density = context.getResources().getDisplayMetrics().density;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AxisFrameView);
        if (typedArray != null) {

            isYAxisInside = typedArray.getBoolean(R.styleable.AxisFrameView_isYAxisInside, false);
            xAxisLineWidth = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_xAxisLineWidth, 1);
            xAxisLineColor = typedArray.getColor(R.styleable.AxisFrameView_xAxisLineColor, Color.GRAY);
            xAxisTextColor = typedArray.getColor(R.styleable.AxisFrameView_xAxisTextColor, Color.BLACK);
            xAxisTextSize = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_xAxisTextSize, (int) (density * 20 + 0.5f));
            xAxisSpacing = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_xAxisSpacing, (int) (density * 10 + 0.5f));
            isDrawXAxisLine = typedArray.getBoolean(R.styleable.AxisFrameView_xAxisLineVisible, true);
            isDrawYAxisLine = typedArray.getBoolean(R.styleable.AxisFrameView_yAxisLineVisible, true);
            yAxisLineWidth = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_yAxisLineWidth, 1);
            yAxisLineColor = typedArray.getColor(R.styleable.AxisFrameView_yAxisLineColor, Color.GRAY);
            yAxisTextColor = typedArray.getColor(R.styleable.AxisFrameView_yAxisTextColor, Color.BLACK);
            yAxisTextSize = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_yAxisTextSize, (int) (density * 20 + 0.5f));
            yAxisSpacing = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_yAxisSpacing, (int) (density * 10 + 0.5f));
            xAxisValueLineColor = typedArray.getColor(R.styleable.AxisFrameView_xAxisValueLineColor, Color.GRAY);
            xAxisValueLineWidth = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_xAxisValueLineWidth, 1);
            yAxisValueLineColor = typedArray.getColor(R.styleable.AxisFrameView_yAxisValueLineColor, Color.GRAY);
            yAxisValueLineWidth = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_yAxisValueLineWidth, 1);
            xAxisValueLineVisible = typedArray.getBoolean(R.styleable.AxisFrameView_xAxisValueLineVisible, false);
            yAxisValueLineVisible = typedArray.getBoolean(R.styleable.AxisFrameView_yAxisValueLineVisible, false);
            yAxisValueLineStyle = typedArray.getInt(R.styleable.AxisFrameView_yAxisValueLineStyle, 0);
            xAxisValueLineStyle = typedArray.getInt(R.styleable.AxisFrameView_xAxisValueLineStyle, 0);
            xAxisVisible = typedArray.getBoolean(R.styleable.AxisFrameView_xAxisVisible, true);
            yAxisVisible = typedArray.getBoolean(R.styleable.AxisFrameView_yAxisVisible, true);
            dragEnable = typedArray.getBoolean(R.styleable.AxisFrameView_dragEnable, false);
            indicatorLineColor = typedArray.getColor(R.styleable.AxisFrameView_indicatorLineColor, Color.RED);
            indicatorLineWidth = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_indicatorLineWidth, 1);
            xAxisValueLineDashWidth = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_xAxisValueLineDashWidth,dipToPx(5));
            xAxisValueLineDashGap = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_xAxisValueLineDashGap,dipToPx(1));
            yAxisValueLineDashWidth = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_yAxisValueLineDashWidth,dipToPx(5));
            yAxisValueLineDashGap = typedArray.getDimensionPixelSize(R.styleable.AxisFrameView_yAxisValueLineDashGap,dipToPx(1));
            typedArray.recycle();
        }

        xAxisTextPaint = new Paint();
        xAxisTextPaint.setAntiAlias(true);
        xAxisTextPaint.setTextSize(xAxisTextSize);
        xAxisTextPaint.setColor(xAxisTextColor);

        xAxisLinePaint = new Paint();
        xAxisLinePaint.setAntiAlias(true);
        xAxisLinePaint.setStrokeWidth(xAxisLineWidth);
        xAxisLinePaint.setColor(xAxisLineColor);

        xAxisValueLinePaint = new Paint();
        xAxisValueLinePaint.setAntiAlias(true);
        xAxisValueLinePaint.setStrokeWidth(xAxisValueLineWidth);
        xAxisValueLinePaint.setColor(xAxisValueLineColor);

        yAxisValueLinePaint = new Paint();
        yAxisValueLinePaint.setAntiAlias(true);
        yAxisValueLinePaint.setStrokeWidth(yAxisValueLineWidth);
        yAxisValueLinePaint.setColor(yAxisValueLineColor);

        yAxisTextPaint = new Paint();
        yAxisTextPaint.setAntiAlias(true);
        yAxisTextPaint.setColor(yAxisTextColor);
        yAxisTextPaint.setTextSize(yAxisTextSize);

        yAxisLinePaint = new Paint();
        yAxisLinePaint.setAntiAlias(true);
        yAxisLinePaint.setColor(yAxisLineColor);
        yAxisLinePaint.setStrokeWidth(yAxisLineWidth);

        indicatorPaint = new Paint();
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setDither(true);
        indicatorPaint.setColor(indicatorLineColor);
        indicatorPaint.setStrokeWidth(indicatorLineWidth);

        if (yAxisValueLineStyle == 1) {
            PathEffect dashPathEffect = new DashPathEffect(new float[]{yAxisValueLineDashWidth, yAxisValueLineDashGap}, 0);
            yAxisValueLinePaint.setPathEffect(dashPathEffect);
        }

        if (xAxisValueLineStyle == 1) {
            PathEffect dashPathEffect = new DashPathEffect(new float[]{xAxisValueLineDashWidth, xAxisValueLineDashGap}, 0);
            xAxisValueLinePaint.setPathEffect(dashPathEffect);
        }

        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawXAxis(canvas);

        drawYAxis(canvas);

        final int saveCount = canvas.save();
        canvas.clipRect(contentRectF);
        onDrawContent(canvas, xAxisLength, yAxisLength);
        canvas.restoreToCount(saveCount);

        if (needDrawIndicator) {
            drawIndicator(canvas);
        }
    }

    private void drawIndicator(Canvas canvas) {

        canvas.drawLine(currX - indicatorLineWidth / 2, getPaddingTop(), currX - indicatorLineWidth / 2, getxAxisTranslation(), indicatorPaint);
    }


    private void drawXAxis(Canvas canvas) {


        if (DEBUG) {

            canvas.save();
            canvas.clipRect(getyAxisTranslation(), xAxisTranslation + xAxisSpacing, getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            canvas.drawColor(Color.parseColor("#90ff0000"));
            canvas.restore();
        }

        if (xAxis == null || !xAxisVisible) {
            return;
        }

        if (isDrawXAxisLine) {
            //画X轴
            canvas.drawLine(yAxisTranslation - yAxisLineWidth, xAxisTranslation + xAxisLineWidth / 2, getWidth() - getPaddingRight(), xAxisTranslation + xAxisLineWidth / 2, xAxisLinePaint);
        }

        List<XAxis.Item> xItemList = xAxis.getItems();
        if (xItemList == null) {
            return;
        }
        final int itemSize = xItemList.size();
        final float maxXAxisSize = getWidth() - getPaddingRight();
        for (int i = 0; i < itemSize; i++) {

            XAxis.Item xItem = xItemList.get(i);
            String drawText = xItem.getDrawText();

            float textWidth = xAxisTextPaint.measureText(drawText);
            float centerX = getXAxisSizeAt(xItem.getValue());
            float x = Math.max(yAxisTranslation, centerX - textWidth / 2);
            if (centerX + textWidth / 2 > maxXAxisSize) {
                x = maxXAxisSize - textWidth;
            }

            float textHeight = xAxisTextPaint.descent() - xAxisTextPaint.ascent();
            float baseLine = xAxisTranslation + xAxisSpacing + (maxXAxisTextHeight - textHeight) / 2 - xAxisTextPaint.ascent();

            if (xAxisValueLineVisible && centerX >= yAxisTranslation && centerX <= maxXAxisSize) {
                canvas.drawLine(centerX, getPaddingTop(), centerX, xAxisTranslation, xAxisValueLinePaint);
            }

            canvas.drawText(drawText, x, baseLine, xAxisTextPaint);
        }
    }


    private void drawYAxis(Canvas canvas) {

        if (DEBUG) {

            canvas.save();
            if (isYAxisInside) {
                canvas.clipRect(yAxisSpacing + getPaddingLeft(), getPaddingTop(), yAxisSpacing + getPaddingLeft() + maxYAxisTextWidth, xAxisTranslation);
            } else {
                canvas.clipRect(getPaddingLeft(), getPaddingTop(), maxYAxisTextWidth + getPaddingLeft(), xAxisTranslation);
            }
            canvas.drawColor(Color.parseColor("#90ff0000"));
            canvas.restore();
        }

        if (yAxis == null || !yAxisVisible) {
            return;
        }

        if (isDrawYAxisLine) {
            //画Y轴
            canvas.drawLine(yAxisTranslation - yAxisLineWidth / 2, getPaddingTop(), yAxisTranslation - yAxisLineWidth / 2, xAxisTranslation + xAxisLineWidth, yAxisLinePaint);
        }

        List<YAxis.Item> yItemList = yAxis.getItems();
        if (yItemList == null) {
            return;
        }

        final int itemSize = yItemList.size();
        for (int i = 0; i < itemSize; i++) {

            YAxis.Item yItem = yItemList.get(i);
            String drawText = yItem.getDrawText();
//            float textWidth = yAxisTextPaint.measureText(drawText);

            float y = getYAxisSizeAt(yItem.getValue());
            float textHeight = yAxisTextPaint.descent() - yAxisTextPaint.ascent();
            float baseLine = y - textHeight / 2 - yAxisTextPaint.ascent();
            float topLine = baseLine + yAxisTextPaint.ascent();
            float bottomLine = baseLine + yAxisTextPaint.descent();

            if (topLine < getPaddingTop()) {
                topLine = getPaddingTop();
                baseLine = topLine - yAxisTextPaint.ascent();
            }

            if (bottomLine > xAxisTranslation) {
                bottomLine = xAxisTranslation;
                baseLine = bottomLine - yAxisTextPaint.descent();
            }

//            float x = getPaddingLeft() + (maxYAxisTextWidth - textWidth) / 2;
            float x = getPaddingLeft();
            if (isYAxisInside) {
                x += yAxisSpacing;
            }

            if (yAxisValueLineVisible && y <= xAxisTranslation && y >= 0) {
                canvas.drawLine(yAxisTranslation, y, getWidth() - getPaddingRight(), y, yAxisValueLinePaint);
            }

            canvas.drawText(drawText, x, baseLine, yAxisTextPaint);
        }
    }

    protected void onDrawContent(Canvas canvas, float xAxisLength, float yAxisLength) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        calculateParams(w, h);
    }

    private void calculateParams(int w, int h) {

        if (w <= 0 || h <= 0) {
            return;
        }


        if (yAxis != null && yAxisVisible) {

            if (isYAxisInside) {
                yAxisTranslation = getPaddingLeft();
            } else {
                maxYAxisTextWidth = getMaxYAxisWidth();
                yAxisTranslation = maxYAxisTextWidth + yAxisSpacing + getPaddingLeft();
            }
        } else {
            yAxisTranslation = getPaddingLeft();
        }


        if (xAxis != null && xAxisVisible) {
            maxXAxisTextHeight = getMaxXAxisHeight();
            xAxisTranslation = h - getPaddingBottom() - (maxXAxisTextHeight + xAxisSpacing);
        } else {
            xAxisTranslation = h - getPaddingBottom();
        }

        xAxisLength = w - getPaddingRight() - yAxisTranslation;
        yAxisLength = xAxisTranslation - getPaddingTop();

        contentRectF.set((int) getyAxisTranslation(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                (int) getxAxisTranslation());
    }


    private float getMaxYAxisWidth() {

        if (yAxis == null) {
            return 0;
        }
        float max = Integer.MIN_VALUE;
        List<YAxis.Item> yItemList = yAxis.getItems();
        if (yItemList == null) {
            return 0;
        }
        final int itemSize = yItemList.size();
        for (int i = 0; i < itemSize; i++) {

            float textWidth = yAxisTextPaint.measureText(yItemList.get(i).getDrawText());
            max = Math.max(textWidth, max);
        }
        return max;
    }

    private float getMaxXAxisHeight() {

        if (xAxis == null) {
            return 0;
        }
        float max = Integer.MIN_VALUE;

        float textHeight = xAxisTextPaint.descent() - xAxisTextPaint.ascent();
        max = Math.max(textHeight, max);
        return max;
    }

    public float getXAxisSizeAt(float xValue) {

        return (xValue - xAxis.getMinValue()) / (xAxis.getMaxValue() - xAxis.getMinValue()) * xAxisLength + yAxisTranslation;
    }


    public float getYAxisSizeAt(float yValue) {

        return (1 - (yValue - yAxis.getMinValue()) / (yAxis.getMaxValue() - yAxis.getMinValue())) * yAxisLength + getPaddingTop();
    }


    public XAxis getXAxis() {
        return xAxis;
    }

    public void setXAxis(XAxis xAxis) {
        this.xAxis = xAxis;
        calculateParams(getWidth(), getHeight());
        invalidate();
    }

    public YAxis getYAxis() {
        return yAxis;
    }

    public void setYAxis(YAxis yAxis) {
        this.yAxis = yAxis;
        calculateParams(getWidth(), getHeight());
        invalidate();
    }

    public float getxAxisTranslation() {
        return xAxisTranslation;
    }

    public float getyAxisTranslation() {
        return yAxisTranslation;
    }

    @Override
    public Rect getContentRegion() {
        return contentRectF;
    }

    public float getxAxisLength() {
        return xAxisLength;
    }

    public float getyAxisLength() {
        return yAxisLength;
    }


    protected XAxis.Item findXAxisItemAt(float x) {

        if (x <= getyAxisTranslation() || x > getWidth()) {
            return null;
        }
        XAxis xAxis = getXAxis();
        if (xAxis == null) {
            return null;
        }
        List<XAxis.Item> xAxisItems = xAxis.getItems();
        if (xAxisItems == null || xAxisItems.isEmpty()) {
            return null;
        }
        float minDistance = Float.MAX_VALUE;
        XAxis.Item minCloseItem = null;
        for (XAxis.Item xAxisItem : xAxisItems) {

            float itemXLocation = getXAxisSizeAt(xAxisItem.getValue());
            float distance = Math.abs(x - itemXLocation);
            if (minDistance > distance) {
                minDistance = distance;
                minCloseItem = xAxisItem;
            }
        }
        return minCloseItem;
    }

    protected YAxis.Item findYAxisItemAt(float y) {

        if (y <= 0 || y > getxAxisTranslation()) {
            return null;
        }
        YAxis yAxis = getYAxis();
        if (yAxis == null) {
            return null;
        }
        List<YAxis.Item> yAxisItems = yAxis.getItems();
        if (yAxisItems == null || yAxisItems.isEmpty()) {
            return null;
        }
        float minDistance = Float.MAX_VALUE;
        YAxis.Item minCloseItem = null;
        for (YAxis.Item yAxisItem : yAxisItems) {

            float itemXLocation = getXAxisSizeAt(yAxisItem.getValue());
            float distance = Math.abs(y - itemXLocation);
            if (minDistance > distance) {
                minDistance = distance;
                minCloseItem = yAxisItem;
            }
        }
        return minCloseItem;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!dragEnable) {
            return super.onTouchEvent(event);
        }
        float currX = event.getX();
        float currY = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                isBeingDragged = false;
                removeCallbacks(dragDelayRunnable);
                postDelayed(dragDelayRunnable,DRAG_DELAY_MILLIONS);
                this.currX = currX;
                this.currY = currY;
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaXAbs = Math.abs(this.currX - currX);
                float deltaYAbs = Math.abs(this.currY - currY);
                if(!isBeingDragged && (deltaXAbs > mTouchSlop || deltaYAbs > mTouchSlop)){
                    removeCallbacks(dragDelayRunnable);
                }
                if(isBeingDragged){
                    onDragging(currX, currY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                removeCallbacks(dragDelayRunnable);
                isBeingDragged = false;
                onDragStopped();
                break;
        }
        getParent().requestDisallowInterceptTouchEvent(isBeingDragged);
        return true;
    }

    protected void onDragStart(float currX, float currY) {

        if (onDragStateChangeListener != null) {
            onDragStateChangeListener.onStart(currX,currY);
        }
        computeIndicatorParams(currX,currY);
        invalidate(getContentRegion());
    }

    protected void onDragging(float currX, float currY) {

        if (onDragStateChangeListener != null) {
            onDragStateChangeListener.onDragging(currX,currY);
        }
        computeIndicatorParams(currX, currY);
        invalidate(getContentRegion());
    }

    protected void onDragStopped() {

        if (onDragStateChangeListener != null) {
            onDragStateChangeListener.onStop();
        }
        needDrawIndicator = false;
        invalidate(getContentRegion());
    }

    private void computeIndicatorParams(float currX, float currY) {

        Rect contentRegion = getContentRegion();
        if (!contentRegion.contains((int) currX, (int) currY)) {
            currX = Math.max(contentRegion.left, currX);
            currX = Math.min(contentRegion.right, currX);
            currY = Math.max(contentRegion.top, currY);
            currY = Math.min(contentRegion.bottom, currY);
        }
        this.currX = currX;
        this.currY = currY;
        needDrawIndicator = true;
    }

    public interface OnDragStateChangeListener {

        void onStart(float currX, float currY);

        void onDragging(float currX, float currY);

        void onStop();
    }

    public void setOnDragStateChangeListener(OnDragStateChangeListener onDragStateChangeListener) {
        this.onDragStateChangeListener = onDragStateChangeListener;
    }

    public OnDragStateChangeListener getOnDragStateChangeListener() {
        return onDragStateChangeListener;
    }

    private int dipToPx(float dip){

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }

    private Runnable dragDelayRunnable = new Runnable() {
        @Override
        public void run() {
            isBeingDragged = true;
            onDragStart(currX,currY);
        }
    };
}
