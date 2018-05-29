package com.zq.view.recyclerview.item;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * 缩放子view的监听器
 * Created by zhangqiang on 2017/12/11.
 */

public class ScaledScrollListener extends RecyclerView.OnScrollListener {

    private OrientationHelper orientationHelper;
    private int itemWidth;
    private float scaleFactor;
    private float maxScaleFactor;

    public ScaledScrollListener(int itemWidth, float scaleFactor, float maxScaleFactor) {
        this.itemWidth = itemWidth;
        this.scaleFactor = scaleFactor;
        this.maxScaleFactor = maxScaleFactor;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        updateScale(recyclerView);
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState == RecyclerView.SCROLL_STATE_IDLE){
            updateScale(recyclerView);
        }
    }

    private void updateScale(RecyclerView recyclerView) {

        if (orientationHelper == null) {
            orientationHelper = OrientationHelper.createHorizontalHelper(recyclerView.getLayoutManager());
        }

        final int center;
        if (recyclerView.getClipToPadding()) {

            center = orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2;
        } else {
            center = orientationHelper.getEnd() / 2;
        }
        final int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {

            ViewGroup childView = (ViewGroup) recyclerView.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int childStart = orientationHelper.getDecoratedStart(childView);
            childStart += layoutParams.leftMargin;
            int childWidth = orientationHelper.getDecoratedMeasurement(childView) - layoutParams.leftMargin - layoutParams.rightMargin;
            int childCenter = childStart + childWidth / 2;
            float centerDelta = (childCenter - center) % orientationHelper.getTotalSpace();
            float scale = scaleFactor + (1 - scaleFactor) * (1 - Math.abs(centerDelta / itemWidth));
            scale = scale * maxScaleFactor;
            childView.setScaleY(scale);
            childView.setScaleX(scale);
        }
    }
}
