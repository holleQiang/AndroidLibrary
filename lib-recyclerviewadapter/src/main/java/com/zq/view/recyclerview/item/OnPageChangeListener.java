package com.zq.view.recyclerview.item;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhangqiang on 2017/12/12.
 */

public abstract class OnPageChangeListener extends RecyclerView.OnScrollListener {

    private int mLastPosition = -1;
    private OrientationHelper orientationHelper;


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
            return;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (orientationHelper == null) {
            orientationHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }

        final int rvCenter;
        if (recyclerView.getClipToPadding()) {
            rvCenter = orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2;
        } else {
            rvCenter = orientationHelper.getTotalSpace() / 2;
        }

        int minSpace = Integer.MAX_VALUE;
        View minCloseView = null;
        final int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View childView = recyclerView.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            final int childStart = orientationHelper.getDecoratedStart(childView) + layoutParams.leftMargin;
            final int childWidth = orientationHelper.getDecoratedMeasurement(childView) - layoutParams.leftMargin - layoutParams.rightMargin;
            final int childCenter = childStart + childWidth / 2;
            final int space = Math.abs(childCenter - rvCenter);
            if (minSpace > space) {
                minSpace = space;
                minCloseView = childView;
            }
        }
        if (minCloseView != null) {
            int position = recyclerView.getChildViewHolder(minCloseView).getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            if (mLastPosition != position) {

                onPageChanged(position);
                mLastPosition = position;
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract void onPageChanged(int position);
}
