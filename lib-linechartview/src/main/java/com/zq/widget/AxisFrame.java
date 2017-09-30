package com.zq.widget;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public interface AxisFrame {

    float getXAxisSizeAt(float xValue);

    float getYAxisSizeAt(float yValue);

    float getxAxisTranslation();

    float getyAxisTranslation();

    int getWidth();

    int getPaddingTop();

    int getPaddingRight();

    int getHeight();

    Rect getContentRegion();
}
