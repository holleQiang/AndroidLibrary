package com.zq.view.wheelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

/**
 * 3D 滚轮控件
 * Created by zhangqiang on 2017/8/10.
 */

public class TDWheelView extends View {

    private int visibleItemCount = 11;
    private float lineSpacing = 1.2f;
    private float textScale = 0.6f;
    private Adapter adapter;
    private Paint textPaint, extraTextPaint, linePaint;
    private float itemHeight;
    private float radius;
    private float verticalOffset;
    private float topLine, bottomLine, centerLine;
    private int extraTextPadding = 50;
    private float extraTextSize = 50;
    private int lineWidth, lineColor;

    private boolean isBeingDragged;
    private float lastMotionY;
    private float touchSlop;
    private SmoothScrollRunnable smoothScrollRunnable;
    private FlingRunnable flingRunnable;
    private VelocityTracker velocityTracker;
    private float maxVelocity, minVelocity;
    private int selectPosition, firstVisiblePosition, lastVisiblePosition;
    private float maxTextHeight;
    private int selectTextColor = Color.BLACK;
    private int unSelectTextColor = Color.GRAY;
    private float textSize = 100;
    private OnItemSelectListener onItemSelectListener;

    public TDWheelView(Context context) {
        super(context);
        init(context, null);
    }

