package com.zq.widget.linechart.point;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.zq.widget.linechart.LineChart;

/**
 * Created by zhangqiang on 2017/9/29.
 */

public class Point {

    private long xValue;

    private long yValue;

    private boolean selected;

    private Object tag;

    public void onDraw(Canvas canvas, Paint paint, float pointX, float pointY,LineChart lineChart){


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

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
