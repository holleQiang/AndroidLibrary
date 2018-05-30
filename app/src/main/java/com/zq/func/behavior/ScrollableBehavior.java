package com.zq.func.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by zhangqiang on 2017/10/17.
 */

public class ScrollableBehavior extends CoordinatorLayout.Behavior<View> {

    public ScrollableBehavior() {
    }

    public ScrollableBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        int leftScrolled = target.getScrollY();
        child.setScrollY(leftScrolled);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        NestedScrollView scrollView = (NestedScrollView) child;
        scrollView.fling((int) velocityY);
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int left = parent.getWidth() - parent.getPaddingRight() - child.getMeasuredWidth() - layoutParams.rightMargin;
        int top = parent.getPaddingTop() + layoutParams.topMargin;
        int right = left + child.getMeasuredWidth();
        int bottom = top + child.getMeasuredHeight();
        child.layout(left,top,right,bottom);
        return true;
    }


}
