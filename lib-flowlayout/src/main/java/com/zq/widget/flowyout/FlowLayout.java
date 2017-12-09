package com.zq.widget.flowyout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * Created by zhangqiang on 2017/11/21.
 */

public class FlowLayout extends ViewGroup {

    public static final int LINE_GRAVITY_LEFT = 0;
    public static final int LINE_GRAVITY_CENTER_HORIZONTAL = 1;
    public static final int LINE_GRAVITY_RIGHT = 2;
    private int lineGravity = LINE_GRAVITY_LEFT;
    private int lineCount;
    private int maxLines;
    private SparseArray<List<View>> views = new SparseArray<>();
    private SparseIntArray viewHeights = new SparseIntArray();

    public FlowLayout(Context context) {
        super(context);
        init(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {

            TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
            lineGravity = t.getInt(R.styleable.FlowLayout_lineGravity, LINE_GRAVITY_LEFT);
            maxLines = t.getInt(R.styleable.FlowLayout_maxLines,Integer.MAX_VALUE);
            t.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        int topStart = getPaddingTop();
        for (int i = 0; i < lineCount; i++) {

            List<View> lineViews = views.get(i);
            int leftStart = getPaddingLeft();
            if (lineGravity == LINE_GRAVITY_CENTER_HORIZONTAL) {

                leftStart += (r - l - getPaddingLeft() - getPaddingRight() - getLineChildWidth(lineViews)) / 2;
            } else if (lineGravity == LINE_GRAVITY_RIGHT) {

                leftStart += r - l - getPaddingLeft() - getPaddingRight() - getLineChildWidth(lineViews);
            }
            int lineHeight = viewHeights.get(i);
            for (View childView :
                    lineViews) {

                LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
                int cl = leftStart + layoutParams.leftMargin;
                int ct = topStart + layoutParams.topMargin;
                if (layoutParams.getGravity() == LayoutParams.GRAVITY_CENTER_VERTICAL) {
                    ct = (lineHeight - childView.getMeasuredHeight()) / 2;
                } else if (layoutParams.gravity == LayoutParams.GRAVITY_BOTTOM) {
                    ct = lineHeight - childView.getMeasuredHeight() - layoutParams.bottomMargin;
                }
                int cr = cl + childView.getMeasuredWidth();
                int cb = ct + childView.getMeasuredHeight();
                childView.layout(cl, ct, cr, cb);
                leftStart = cr + layoutParams.rightMargin;
            }
            topStart += viewHeights.get(i);
        }
    }

    private int getLineChildWidth(List<View> lineViews) {

        if (lineViews == null) {
            return 0;
        }
        int width = 0;
        for (View childView :
                lineViews) {

            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            width += layoutParams.leftMargin + layoutParams.rightMargin + childView.getMeasuredWidth();
        }
        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        views.clear();
        viewHeights.clear();

        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int maxParentWidth = Integer.MAX_VALUE;
        if (parentWidthMode == MeasureSpec.EXACTLY || parentWidthMode == MeasureSpec.AT_MOST) {
            maxParentWidth = parentWidthSize;
        }
        maxParentWidth = maxParentWidth - getPaddingLeft() - getPaddingRight();
        int childState = 0;
        int maxChildHeight = 0;
        int maxChildWidth = 0;
        int currentLineWidth = 0;
        int maxLinedHeight = 0;
        final int childCount = getChildCount();
        int notGoneViewCount = getNotGoneViewCount();
        int lineIndex = notGoneViewCount > 0 ? 0 : -1;
        int measureIndex = -1;
        for (int i = 0; i < childCount; i++) {

            if(maxLines <= 0){
                break;
            }

            View childView = getChildAt(i);

            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            measureIndex++;

            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            childState = combineMeasuredStates(childState, childView.getMeasuredState());

            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (currentLineWidth + childWidth > maxParentWidth) {

                //换行
                lineIndex++;
                //累计高度
                maxChildHeight += maxLinedHeight;
                //计算最大行宽
                maxChildWidth = Math.max(maxChildWidth, currentLineWidth);

                if(lineIndex + 1 > maxLines){
                    break;
                }

                //添加到集合
                addToLineViewList(lineIndex, childView);
                //计算行宽
                currentLineWidth = childWidth;
                //计算最大行高
                maxLinedHeight = Math.max(0, childHeight);
            } else {

                //添加到集合
                addToLineViewList(lineIndex, childView);
                //计算行宽
                currentLineWidth += childWidth;
                //计算最大行高
                maxLinedHeight = Math.max(maxLinedHeight, childHeight);
            }
            if (measureIndex == notGoneViewCount - 1) {
                //如果是最后一个

                //累计高度
                maxChildHeight += maxLinedHeight;
                //计算最大行宽
                maxChildWidth = Math.max(maxChildWidth, currentLineWidth);
            }
            viewHeights.put(lineIndex, maxLinedHeight);
        }
        lineCount = lineIndex + 1;
        setMeasuredDimension(resolveSizeAndState(maxChildWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec, childState),
                resolveSizeAndState(maxChildHeight + getPaddingTop() + getPaddingBottom(), heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    private void addToLineViewList(int lineIndex, View childView) {

        List<View> lineViews = views.get(lineIndex);
        if (lineViews == null) {
            lineViews = new ArrayList<>();
            views.put(lineIndex, lineViews);
        }
        lineViews.add(childView);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public int getLineCount() {
        return lineCount;
    }

    public static class LayoutParams extends MarginLayoutParams {

        public static final int GRAVITY_TOP = 0;
        public static final int GRAVITY_CENTER_VERTICAL = 1;
        public static final int GRAVITY_BOTTOM = 2;

        private int gravity = GRAVITY_TOP;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray t = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout);
            gravity = t.getInt(R.styleable.FlowLayout_Layout_gravityInLine, GRAVITY_TOP);
            t.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public int getGravity() {
            return gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }
    }

    private int getNotGoneViewCount() {

        int count = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            if (getChildAt(i).getVisibility() != GONE) {
                count++;
            }
        }
        return count;
    }
}
