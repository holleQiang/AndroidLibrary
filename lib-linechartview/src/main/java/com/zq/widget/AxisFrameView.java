package com.zq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zq.widget.axis.XAxis;
import com.zq.widget.axis.YAxis;

import java.util.List;

/**
 * Created by zhangqiang on 2017/9/29.
 */

public class AxisFrameView extends View implements AxisFrame{

    private static final boolean DEBUG = false;

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

    public AxisFrameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AxisFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    protected void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AxisFrameView);
        if (typedArray != null) {

            float density = context.getResources().getDisplayMetrics().density;

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
    }


    private void drawXAxis(Canvas canvas) {


        if (DEBUG) {

            canvas.save();
            canvas.clipRect(yAxisTranslation, xAxisTranslation + xAxisSpacing, getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            canvas.drawColor(Color.parseColor("#90ff0000"));
            canvas.restore();
        }

        if (xAxis == null) {
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
        final float minXAxisSize = getXAxisSizeAt(0);
        final float maxXAxisSize = getWidth() - getPaddingRight();
        for (int i = 0; i < itemSize; i++) {

            XAxis.Item xItem = xItemList.get(i);
            String drawText = xItem.getDrawText();

            float textWidth = xAxisTextPaint.measureText(drawText);
            float centerX = getXAxisSizeAt(xItem.getValue());
            float x = Math.max(minXAxisSize, centerX - textWidth / 2);
            if (centerX + textWidth / 2 > maxXAxisSize) {
                x = maxXAxisSize - textWidth;
            }

            float textHeight = xAxisTextPaint.descent() - xAxisTextPaint.ascent();
            float baseLine = xAxisTranslation + xAxisSpacing + (maxXAxisTextHeight - textHeight) / 2 - xAxisTextPaint.ascent();

            if (xAxisValueLineVisible && centerX > yAxisTranslation && centerX <= maxXAxisSize) {
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

        if (yAxis == null) {
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
            float textWidth = yAxisTextPaint.measureText(drawText);

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

            float x = getPaddingLeft() + (maxYAxisTextWidth - textWidth) / 2;
            if (isYAxisInside) {
                x += yAxisSpacing;
            }

            if (yAxisValueLineVisible && y < xAxisTranslation && y >= 0) {
                canvas.drawLine(yAxisTranslation, y, getWidth() - getPaddingRight(), y, xAxisValueLinePaint);
            }

            canvas.drawText(drawText, x, baseLine, yAxisTextPaint);
        }
    }

    protected void onDrawContent(Canvas canvas, float xAxisLength, float yAxisLength) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        maxXAxisTextHeight = getMaxXAxisHeight();
        maxYAxisTextWidth = getMaxYAxisWidth();

        if (yAxis != null) {

            if (isYAxisInside) {
                yAxisTranslation = getPaddingLeft();
            } else {
                yAxisTranslation = maxYAxisTextWidth + yAxisSpacing + getPaddingLeft();
            }
        } else {
            yAxisTranslation = getPaddingLeft();
        }

        if (xAxis != null) {
            xAxisTranslation = h - getPaddingBottom() - (maxXAxisTextHeight + xAxisSpacing);
        } else {
            xAxisTranslation = getPaddingTop();
        }

        xAxisLength = w - getPaddingRight() - yAxisTranslation;
        yAxisLength = xAxisTranslation - getPaddingTop();

        contentRectF.set((int)getyAxisTranslation(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                (int)getxAxisTranslation());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int exactWidth, exactHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            exactWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            exactHeight = heightSize;
        }
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

    public int getxAxisSpacing() {
        return xAxisSpacing;
    }

    public void setxAxisSpacing(int xAxisSpacing) {
        this.xAxisSpacing = xAxisSpacing;
    }

    public int getyAxisSpacing() {
        return yAxisSpacing;
    }

    public void setyAxisSpacing(int yAxisSpacing) {
        this.yAxisSpacing = yAxisSpacing;
    }

    public XAxis getXAxis() {
        return xAxis;
    }

    public void setXAxis(XAxis xAxis) {
        this.xAxis = xAxis;
        invalidate();
    }

    public YAxis getYAxis() {
        return yAxis;
    }

    public void setYAxis(YAxis yAxis) {
        this.yAxis = yAxis;
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
}
