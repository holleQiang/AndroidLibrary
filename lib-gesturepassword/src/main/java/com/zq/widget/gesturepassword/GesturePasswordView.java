package com.zq.widget.gesturepassword;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
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
    private float circleFactor = 0.6f;
    private float fillCircleFactor = 0.4f;
    private OnGetPasswordListener onGetPasswordListener;

    public GesturePasswordView(Context context) {
        super(context);
        init();
    }

    public GesturePasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GesturePasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        paint = new Paint();
        paint.setAntiAlias(true);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        setLayerType(LAYER_TYPE_SOFTWARE,paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRegions(canvas, paint);

        drawLine(canvas, paint);
    }

    private void drawRegions(Canvas canvas, Paint paint) {

        for (Region region : regions) {

            boolean isSelected = selectRegions.contains(region);

            if(isSelected){
                drawSelectedRegion(canvas, region, paint);
            }else{
                drawRegion(canvas, region, paint);
            }

        }
    }

    private void drawSelectedRegion(Canvas canvas, Region region, Paint paint) {

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        region.getBounds(regionBounds);

        int centerX = regionBounds.centerX();
        int centerY = regionBounds.centerY();
        float circleRadius = getRegionCircleRadius(regionBounds);
        canvas.drawCircle(centerX, centerY, circleRadius, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(20,0,0, Color.BLACK);
        canvas.drawCircle(centerX, centerY, circleRadius * fillCircleFactor, paint);
    }

    private void drawRegion(Canvas canvas, Region region, Paint paint) {

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        region.getBounds(regionBounds);

        int centerX = regionBounds.centerX();
        int centerY = regionBounds.centerY();
        float circleRadius = getRegionCircleRadius(regionBounds);

        canvas.drawCircle(centerX, centerY, circleRadius, paint);
    }

    private void drawLine(Canvas canvas, Paint paint) {

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(20);
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

        float mCurrMotionX = event.getX();
        float mCurrMotionY = event.getY();

        switch (MotionEventCompat.getActionMasked(event)) {

            case MotionEvent.ACTION_DOWN:

                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
                isBeingDragged = false;
                reset();
                isBeingDragged = dispatchSelectRegion((int) mCurrMotionX, (int) mCurrMotionY);
                break;
            case MotionEvent.ACTION_MOVE:

                dispatchSelectRegion((int) mCurrMotionX, (int) mCurrMotionY);

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

    private boolean dispatchSelectRegion(int mCurrMotionX, int mCurrMotionY) {

        for (Region region : regions) {

            region.getBounds(regionBounds);
            float radius = getRegionCircleRadius(regionBounds);
            int centerX = regionBounds.centerX();
            int centerY = regionBounds.centerY();
            double centerOffset = Math.sqrt((mCurrMotionX - centerX) * (mCurrMotionX - centerX) + (mCurrMotionY - centerY) * (mCurrMotionY - centerY));

            if (!selectRegions.contains(region)
                    && centerOffset < radius) {

                selectRegions.add(region);
                return true;
            }
        }
        return false;
    }

    private Region getLastSelectedRegion() {

        int selectCount = selectRegions.size();
        if (selectCount <= 0) {
            return null;
        }
        return selectRegions.get(selectCount - 1);
    }

    private float getRegionCircleRadius(Rect regionBounds) {

        return Math.min(regionBounds.width(), regionBounds.height()) / 2f * circleFactor;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int contentWidth = w - getPaddingLeft() - getPaddingRight();
        int contentHeight = h - getPaddingTop() - getPaddingBottom();
        int singleWidth = contentWidth / columnCount;
        int singleHeight = contentHeight / rowCount;

        int l, t, r, b;
        for (int i = 0; i < rowCount; i++) {

            t = getPaddingTop() + singleHeight * i;
            b = t + singleHeight;

            for (int j = 0; j < columnCount; j++) {

                l = getPaddingLeft() + singleWidth * j;
                r = l + singleWidth;

                Log.i("Test", "==========l========" + l + "=t==" + t + "===r==" + r + "==b===" + b);
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

    public float getCircleFactor() {
        return circleFactor;
    }

    public void setCircleFactor(float circleFactor) {
        this.circleFactor = circleFactor;
    }

    public float getFillCircleFactor() {
        return fillCircleFactor;
    }

    public void setFillCircleFactor(float fillCircleFactor) {
        this.fillCircleFactor = fillCircleFactor;
    }

    /**
     * 清空选择
     */
    public void clearSelections(){

        selectRegions.clear();
        postInvalidate();
    }
}
