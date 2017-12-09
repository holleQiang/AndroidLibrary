package com.zq.widget.gesturepassword;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码控件
 * Created by zhangqiang on 2017/10/18.
 */

public class GesturePasswordView extends View {

    private int rowCount = 3;
    private int columnCount = 3;
    private List<Region> regions = new ArrayList<>();
    private Paint paint;
    private Rect regionBounds = new Rect();
    private List<Region> selectRegions = new ArrayList<>();
    private boolean isBeingDragged;
    private float mLastMotionX, mLastMotionY;
    private int mTouchSlop;
    private Path linePath = new Path();
    private Path draggedPath = new Path();
    private OnGetPasswordListener onGetPasswordListener;
    private boolean dragEnable = true;
    private int circleColor;
    private int selectCircleColor;
    private int circleRadius;
    private int largeCircleRadius;
    private int largeCircleColor;
    private int lineColor;
    private int lineWidth;


    public GesturePasswordView(Context context) {
        super(context);
        init(context, null);
    }

    public GesturePasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GesturePasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {

            TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.GesturePasswordView, R.attr.GesturePasswordViewStyle, 0);

            rowCount = t.getInt(R.styleable.GesturePasswordView_rowCount, 3);
            columnCount = t.getInt(R.styleable.GesturePasswordView_columnCount, 3);
            circleColor = t.getColor(R.styleable.GesturePasswordView_circleColor, Color.GRAY);
            selectCircleColor = t.getColor(R.styleable.GesturePasswordView_selectCircleColor, Color.LTGRAY);
            circleRadius = t.getDimensionPixelSize(R.styleable.GesturePasswordView_circleRadius, (int) dipToPx(22));
            largeCircleRadius = t.getDimensionPixelSize(R.styleable.GesturePasswordView_LargeCircleRadius, (int) dipToPx(65));
            largeCircleColor = t.getColor(R.styleable.GesturePasswordView_LargeCircleColor,Color.RED);
            lineColor = t.getColor(R.styleable.GesturePasswordView_lineColor, Color.GRAY);
            lineWidth = t.getDimensionPixelSize(R.styleable.GesturePasswordView_lineWidth, (int) dipToPx(2));
            t.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRegions(canvas);