    public TDWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public TDWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TDWheelView);

            visibleItemCount = typedArray.getInt(R.styleable.TDWheelView_tdw_visibleItemCount, 11);
            lineSpacing = typedArray.getFloat(R.styleable.TDWheelView_tdw_lineSpacing, 1.2f);
            textScale = typedArray.getFloat(R.styleable.TDWheelView_tdw_textScale, 0.6f);
            extraTextPadding = typedArray.getDimensionPixelSize(R.styleable.TDWheelView_tdw_extraTextPadding, 20);
            selectTextColor = typedArray.getColor(R.styleable.TDWheelView_tdw_selectTextColor, Color.BLACK);
            unSelectTextColor = typedArray.getColor(R.styleable.TDWheelView_tdw_unSelectTextColor, Color.GRAY);
            textSize = typedArray.getDimensionPixelSize(R.styleable.TDWheelView_tdw_textSize, 100);
            extraTextSize = typedArray.getDimensionPixelSize(R.styleable.TDWheelView_tdw_extraTextSize, 80);
            lineWidth = typedArray.getDimensionPixelOffset(R.styleable.TDWheelView_tdw_lineWidth, 2);
            lineColor = typedArray.getColor(R.styleable.TDWheelView_tdw_lineColor, Color.BLACK);
            typedArray.recycle();
        }


        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        extraTextPaint = new Paint();
        extraTextPaint.setTextSize(extraTextSize);
        extraTextPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(lineColor);

        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        maxVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        minVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        smoothScrollRunnable = new SmoothScrollRunnable();
        flingRunnable = new FlingRunnable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = canvas.getWidth();
        float centerX = w / 2f;

        canvas.drawLine(0, topLine, w, topLine, linePaint);
        canvas.drawLine(0, bottomLine, w, bottomLine, linePaint);

        final int count = adapter == null ? 0 : adapter.getItemCount();

        if (count <= 0) {
            return;
        }

        for (int i = 0; i < count; i++) {


            float radian = (itemHeight * i + verticalOffset) / radius;

            float itemTop = itemHeight * i + verticalOffset;
            float itemBottom = itemTop + itemHeight;
            float itemCenter = itemTop + itemHeight / 2;
            float bottom = itemHeight * (visibleItemCount - 1);
            float center = bottom / 2;
            if (itemTop <= 0 || itemTop >= bottom) {
                continue;
            }

            if (itemTop < 0 && itemBottom > 0) {

                firstVisiblePosition = i;
            }
            if (itemTop < bottom && itemBottom > bottom) {

                lastVisiblePosition = i;
            }


            canvas.save();

            float translationY = (float) (radius - radius * Math.cos(radian) - Math.sin(radian) * itemHeight / 2);

            canvas.translate(centerX, translationY);

            float scaleY = (float) Math.sin(radian);

            //根据偏移中心的比例计算缩放
            float currentScale = textScale + (1 - textScale) * (1 - (Math.abs(center - itemCenter)) / center);
            canvas.scale(currentScale, currentScale);

            float realItemHeight = itemHeight * scaleY;

            if (translationY < centerLine && translationY + realItemHeight > bottomLine
                    || translationY < topLine && translationY + realItemHeight > centerLine) {

                selectPosition = i;
            }

            canvas.scale(1, scaleY);

            String text = adapter.getTextAt(i);
            String leftText = adapter.getLeftExtraText(i);
            String rightText = adapter.getRightExtraText(i);
            float textWidth = textPaint.measureText(text);

            float leftTextWidth = 0;
            if (leftText != null) {
                leftTextWidth = extraTextPaint.measureText(leftText);
            }

            float rightTextWidth = 0;
            if (rightText != null) {
                rightTextWidth = extraTextPaint.measureText(rightText);
            }

            if (translationY < topLine && translationY + realItemHeight > topLine) {

                canvas.save();
                canvas.clipRect(getLeftClipText(textWidth, leftTextWidth), 0, getRightClipText(textWidth, rightTextWidth), topLine - translationY);
                textPaint.setColor(unSelectTextColor);
                extraTextPaint.setColor(unSelectTextColor);
                drawText(canvas, leftTextWidth, leftText, textWidth, text, rightTextWidth, rightText);
                canvas.restore();

                canvas.save();
                canvas.clipRect(getLeftClipText(textWidth, leftTextWidth), topLine - translationY, getRightClipText(textWidth, rightTextWidth), translationY + realItemHeight);
                textPaint.setColor(selectTextColor);
                extraTextPaint.setColor(selectTextColor);
                drawText(canvas, leftTextWidth, leftText, textWidth, text, rightTextWidth, rightText);
                canvas.restore();

            } else if (translationY < bottomLine && translationY + realItemHeight > bottomLine) {


                canvas.save();
                canvas.clipRect(getLeftClipText(textWidth, leftTextWidth), 0, getRightClipText(textWidth, rightTextWidth), bottomLine - translationY);
                textPaint.setColor(selectTextColor);
                extraTextPaint.setColor(selectTextColor);
                drawText(canvas, leftTextWidth, leftText, textWidth, text, rightTextWidth, rightText);
                canvas.restore();

                canvas.save();
                canvas.clipRect(getLeftClipText(textWidth, leftTextWidth), bottomLine - translationY, getRightClipText(textWidth, rightTextWidth), translationY + realItemHeight);
                textPaint.setColor(unSelectTextColor);
                extraTextPaint.setColor(unSelectTextColor);
                drawText(canvas, leftTextWidth, leftText, textWidth, text, rightTextWidth, rightText);
                canvas.restore();
            } else if (translationY >= topLine && translationY + realItemHeight <= bottomLine) {

                textPaint.setColor(selectTextColor);
                extraTextPaint.setColor(selectTextColor);
                drawText(canvas, leftTextWidth, leftText, textWidth, text, rightTextWidth, rightText);
            } else {

                textPaint.setColor(unSelectTextColor);
                extraTextPaint.setColor(unSelectTextColor);
                drawText(canvas, leftTextWidth, leftText, textWidth, text, rightTextWidth, rightText);
            }

            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float currY = event.getY();

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                lastMotionY = currY;
                isBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaY = lastMotionY - currY;
                if (!isBeingDragged) {

                    if (Math.abs(deltaY) < touchSlop) {
                        return false;
                    }

                    if (deltaY > 0) {
                        deltaY -= touchSlop;
                    } else {
                        deltaY += touchSlop;
                    }
                    isBeingDragged = true;
                }

                int maxY = (int) (itemHeight * visibleItemCount / 2 - itemHeight / 2);
                int minY = (int) (-itemHeight * (getItemCount() - 1) + maxY);
                if (verticalOffset < minY || verticalOffset > maxY) {

                    deltaY /= 3;
                }

                lastMotionY = currY;
                verticalOffset -= deltaY;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                velocityTracker.computeCurrentVelocity(1000);
                float velocityY = velocityTracker.getYVelocity(event.getPointerId(0));
                velocityTracker.recycle();
                velocityTracker = null;

                int absVelocityY = (int) Math.abs(velocityY);
                if (absVelocityY > minVelocity && absVelocityY < maxVelocity) {

                    flingRunnable.fling((int) velocityY);
                } else {

                    smoothScrollToPosition(selectPosition);
                }
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        radius = (h - getPaddingTop() - getPaddingBottom()) / 2;

        itemHeight = (float) (radius * Math.PI / (visibleItemCount - 1));

        lineSpacing = itemHeight / maxTextHeight;

        topLine = (float) (radius - Math.sin(itemHeight / radius) * radius / 2);

        bottomLine = (float) (radius + Math.sin(itemHeight / radius) * radius / 2);

        centerLine = radius;

        scrollToPosition(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        maxTextHeight = Math.max(textPaint.descent() - textPaint.ascent(),
                extraTextPaint.descent() - extraTextPaint.ascent());

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        itemHeight = maxTextHeight * lineSpacing;

        float halfCircleLength = itemHeight * (visibleItemCount - 1);

        radius = (float) (halfCircleLength / Math.PI);

        int calculateHeight = (int) (radius * 2 + getPaddingTop() + getPaddingBottom());

        if (heightMode == MeasureSpec.AT_MOST) {

            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), Math.min(calculateHeight, measureHeight));
        } else {

            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), calculateHeight);
        }
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        final int lastItemIndex = getItemCount() - 1;
        if (selectPosition > lastItemIndex) {
            scrollToPosition(lastItemIndex);
        } else {
            invalidate();
        }
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void smoothScrollToPosition(int position) {

        int deltaY = (int) (-itemHeight * position + visibleItemCount / 2 * itemHeight - verticalOffset);
        int duration = Math.max(500, Math.abs(deltaY));
        duration = Math.min(200, duration);
        smoothScrollRunnable.smoothScrollBy(deltaY, duration);
    }

    public void scrollToPosition(int position) {

        int dstY = (int) (-itemHeight * position + visibleItemCount / 2 * itemHeight);
        smoothScrollRunnable.scrollTo(dstY);
    }

    public void setSelectPosition(int selectPosition) {
        if (selectPosition < 0 || selectPosition > getItemCount() - 1) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (this.selectPosition != selectPosition) {
            smoothScrollToPosition(selectPosition);
            this.selectPosition = selectPosition;
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    private class SmoothScrollRunnable implements Runnable {

        private final OverScroller overScroller;
        private boolean isStop;

        SmoothScrollRunnable() {

            overScroller = new OverScroller(getContext());
        }


        void smoothScrollBy(int deltaY, int duration) {

            setStop(false);

            if (!overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
            overScroller.startScroll(0, (int) verticalOffset, 0, deltaY, duration);
            ViewCompat.postOnAnimation(TDWheelView.this, this);
        }

        void scrollTo(int dstY) {

            setStop(false);

            if (!overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
            overScroller.startScroll(0, (int) verticalOffset, 0, (int) (dstY - verticalOffset), 0);
            ViewCompat.postOnAnimation(TDWheelView.this, this);
        }

        @Override
        public void run() {

            if (isStop()) {

                overScroller.abortAnimation();
                return;
            }

            if (overScroller.computeScrollOffset()) {

                verticalOffset = (float) overScroller.getCurrY();
                invalidate();
                ViewCompat.postOnAnimation(TDWheelView.this, this);
            } else {
                isStop = true;
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(selectPosition, firstVisiblePosition, lastVisiblePosition);
                }
            }
        }

        public boolean isStop() {
            return isStop;
        }

        void setStop(boolean stop) {
            isStop = stop;
        }
    }


    private class FlingRunnable implements Runnable {

        private final OverScroller overScroller;
        private boolean isStop;
        private boolean shouldFixPosition = true;

        FlingRunnable() {

            overScroller = new OverScroller(getContext());
        }

        void fling(int velocityY) {

            setStop(false);

            if (!overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
            int maxY = (int) (itemHeight * visibleItemCount / 2 - itemHeight / 2);
            int minY = (int) (-itemHeight * (getItemCount() - 1) + maxY);
            overScroller.fling(0, (int) verticalOffset, 0, velocityY, 0, 0, minY, maxY, 0, 200);
            ViewCompat.postOnAnimation(TDWheelView.this, this);
        }

        @Override
        public void run() {

            if (isStop()) {

                overScroller.abortAnimation();
                return;
            }

            if (overScroller.computeScrollOffset()) {

                float currY = overScroller.getCurrY();

                if (overScroller.isOverScrolled()) {
                    shouldFixPosition = false;
                }

                if (shouldFixPosition && overScroller.getCurrVelocity() < minVelocity) {
                    smoothScrollToPosition(selectPosition);
                    setStop(true);
                    return;
                }

                verticalOffset = currY;
                invalidate();
                ViewCompat.postOnAnimation(TDWheelView.this, this);
            } else {
                setStop(true);
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(selectPosition, firstVisiblePosition, lastVisiblePosition);
                }
            }
        }

        boolean isStop() {
            return isStop;
        }

        void setStop(boolean stop) {
            isStop = stop;
            shouldFixPosition = !stop;
        }
    }

    private int getItemCount() {

        return adapter == null ? 0 : adapter.getItemCount();
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    private void drawText(Canvas canvas,
                          float leftTextWidth, String leftText,
                          float textWidth, String text,
                          float rightTextWidth, String rightText) {

        float extraTextHeight = extraTextPaint.descent() - extraTextPaint.ascent();
        float textHeight = textPaint.descent() - textPaint.ascent();

        canvas.drawText(text, -textWidth / 2, (itemHeight - textHeight) / 2 - textPaint.ascent(), textPaint);

        if (leftTextWidth > 0) {
            canvas.save();
            canvas.translate(-leftTextWidth - extraTextPadding - textWidth / 2, 0);
            canvas.drawText(leftText, 0, (itemHeight - extraTextHeight) / 2 - extraTextPaint.ascent(), extraTextPaint);
            canvas.restore();
        }
        if (rightTextWidth > 0) {
            canvas.save();
            canvas.translate(textWidth / 2 + extraTextPadding, 0);
            canvas.drawText(rightText, 0, (itemHeight - extraTextHeight) / 2 - extraTextPaint.ascent(), extraTextPaint);
            canvas.restore();
        }
    }

    private float getLeftClipText(float textWidth, float leftTextWidth) {

        float left = -textWidth / 2;
        if (leftTextWidth > 0) {
            left -= (extraTextPadding + leftTextWidth);
        }
        return left;
    }

    private float getRightClipText(float textWidth, float rightTextWidth) {

        float right = textWidth / 2;
        if (rightTextWidth > 0) {
            right += (extraTextPadding + rightTextWidth);
        }
        return right;
    }

}
