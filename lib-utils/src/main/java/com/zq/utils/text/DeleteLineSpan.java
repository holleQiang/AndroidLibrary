package com.caiyi.fund.trade.widget.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created by zhangqiang on 2017/11/29.
 */

public class DeleteLineSpan extends ReplacementSpan {

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return (int) Math.ceil(paint.measureText(text,start,end));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        canvas.drawText(text,start,end,x,y,paint);
        float startY = (top + bottom)/2f;
        float textWidth = paint.measureText(text, start, end);
        paint.setStrokeWidth(2);
        canvas.drawLine(x,startY,x + textWidth,startY,paint);
    }
}