        drawLine(canvas);
    }

    private void drawRegions(Canvas canvas) {

        for (Region region : regions) {

            boolean isSelected = selectRegions.contains(region);

            if (isSelected) {
                drawSelectedRegion(canvas, region);
            } else {
                drawUnSelectRegion(canvas, region);
            }
        }
    }

    private void drawSelectedRegion(Canvas canvas, Region region) {

        if (circleRadius > largeCircleRadius) {

            drawDefaultCircle(canvas, region,true);
            drawLargeCircle(canvas, region);
        } else {

            drawLargeCircle(canvas, region);
            drawDefaultCircle(canvas, region,true);
        }
    }

    private void drawUnSelectRegion(Canvas canvas, Region region) {

        drawDefaultCircle(canvas, region,false);
    }

    private void drawLargeCircle(Canvas canvas, Region region) {

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(largeCircleColor);

        region.getBounds(regionBounds);

        int centerX = regionBounds.centerX();
        int centerY = regionBounds.centerY();
        canvas.drawCircle(centerX, centerY, largeCircleRadius, paint);
    }

    private void drawDefaultCircle(Canvas canvas, Region region,boolean selected) {

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(selected ? selectCircleColor : circleColor);

        region.getBounds(regionBounds);

        int centerX = regionBounds.centerX();
        int centerY = regionBounds.centerY();
        canvas.drawCircle(centerX, centerY, circleRadius, paint);
    }

    private void drawLine(Canvas canvas) {

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        paint.setStrokeJoin(Paint.Join.ROUND);

        linePath.reset();

        int regionCount = selectRegions.size();
        if (regionCount <= 0) {
            return;
        }
        for (int i = 0; i < regionCount; i++) {

            Region region = selectRegions.get(i);

            region.getBounds(regionBounds);

            if (i == 0) {
                linePath.moveTo(regionBounds.centerX(), regionBounds.centerY());
            } else {
                linePath.lineTo(regionBounds.centerX(), regionBounds.centerY());
            }
        }
        linePath.addPath(draggedPath);

        canvas.drawPath(linePath, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!dragEnable) {
            return super.onTouchEvent(event);
        }

        float mCurrMotionX = event.getX();
        float mCurrMotionY = event.getY();

        switch (MotionEventCompat.getActionMasked(event)) {

            case MotionEvent.ACTION_DOWN:

                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
                isBeingDragged = false;
                reset();
                isBeingDragged = computeSelectRegion((int) mCurrMotionX, (int) mCurrMotionY);
                break;
            case MotionEvent.ACTION_MOVE:

                computeSelectRegion((int) mCurrMotionX, (int) mCurrMotionY);

                float diffXAbs = Math.abs(mLastMotionX - mCurrMotionX);
                float diffYAbs = Math.abs(mLastMotionY - mCurrMotionY);
                if (!isBeingDragged && (diffXAbs > mTouchSlop || diffYAbs > mTouchSlop)) {
                    isBeingDragged = true;
                }

                Region lastSelectedRegion = getLastSelectedRegion();
                if (lastSelectedRegion != null) {

                    draggedPath.reset();
                    lastSelectedRegion.getBounds(regionBounds);
                    draggedPath.moveTo(regionBounds.centerX(), regionBounds.centerY());
                    draggedPath.lineTo(mCurrMotionX, mCurrMotionY);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                reset();

                if (onGetPasswordListener != null && !selectRegions.isEmpty()) {
                    onGetPasswordListener.onGetPassword(selectRegions.size(), generatorPassword(selectRegions));
                }
                break;
        }
        getParent().requestDisallowInterceptTouchEvent(isBeingDragged);
        return true;
    }

    private String generatorPassword(List<Region> selectRegions) {

        StringBuilder sb = new StringBuilder();
        for (Region region :
                selectRegions) {
            sb.append(regions.indexOf(region));
        }
        return sb.toString();
    }

    private void reset() {

        draggedPath.reset();
        isBeingDragged = false;
        invalidate();
    }

    private boolean computeSelectRegion(int mCurrMotionX, int mCurrMotionY) {

        for (Region region : regions) {

            region.getBounds(regionBounds);
            int centerX = regionBounds.centerX();
            int centerY = regionBounds.centerY();
            int xDistance = mCurrMotionX - centerX;
            int yDistance = mCurrMotionY - centerY;
            double centerOffset = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

            if (!selectRegions.contains(region)
                    && centerOffset < largeCircleRadius) {

                selectRegions.add(region);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(Math.max(0,width),Math.max(0,height));
        setMeasuredDimension(resolveSize(size, widthMeasureSpec), resolveSize(size, heightMeasureSpec));
    }

    private Region getLastSelectedRegion() {

        int selectCount = selectRegions.size();
        if (selectCount <= 0) {
            return null;
        }
        return selectRegions.get(selectCount - 1);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        computeRegions();
    }

    private void computeRegions() {

        regions.clear();
        if (columnCount <= 0 || rowCount <= 0) {
            return;
        }
        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int singleWidth = contentWidth / columnCount;
        int singleHeight = contentHeight / rowCount;

        int l, t, r, b;
        for (int i = 0; i < rowCount; i++) {

            t = getPaddingTop() + singleHeight * i;
            b = t + singleHeight;

            for (int j = 0; j < columnCount; j++) {

                l = getPaddingLeft() + singleWidth * j;
                r = l + singleWidth;

                regions.add(new Region(l, t, r, b));
            }
        }
    }

    /**
     * 密码获取回调接口
     */
    public interface OnGetPasswordListener {

        void onGetPassword(int selectCount, String password);
    }

    public void setOnGetPasswordListener(OnGetPasswordListener onGetPasswordListener) {
        this.onGetPasswordListener = onGetPasswordListener;
    }


    /**
     * 清空选择
     */
    public void clearSelections() {

        selectRegions.clear();
        postInvalidate();
    }

    /**
     * 清空选择
     */
    public void clearSelectionsDelay(int delayMillions) {

        dragEnable = false;
        postDelayed(new Runnable() {
            @Override
            public void run() {

                dragEnable = true;
                selectRegions.clear();
                postInvalidate();
            }
        }, delayMillions);
    }

    float dipToPx(float dip) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        computeRegions();
        postInvalidate();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        computeRegions();
        postInvalidate();
    }

    public boolean isDragEnable() {
        return dragEnable;
    }

    public void setDragEnable(boolean dragEnable) {
        this.dragEnable = dragEnable;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        postInvalidate();
    }

    public int getSelectCircleColor() {
        return selectCircleColor;
    }

    public void setSelectCircleColor(int selectCircleColor) {
        this.selectCircleColor = selectCircleColor;
        postInvalidate();
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        postInvalidate();
    }

    public int getLargeCircleRadius() {
        return largeCircleRadius;
    }

    public void setLargeCircleRadius(int largeCircleRadius) {
        this.largeCircleRadius = largeCircleRadius;
        postInvalidate();
    }

    public int getLargeCircleColor() {
        return largeCircleColor;
    }

    public void setLargeCircleColor(int largeCircleColor) {
        this.largeCircleColor = largeCircleColor;
        postInvalidate();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        postInvalidate();
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        postInvalidate();
    }
}
