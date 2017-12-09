package com.zq.widget.sidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 侧边字母导航
 * Created by zhangqiang on 2017/12/4.
 */

public class Sidebar extends View {

    private CharSequence[] letters;
    private TextPaint paint;
    private int textSize;
    private int textColor, selectLetterColor;
    private int letterSpacing;
    private int orientation;
    private float blockSize;
    private OnLetterSelectChangeListener onLetterSelectChangeListener;
    private String selectLetter;

    public Sidebar(Context context) {
        super(context);
        init(context, null);
    }

    public Sidebar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Sidebar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {

            TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.Sidebar);
            letters = t.getTextArray(R.styleable.Sidebar_letters);
            textSize = t.getDimensionPixelSize(R.styleable.Sidebar_android_textSize, spToPX(20));
            textColor = t.getColor(R.styleable.Sidebar_android_textColor, Color.BLACK);
            letterSpacing = t.getDimensionPixelSize(R.styleable.Sidebar_letterSpacing, dipToPx(5));
            orientation = t.getInt(R.styleable.Sidebar_android_orientation, LinearLayout.VERTICAL);
            selectLetterColor = t.getColor(R.styleable.Sidebar_selectLetterColor, textColor);
            t.recycle();
        }

        paint = new TextPaint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                setPressed(true);

            case MotionEvent.ACTION_MOVE:
                float cx = event.getX();
                float cy = event.getY();
                String lastSelectLetter = selectLetter;
                selectLetter = findLetterUnder(cx, cy);
                if (selectLetter != null && !selectLetter
                        .equals(lastSelectLetter)) {

                    if (onLetterSelectChangeListener != null) {
                        onLetterSelectChangeListener.onLetterSelectChange(selectLetter);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                selectLetter = null;
                invalidate();
                setPressed(false);
                break;
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (letters == null || blockSize <= 0) {
            return;
        }
        final int letterCount = letters.length;
        if (orientation == LinearLayout.VERTICAL) {

            for (int i = 0; i < letterCount; i++) {

                String letter = (String) letters[i];
                paint.setTextSize(textSize);
                fixTextSize(letter);
                float textWidth = paint.measureText(letter);
                float textHeight = paint.descent() - paint.ascent();
                float startX = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight() - textWidth) / 2;
                float baseLine = getPaddingTop() + letterSpacing * i + blockSize * i + (blockSize - textHeight) / 2 - paint.ascent();
                if (letter.equals(selectLetter)) {
                    paint.setColor(selectLetterColor);
                } else {
                    paint.setColor(textColor);
                }
                canvas.drawText(letter, startX, baseLine, paint);

            }
        } else if (orientation == LinearLayout.HORIZONTAL) {

            for (int i = 0; i < letterCount; i++) {

                String letter = (String) letters[i];
                paint.setTextSize(textSize);
                fixTextSize(letter);
                float textWidth = paint.measureText(letter);
                float textHeight = paint.descent() - paint.ascent();
                float startX = getPaddingLeft() + letterSpacing * i + blockSize * i + (blockSize - textWidth) / 2;
                float baseLine = getPaddingTop() - paint.ascent() + (getHeight() - getPaddingTop() - getPaddingBottom() - textHeight) / 2;
                if (letter.equals(selectLetter)) {
                    paint.setColor(selectLetterColor);
                } else {
                    paint.setColor(textColor);
                }
                canvas.drawText(letter, startX, baseLine, paint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (letters == null || orientation != LinearLayout.HORIZONTAL && orientation != LinearLayout.VERTICAL) {
            setMeasuredDimension(resolveSize(0, widthMeasureSpec), resolveSize(0, heightMeasureSpec));
            return;
        }
        if (orientation == LinearLayout.VERTICAL) {

            float width = 0;
            float height = 0;
            float letterHeight = paint.descent() - paint.ascent();
            for (CharSequence letter : letters) {

                float letterWidth = paint.measureText((String) letter);
                width = Math.max(letterWidth, width);
                height += letterHeight + letterSpacing;
            }
            height -= letterSpacing;
            width += getPaddingLeft() + getPaddingRight();
            height += getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(resolveSize((int) (width), widthMeasureSpec),
                    resolveSize((int) (height), heightMeasureSpec));
        } else {

            float width = 0;
            float height = paint.descent() - paint.ascent();
            for (CharSequence letter : letters) {

                float letterWidth = paint.measureText((String) letter);
                width += letterWidth + letterSpacing;
            }
            width -= letterSpacing;
            width += getPaddingLeft() + getPaddingRight();
            height += getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(resolveSize((int) (width), widthMeasureSpec),
                    resolveSize((int) (height), heightMeasureSpec));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (letters == null) {
            return;
        }
        final int letterCount = letters.length;
        float spacingLength = letterSpacing * (letterCount - 1);
        if (orientation == LinearLayout.HORIZONTAL) {
            blockSize = (w - getPaddingLeft() - getPaddingRight() - spacingLength) / letterCount;
        } else if (orientation == LinearLayout.VERTICAL) {
            blockSize = (h - getPaddingTop() - getPaddingBottom() - spacingLength) / letterCount;
        }
    }

    private void fixTextSize(String letter) {

        final float perCutSize = spToPX(1);
        if (orientation == LinearLayout.VERTICAL) {

            for (int i = 0; i < Integer.MAX_VALUE; i++) {

                float letterWidth = paint.measureText(letter);
                if (letterWidth > getWidth() - getPaddingLeft() - getPaddingRight()) {

                    paint.setTextSize(paint.getTextSize() - perCutSize);
                }

                float letterHeight = paint.descent() - paint.ascent();
                if (letterHeight > blockSize) {
                    paint.setTextSize(paint.getTextSize() - perCutSize);
                } else {
                    break;
                }
            }

        } else if (orientation == LinearLayout.HORIZONTAL) {

            for (int i = 0; i < Integer.MAX_VALUE; i++) {

                float letterWidth = paint.measureText(letter);
                if (letterWidth > blockSize) {

                    paint.setTextSize(paint.getTextSize() - perCutSize);
                }

                float letterHeight = paint.descent() - paint.ascent();
                if (letterHeight > getHeight() - getPaddingTop() - getPaddingBottom()) {
                    paint.setTextSize(paint.getTextSize() - perCutSize);
                } else {
                    break;
                }
            }
        }
    }

    int dipToPx(float dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    int spToPX(float sp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    public interface OnLetterSelectChangeListener {

        void onLetterSelectChange(String letter);
    }

    public OnLetterSelectChangeListener getOnLetterSelectChangeListener() {
        return onLetterSelectChangeListener;
    }

    public void setOnLetterSelectChangeListener(OnLetterSelectChangeListener onLetterSelectChangeListener) {
        this.onLetterSelectChangeListener = onLetterSelectChangeListener;
    }

    private String findLetterUnder(float x, float y) {

        if (letters == null) {
            return null;
        }
        final int letterCount = letters.length;
        for (int i = 0; i < letterCount; i++) {

            if (orientation == LinearLayout.HORIZONTAL) {

                float startX = getPaddingLeft() + letterSpacing * i + blockSize * i;
                if (x >= startX && x <= startX + blockSize) {

                    return (String) letters[i];
                }
            } else if (orientation == LinearLayout.VERTICAL) {

                float startY = getPaddingTop() + letterSpacing * i + blockSize * i;
                if (y >= startY && y <= startY + blockSize) {
                    return (String) letters[i];
                }
            }
        }
        return null;
    }
}
