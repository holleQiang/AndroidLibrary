package com.zq.widget.linechart.point;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.zq.widget.linechart.LineChart;

/**
 * Created by zhangqiang on 2017/9/29.
 */

public class Point {

    private int xValue;

    private int yValue;

    private boolean selected;

    public void onDraw(Canvas canvas, Paint paint, float pointX, float pointY,LineChart lineChart){


    }

    public int getxValue() {
        return xValue;
    }

    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    public int getyValue() {
        return yValue;
    }

    public void setyValue(int yValue) {
        this.yValue = yValue;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
