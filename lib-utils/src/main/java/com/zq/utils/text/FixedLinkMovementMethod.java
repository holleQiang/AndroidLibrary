package com.caiyi.fund.trade.widget.span;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caiyi.fund.trade.R;

/**
 * Created by zhangqiang on 2017/12/19.
 */

public class FixedLinkMovementMethod extends LinkMovementMethod {

    private static LinkMovementMethod sInstance;
    private static final Start START = new Start();
    private static final End END = new End();
    private boolean isWidgetClickable;
    private boolean isWidgetLongClickable;
    private ClickableSpan touchedTarget;
    private boolean isContextClickable;

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new FixedLinkMovementMethod();
        return sInstance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {

        int action = event.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:

                isWidgetClickable = widget.isClickable();
                isWidgetLongClickable = widget.isLongClickable();
                isContextClickable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && widget.isContextClickable();

                touchedTarget = findClickableSpan(widget, buffer, event);
                if (touchedTarget != null) {

                    widget.setClickable(false);
                    widget.setLongClickable(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        widget.setContextClickable(false);
                    }
                    int start = buffer.getSpanStart(touchedTarget);
                    int end = buffer.getSpanEnd(touchedTarget);
                    setFlag(buffer, start, end);
                    buffer.setSpan(new BackgroundColorSpan(ContextCompat.getColor(widget.getContext(), R.color.btn_pressed)),
                            start,
                            end,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (touchedTarget == null) {
                    break;
                }
                ClickableSpan target = findClickableSpan(widget, buffer, event);
                if (touchedTarget != target || !isPointInView(widget, event)) {
                    resetViewState(widget, buffer);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (touchedTarget != null) {

                    target = findClickableSpan(widget, buffer, event);
                    if (isPointInView(widget, event) && touchedTarget == target) {
                        touchedTarget.onClick(widget);
                    }
                }
                resetViewState(widget, buffer);
                break;
        }
        boolean disallowTouchEvent = touchedTarget != null;
        ViewGroup viewGroup = (ViewGroup) widget.getParent();
        viewGroup.requestDisallowInterceptTouchEvent(disallowTouchEvent);
        return disallowTouchEvent;
    }

    private void resetViewState(TextView widget, Spannable buffer) {

        touchedTarget = null;
        widget.setClickable(isWidgetClickable);
        widget.setLongClickable(isWidgetLongClickable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            widget.setContextClickable(isContextClickable);
        }
        Object[] spans = buffer.getSpans(getFlagStart(buffer), getFlagEnd(buffer), BackgroundColorSpan.class);
        if (spans != null && spans.length > 0) {
            buffer.removeSpan(spans[spans.length - 1]);
        }
        clearFlag(buffer);
    }

    private static class Start {

    }

    private static class End {


    }

    private void setFlag(Spannable buffer, int start, int end) {

        int oldStart = getFlagStart(buffer);
        int oldEnd = getFlagEnd(buffer);
        if (oldStart != start || oldEnd != end) {
            buffer.setSpan(START, start, start, Spanned.SPAN_POINT_POINT | Spanned.SPAN_INTERMEDIATE);
            buffer.setSpan(END, start, start, Spanned.SPAN_POINT_POINT);
        }
    }

    private int getFlagStart(Spannable buffer) {

        return buffer.getSpanStart(START);
    }

    private int getFlagEnd(Spannable buffer) {
        return buffer.getSpanStart(END);
    }

    private void clearFlag(Spannable buffer) {

        buffer.removeSpan(START);
        buffer.removeSpan(END);
    }

    private static boolean isPointInView(View widget, MotionEvent event) {

        return event.getX() >= 0
                && event.getX() <= widget.getWidth()
                && event.getY() >= 0
                && event.getY() <= widget.getHeight();
    }

    @Nullable
    private static ClickableSpan findClickableSpan(TextView widget, Spannable buffer, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        Layout layout = widget.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);

        if (links == null || links.length <= 0) {
            return null;
        }
        return links[0];
    }

}
