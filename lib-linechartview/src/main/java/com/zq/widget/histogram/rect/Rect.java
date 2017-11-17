package com.zq.widget.histogram.rect;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zq.widget.histogram.Histogram;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public class Rect {

    private long xValue;
    private long yValue;
    private int topColor, bottomColor;

    private boolean isSelected;

    public Rect(int topColor, int bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }

    public void onDraw(Canvas canvas, Paint paint, float x, float y, Histogram histogram) {

        float left,top,right,bottom;

        if(isSelected){

            left = x - histogram.getRectWidth()/2 - histogram.getRectSpacing()/2;
            right = x + histogram.getRectWidth()/2 + histogram.getRectSpacing()/2;
        }else{
            left = x - histogram.getRectWidth()/2;
            right = x + histogram.getRectWidth()/2;
        }
        if(getyValue() > 0){

            paint.setColor(topColor);
            bottom = histogram.getYAxisSizeAt(0);
            top = histogram.getYAxisSizeAt(getyValue());
        }else{

            paint.setColor(bottomColor);
            top = histogram.getYAxisSizeAt(0);
            bottom = histogram.getYAxisSizeAt(getyValue());
        }
        left = Math.max(histogram.getyAxisTranslation(),left);
        top = Math.max(histogram.getPaddingTop(),top);
        right = Math.min(histogram.getWidth() - histogram.getPaddingRight(),right);
        bottom = Math.min(histogram.getxAxisTranslation(),bottom);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public long getxValue() {
        return xValue;
    }

    public void setxValue(long xValue) {
        this.xValue = xValue;
    }

    public long getyValue() {
        return yValue;
    }

    public void setyValue(long yValue) {
        this.yValue = yValue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
