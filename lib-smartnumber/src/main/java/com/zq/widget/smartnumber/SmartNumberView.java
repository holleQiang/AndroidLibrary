package com.zq.widget.smartnumber;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 智能变化的数字效果
 * Created by zhangqiang on 2017/10/19.
 */

public class SmartNumberView extends View {

    private Paint paint = new Paint();
    private float centerY;
    private Queue<NumberInfo> numberQueue = new LinkedList<>();
    private float animatorValue;
    private NumberInfo currentNumber, nextNumber;
    private int changeFactor;
    private int textSize;
    private int textColor;

    public SmartNumberView(Context context) {
        super(context);
        init(context, null);
    }

    public SmartNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {

        if (attrs != null) {

            float density = context.getResources().getDisplayMetrics().density;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmartNumberView);
            textColor = a.getColor(R.styleable.SmartNumberView_textColor, Color.GRAY);
            textSize = a.getDimensionPixelSize(R.styleable.SmartNumberView_textSize, (int) (20 * density + 0.5f));
            a.recycle();
        }

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (ViewCompat.isInLayout(this)) {
            return;
        }

        if (currentNumber == null && nextNumber != null) {

            String nextText = nextNumber.getNumber() + "";
            drawNextText(canvas, nextText, 0, nextText.length());
        } else if (currentNumber != null && nextNumber == null) {

            String currentText = currentNumber.getNumber() + "";
            drawCurrentText(canvas, currentText, 0, currentText.length());
        } else if (currentNumber != null) {

            String currentText = currentNumber.getNumber() + "";
            String nextText = nextNumber.getNumber() + "";

            final int currCharCount = currentText.length();
            final int nextTextCount = nextText.length();
            for (int i = 0; i < currCharCount; i++) {

                char currChar = currentText.charAt(i);

                if (i <= nextTextCount - 1) {

                    char nextChar = nextText.charAt(i);
                    if (currChar != nextChar) {

                        drawCurrentText(canvas, currentText, i, i + 1);
                        drawNextText(canvas, nextText, i, i + 1);
                    } else {

                        drawSameText(canvas, currentText, i, i + 1);
                    }
                } else {

                    drawCurrentText(canvas, currentText, i, currCharCount);
                    return;
                }
            }
            drawNextText(canvas, nextText, currCharCount, nextTextCount);
        }
    }

    /**
     * 绘制相同的文字
     *
     * @param canvas      画布
     * @param currentText 当前文字
     * @param start       起点
     * @param end         终点
     */
    private void drawSameText(Canvas canvas, String currentText, int start, int end) {

        if (currentNumber == null) {
            return;
        }

        String text = currentText.substring(start, end);
        float textHeight = paint.descent() - paint.ascent();
        float baseLine = centerY + (-paint.ascent() - textHeight / 2);
        float startX = getPaddingLeft() + paint.measureText(currentText, 0, start);

        paint.setAlpha(255);
        canvas.drawText(text, startX, baseLine, paint);
    }

    /**
     * 绘制当前即将离去的文字
     *
     * @param canvas      画布
     * @param currentText 当前的文字
     * @param start       起点
     * @param end         终点
     */
    private void drawCurrentText(Canvas canvas, String currentText, int start, int end) {

        if (currentNumber == null) {
            return;
        }

        String text = currentText.substring(start, end);
        float textHeight = paint.descent() - paint.ascent();
        float baseLine = centerY + (-paint.ascent() - textHeight / 2);
        float startX = getPaddingLeft() + paint.measureText(currentText, 0, start);

        float currentBaseLine = baseLine - animatorValue * textHeight * changeFactor;

        paint.setAlpha((int) (255 * (1 - animatorValue)));
        canvas.drawText(text, startX, currentBaseLine, paint);
    }

    /**
     * 绘制即将到来的文字
     *
     * @param canvas   画布
     * @param nextText 即将到来的文字
     * @param start    起点
     * @param end      终点
     */
    private void drawNextText(Canvas canvas, String nextText, int start, int end) {

        if (nextNumber == null) {
            return;
        }

        String text = nextText.substring(start, end);
        float textHeight = paint.descent() - paint.ascent();
        float baseLine = centerY + (-paint.ascent() - textHeight / 2);
        float startX = getPaddingLeft() + paint.measureText(nextText, 0, start);

        float nextBaseLine = baseLine + (1 - animatorValue) * textHeight * changeFactor;

        paint.setAlpha((int) (255 * animatorValue));
        canvas.drawText(text, startX, nextBaseLine, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        float textHeight = paint.descent() - paint.ascent();
        float textWidth = currentNumber == null ? 0 : Math.max(getNumberWidth(currentNumber), getNumberWidth(nextNumber));
        int width = (int) Math.ceil(textWidth) + getPaddingLeft() + getPaddingRight();
        int height = (int) Math.ceil(textHeight) + getPaddingTop() + getPaddingBottom();
        if (heightMode == MeasureSpec.EXACTLY) {

            height = MeasureSpec.getSize(heightMeasureSpec);
        } else if (heightMode == MeasureSpec.AT_MOST) {

            height = Math.min(MeasureSpec.getSize(heightMeasureSpec), height);
        }
        if (widthMode == MeasureSpec.EXACTLY) {

            width = MeasureSpec.getSize(widthMeasureSpec);
        } else if (widthMode == MeasureSpec.AT_MOST) {

            width = Math.min(MeasureSpec.getSize(widthMeasureSpec), width);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerY = (h - getPaddingTop() - getPaddingBottom()) / 2 + getPaddingTop();
    }

    /**
     * 设置数字
     *
     * @param number 数字
     */
    public void setNumber(int number) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1);
        valueAnimator.setDuration(300);
        valueAnimator.addListener(animatorListener);
        valueAnimator.addUpdateListener(animatorUpdateListener);
        NumberInfo numberInfo = new NumberInfo();
        numberInfo.setNumber(number);
        numberInfo.setAnimator(valueAnimator);
        numberQueue.add(numberInfo);

        playNext();
    }

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

            animatorValue = (float) animation.getAnimatedValue();
            invalidate();
        }
    };
    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

            currentNumber = nextNumber;
            nextNumber = null;
            animatorValue = 0;
            invalidate();
            playNext();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * 播放下一个点赞动画
     */
    private void playNext() {

        if (nextNumber != null) {
            return;
        }
        NumberInfo numberInfo = numberQueue.poll();
        if (numberInfo != null) {

            if (currentNumber != null && currentNumber.getNumber() == numberInfo.getNumber()) {
                playNext();
                return;
            }
            if (currentNumber != null && currentNumber.getNumber() > numberInfo.getNumber()) {
                changeFactor = -1;
            } else {
                changeFactor = 1;
            }
            nextNumber = numberInfo;

            if (currentNumber == null) {
                animatorListener.onAnimationEnd(null);
            } else {
                numberInfo.getAnimator().start();
            }
        }
        requestLayoutIfNeed();
    }


    private static class NumberInfo {

        private int number;

        private Animator animator;

        int getNumber() {
            return number;
        }

        void setNumber(int number) {
            this.number = number;
        }

        Animator getAnimator() {
            return animator;
        }

        void setAnimator(Animator animator) {
            this.animator = animator;
        }
    }

    /**
     * 重新布局
     */
    private void requestLayoutIfNeed() {

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null || layoutParams.width > 0 || ViewCompat.isInLayout(this)) {
            return;
        }
        if (currentNumber == null && nextNumber != null) {

            requestLayout();
        } else if (currentNumber != null && nextNumber != null && getNumberWidth(currentNumber) != getNumberWidth(nextNumber)) {

            requestLayout();
        } else if (currentNumber != null && nextNumber == null) {

            requestLayout();
        }
    }

    private float getNumberWidth(NumberInfo numberInfo) {

        if (numberInfo == null) {
            return 0;
        }
        return paint.measureText(numberInfo.getNumber() + "");
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {

        if (this.textSize != textSize) {

            this.textSize = textSize;
            paint.setTextSize(textSize);
            requestLayoutIfNeed();
            postInvalidate();
        }
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        paint.setColor(textColor);
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        numberQueue.clear();
        if (currentNumber != null) {
            currentNumber.getAnimator().end();
        }
        if (nextNumber != null) {
            nextNumber.getAnimator().end();
        }
    }
}
