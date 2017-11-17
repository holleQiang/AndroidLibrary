package com.zq.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * 正方形frameLayout 高度以宽度为基准
 * Created by zhangqiang on 2017/10/17.
 */
public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(@NonNull Context context) {
        super(context);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if(heightSize >= widthSize){

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        final int childHeightMeasureSpec;
        if(layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT){

            int height = Math.max(0,widthSize - layoutParams.topMargin - layoutParams.bottomMargin);
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        }else{

            childHeightMeasureSpec = getChildMeasureSpec(widthMeasureSpec,layoutParams.topMargin + layoutParams.bottomMargin,layoutParams.width);
        }
        super.onMeasure(widthMeasureSpec, childHeightMeasureSpec);
    }
}
