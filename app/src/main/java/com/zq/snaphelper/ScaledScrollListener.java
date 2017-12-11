package com.zq.snaphelper;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by zhangqiang on 2017/12/11.
 */

public class ScaledScrollListener extends RecyclerView.OnScrollListener {

    private OrientationHelper orientationHelper;
    private int itemWidth;
    private float scaleFactor;

    public ScaledScrollListener(int itemWidth, float scaleFactor) {
        this.itemWidth = itemWidth;
        this.scaleFactor = scaleFactor;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (orientationHelper == null) {
            orientationHelper = OrientationHelper.createHorizontalHelper(recyclerView.getLayoutManager());
        }

        final int totalSpace = orientationHelper.getTotalSpace();
        int center;
        if (recyclerView.getClipToPadding()) {

            center = orientationHelper.getStartAfterPadding() + totalSpace / 2;
        } else {
            center = totalSpace / 2;
        }
        final int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {

            ViewGroup childView = (ViewGroup) recyclerView.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int childStart = orientationHelper.getDecoratedStart(childView);
            childStart += layoutParams.leftMargin;
            int childWidth = orientationHelper.getDecoratedMeasurement(childView) - layoutParams.leftMargin - layoutParams.rightMargin;
            int childCenter = childStart + childWidth / 2;
            float centerDelta = (childCenter - center) % totalSpace;
            float scale = scaleFactor + (1 - scaleFactor) * (1 - Math.abs(centerDelta / itemWidth));
            childView.setScaleY(scale);
            childView.setScaleX(scale);
        }
    }
}
