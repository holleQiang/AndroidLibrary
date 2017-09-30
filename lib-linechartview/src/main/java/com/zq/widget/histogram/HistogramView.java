package com.zq.widget.histogram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.zq.widget.AxisFrameView;
import com.zq.widget.R;
import com.zq.widget.histogram.rect.Rect;

import java.util.List;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public class HistogramView extends AxisFrameView implements Histogram{

    private List<Rect> rectList;
    private int rectSpacing;
    private float rectWidth;
    private Paint paint;
    private boolean isBeingDragged;
    private float lastMotionX, lastMotionY;
    private int mTouchSlop;

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        if(attrs != null){

            float density = context.getResources().getDisplayMetrics().density;
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistogramView);
            rectSpacing = typedArray.getDimensionPixelSize(R.styleable.HistogramView_rectSpacing, (int) (density * 5 + 0.5f));
            typedArray.recycle();
        }

        paint = new Paint();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDrawContent(Canvas canvas, float xAxisLength, float yAxisLength) {
        super.onDrawContent(canvas, xAxisLength, yAxisLength);
        if(rectList == null || rectList.isEmpty()){
            return;
        }
        for (Rect rect:
             rectList) {

            float x = getXAxisSizeAt(rect.getxValue());
            float y = getYAxisSizeAt(rect.getyValue());
            int saveCount = canvas.save();
            canvas.clipRect(x - rectWidth/2 - rectSpacing,getPaddingTop(),x + rectWidth/2 + rectSpacing,getxAxisTranslation());
            rect.onDraw(canvas,paint,x,y,this);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(rectList == null || rectList.isEmpty()){
            return;
        }
        final int count = rectList.size();
        Rect firstRect = rectList.get(0);
        Rect lastRect = rectList.get(count - 1);
        rectWidth = (getXAxisSizeAt(lastRect.getxValue()) - getXAxisSizeAt(firstRect.getxValue()) - (count - 1) * rectSpacing)/(count-1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float currX = event.getX();
        float currY = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                lastMotionX = currX;
                lastMotionY = currY;
                isBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = lastMotionX - currX;
                float deltaY = lastMotionY - currY;


                if (!isBeingDragged && Math.abs(deltaX) > mTouchSlop && Math.abs(deltaY) < mTouchSlop) {

                    onDragStart(currX);
                    isBeingDragged = true;
                }
                if (isBeingDragged) {
                    onDragging(currX);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (isBeingDragged) {
                    onDragStopped();
                }
                isBeingDragged = false;
                break;
        }

        getParent().requestDisallowInterceptTouchEvent(isBeingDragged);

        return true;
    }

    private void onDragStart(float currentX) {

        onDragging(currentX);
    }

    private void onDragStopped() {

        if (rectList == null) {
            return;
        }
        for (Rect rect : rectList) {

            rect.setSelected(false);
        }
        invalidate();
    }

    protected void onDragging(float currentX) {

        if (rectList == null) {
            return;
        }
        for (Rect rect : rectList) {

            float x = getXAxisSizeAt(rect.getxValue());
            if(currentX > x - rectWidth/2 && currentX < x + rectWidth/2){
                rect.setSelected(true);
            }else{
                rect.setSelected(false);
            }
        }
        invalidate();
    }

    public float getRectWidth() {
        return rectWidth;
    }

    public void setRectWidth(float rectWidth) {
        this.rectWidth = rectWidth;
    }

    public List<Rect> getRectList() {
        return rectList;
    }

    public void setRectList(List<Rect> rectList) {
        this.rectList = rectList;
    }

    public int getRectSpacing() {
        return rectSpacing;
    }

    public void setRectSpacing(int rectSpacing) {
        this.rectSpacing = rectSpacing;
    }
}
