package com.zq.widget.linechart.point;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zq.widget.linechart.LineChart;

/**
 * Created by zhangqiang on 2017/9/29.
 */

public class PointImpl extends Point {

    @Override
    public void onDraw(Canvas canvas, Paint paint, float pointX, float pointY, LineChart lineChart) {
        super.onDraw(canvas, paint, pointX, pointY,lineChart);
        paint.setStrokeWidth(1);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

//        canvas.drawColor(Color.parseColor("#20ff0000"));

        if(isSelected()){

            canvas.drawCircle(pointX,lineChart.getPaddingTop() + 10,10,paint);

            canvas.drawLine(pointX,lineChart.getPaddingTop() + 20,pointX,canvas.getHeight(),paint);
        }

        canvas.translate(pointX,pointY);
        canvas.drawCircle(0,0,10,paint);
    }
}
