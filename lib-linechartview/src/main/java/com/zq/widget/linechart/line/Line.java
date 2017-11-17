package com.zq.widget.linechart.line;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

import com.zq.widget.linechart.LineChart;
import com.zq.widget.linechart.point.Point;

import java.util.List;

/**
 * Created by zhangqiang on 2017/9/25.
 */

public class Line {

    private int lineColor;

    private int lineWidth;

    private List<Point> pointList;

    private Path linePath = new Path();

    private float lineAnimateValue;

    private boolean fillAreaVisible;

    private Shader fillShader;

    private int lastLineChartWidth, lastLineChartHeight;

    private int fillStartColor = Color.parseColor("#33ff4a43");
    private int fillEndColor = Color.parseColor("#33ff4a43");
    private Path fillAreaPath = new Path();

    public void onDraw(Canvas canvas, Paint paint, LineChart lineChart) {

        List<Point> pointList = getPointList();

        if (pointList == null || pointList.isEmpty()) {
            return;
        }

        final int pointCount = pointList.size();

        linePath.reset();

        Point firstPoint = pointList.get(0);
        float firstX = lineChart.getXAxisSizeAt(firstPoint.getxValue());
        final float lineXWidth = lineChart.getXAxisSizeAt(pointList.get(pointCount - 1).getxValue()) - firstX;
        float currentX = lineXWidth * lineAnimateValue + firstX;
        Point lastPoint = null;
        int drawPointCount = 0;
        for (int j = 0; j < pointCount; j++) {

            Point point = pointList.get(j);
            float x = lineChart.getXAxisSizeAt(point.getxValue());
            float y = lineChart.getYAxisSizeAt(point.getyValue());

            if (lastPoint != null) {

                float lastX = lineChart.getXAxisSizeAt(lastPoint.getxValue());
                float lastY = lineChart.getYAxisSizeAt(lastPoint.getyValue());

                if (currentX > lastX && currentX < x) {

                    float slope = (y - lastY) / (x - lastX);
                    float currentY = slope * (currentX - lastX) + lastY;
                    linePath.lineTo(currentX, currentY);
                    break;
                } else {

                    linePath.lineTo(x, y);
                }
            } else {

                linePath.moveTo(x, y);
            }
            drawPointCount++;
            lastPoint = point;
        }

        if (fillAreaVisible) {
            fillAreaPath.reset();
            fillAreaPath.addPath(linePath);
            fillAreaPath.lineTo(currentX, lineChart.getxAxisTranslation());
            fillAreaPath.lineTo(firstX, lineChart.getxAxisTranslation());
            fillAreaPath.close();

            if (fillShader == null || isLineChartSizeChanged(lineChart)) {

                lastLineChartWidth = lineChart.getWidth();
                lastLineChartHeight = lineChart.getHeight();
                fillShader = new LinearGradient(0,
                        lineChart.getxAxisTranslation(),
                        0,
                        lineChart.getPaddingTop(),
                        fillStartColor,
                        fillEndColor,
                        Shader.TileMode.CLAMP);
            }
            paint.reset();
            paint.setShader(fillShader);
            canvas.drawPath(fillAreaPath, paint);
        }

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(getLineColor());
        paint.setStrokeWidth(getLineWidth());
        canvas.drawPath(linePath, paint);

        for (int j = 0; j < drawPointCount; j++) {

            Point point = pointList.get(j);
            float pointX = lineChart.getXAxisSizeAt(point.getxValue());
            float pointY = lineChart.getYAxisSizeAt(point.getyValue());

            paint.reset();
            final int saveCount = canvas.save();
            point.onDraw(canvas, paint, pointX, pointY, lineChart);
            canvas.restoreToCount(saveCount);
        }
    }

    public void onDragging(float currentX, LineChart lineChart) {

        List<Point> pointList = getPointList();
        if (pointList == null || pointList.isEmpty()) {
            return;
        }

        Point lastPoint = null;
        final int pointCount = pointList.size();
        for (int i = 0; i < pointCount; i++) {

            Point point = pointList.get(i);
            point.setSelected(false);

            float pointX = lineChart.getXAxisSizeAt(point.getxValue());

            if (lastPoint != null) {

                float lastPointX = lineChart.getXAxisSizeAt(lastPoint.getxValue());

                if (lastPointX <= currentX && currentX <= pointX) {

                    if (currentX < (lastPointX + pointX) / 2) {
                        lastPoint.setSelected(true);
                    } else {
                        point.setSelected(true);
                    }
                }
            }

            if (i == 0 && currentX <= pointX && currentX > lineChart.getyAxisTranslation()) {
                point.setSelected(true);
            }

            if (i == pointCount - 1 && currentX >= pointX && currentX <= lineChart.getWidth() - lineChart.getPaddingRight()) {
                point.setSelected(true);
            }
            lastPoint = point;
        }
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void onDragStopped() {

        List<Point> pointList = getPointList();
        if (pointList == null || pointList.isEmpty()) {
            return;
        }

        final int pointCount = pointList.size();
        for (int i = 0; i < pointCount; i++) {

            Point point = pointList.get(i);
            point.setSelected(false);
        }
    }

    public float getLineAnimateValue() {
        return lineAnimateValue;
    }

    public void setLineAnimateValue(float lineAnimateValue) {
        this.lineAnimateValue = lineAnimateValue;
    }

    public boolean isFillAreaVisible() {
        return fillAreaVisible;
    }

    public void setFillAreaVisible(boolean fillAreaVisible) {
        this.fillAreaVisible = fillAreaVisible;
    }

    protected boolean isLineChartSizeChanged(LineChart lineChart) {

        return lastLineChartWidth != lineChart.getWidth() || lastLineChartHeight != lineChart.getHeight();
    }

    public int getFillStartColor() {
        return fillStartColor;
    }

    public void setFillStartColor(int fillStartColor) {
        this.fillStartColor = fillStartColor;
    }

    public int getFillEndColor() {
        return fillEndColor;
    }

    public void setFillEndColor(int fillEndColor) {
        this.fillEndColor = fillEndColor;
    }
}
