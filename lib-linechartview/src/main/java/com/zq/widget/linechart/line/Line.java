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
    private OnPointSelectListener onPointSelectListener;

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
            paint.setAntiAlias(true);
            paint.setDither(true);
            canvas.drawPath(fillAreaPath, paint);
        }

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);
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

    public void onDragStart(float currX, float currY, LineChart lineChart) {

        onDragging(currX, currY, lineChart);
    }

    public void onDragging(float currentX, float currY, LineChart lineChart) {

        Point minClosePoint = findPointAt(currentX, lineChart);

        if(minClosePoint != null){
            minClosePoint.setSelected(true);
            if(onPointSelectListener != null){
                onPointSelectListener.onPointSelect(minClosePoint);
            }
        }
    }

    public void onDragStopped() {

        List<Point> pointList = getPointList();
        if (pointList == null || pointList.isEmpty()) {
            return;
        }

        for (Point point : pointList) {
            point.setSelected(false);
        }
    }


    public Point findPointAt(float currentX,LineChart lineChart){

        List<Point> pointList = getPointList();
        if (pointList == null || pointList.isEmpty()) {
            return null;
        }

        float minDistance = Float.MAX_VALUE;
        Point minClosePoint = null;
        for (Point point : pointList) {

            point.setSelected(false);
            float distance = Math.abs(currentX - lineChart.getXAxisSizeAt(point.getxValue()));
            if(minDistance > distance){
                minDistance = distance;
                minClosePoint = point;
            }
        }
        return minClosePoint;
    }

    protected boolean isLineChartSizeChanged(LineChart lineChart) {

        return lastLineChartWidth != lineChart.getWidth() || lastLineChartHeight != lineChart.getHeight();
    }


    public interface OnPointSelectListener{

        void onPointSelect(Point point);
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

    public OnPointSelectListener getOnPointSelectListener() {
        return onPointSelectListener;
    }

    public void setOnPointSelectListener(OnPointSelectListener onPointSelectListener) {
        this.onPointSelectListener = onPointSelectListener;
    }
}
