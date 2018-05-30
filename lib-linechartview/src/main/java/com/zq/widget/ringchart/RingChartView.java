package com.zq.widget.ringchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.zq.widget.R;

import java.util.List;

/**
 * 环形图
 * Created by zhangqiang on 2017/10/18.
 */

public class RingChartView extends View {

    private int ringWidth = 200;
    private Paint paint;
    private List<ArcItem> arcItemList;
    private int totalWeight;
    private RectF ringBounds = new RectF();
    private float centerX, centerY;
    private float radius;
    private Path textPath = new Path();
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    private boolean isTextVisible;
    private int textSize;


    public RingChartView(Context context) {
        super(context);
        init(context, null);
    }

    public RingChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RingChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {

        if (attrs != null) {

            float density = context.getResources().getDisplayMetrics().density;
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingChartView);
            isTextVisible = typedArray.getBoolean(R.styleable.RingChartView_ringTextVisible, true);
            textSize = typedArray.getDimensionPixelSize(R.styleable.RingChartView_ringTextSize, (int) (20 * density + 0.5f));
            ringWidth = typedArray.getDimensionPixelSize(R.styleable.RingChartView_ringWidth, (int) (50 * density + 0.5f));
            typedArray.recycle();
        }

        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (arcItemList == null || arcItemList.isEmpty()) {
            return;
        }

        int saveFlag = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        final int startAngle = -90;
        float rotateAngle = startAngle;
        final int itemCount = arcItemList.size();
        for (int i = 0; i < itemCount; i++) {


            ArcItem arcItem = arcItemList.get(i);

            int weight = arcItem.getWeight();
            float percent = (float) weight / totalWeight;
            float sweepAngle = i == itemCount - 1 ? 360 + startAngle - rotateAngle : 360 * percent;
            paint.reset();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(arcItem.getColor());
            canvas.drawArc(ringBounds, rotateAngle, sweepAngle, true, paint);


            String text = arcItem.getText();
            if(!TextUtils.isEmpty(text) && isTextVisible){

                paint.reset();
                paint.setAntiAlias(true);
                paint.setTextSize(textSize);
                paint.setColor(arcItem.getTextColor());

                float textWidth = paint.measureText(text);
                float textHeight = paint.descent() - paint.ascent();
                float offset = radius - ringWidth + (ringWidth - textWidth) / 2;

                double radian = (rotateAngle + sweepAngle / 2) * Math.PI / 180;
                textPath.reset();
                textPath.moveTo(centerX, centerY);
                textPath.lineTo((float) (radius * Math.cos(radian) + centerX), (float) (radius * Math.sin(radian) + centerY));

                canvas.drawTextOnPath(text, textPath, offset, textHeight / 2 - paint.descent(), paint);
            }
            rotateAngle += sweepAngle;
        }

        paint.reset();
        paint.setAntiAlias(true);
        paint.setXfermode(xfermode);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, radius - ringWidth, paint);
        canvas.restoreToCount(saveFlag);
    }

    public List<ArcItem> getArcItemList() {
        return arcItemList;
    }

    public void setArcItemList(List<ArcItem> arcItemList) {
        this.arcItemList = arcItemList;
        reload();
    }

    public void reload() {

        if (arcItemList == null || arcItemList.isEmpty()) {
            return;
        }
        int totalWeight = 0;
        for (ArcItem arcItem :
                arcItemList) {

            int weight = arcItem.getWeight();
            if (weight < 0) {
                throw new IllegalArgumentException("the weight of arcItem can not be < 0");
            }
            totalWeight += arcItem.getWeight();
        }
        this.totalWeight = totalWeight;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();
        centerX = width / 2f + getPaddingLeft();
        centerY = height / 2f + getPaddingTop();
        radius = Math.min(width, height) / 2f;
        float l = centerX - radius;
        float t = centerY - radius;
        float r = centerX + radius;
        float b = centerY + radius;
        ringBounds.set(l, t, r, b);
    }
}
