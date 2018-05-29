package com.zq.view.recyclerview.hscroll.controller;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhangqiang on 2018/2/27.
 */

public class HorizontalScrollControllerImpl implements HorizontalScrollController {

    private int syncViewId;

    public HorizontalScrollControllerImpl(@IdRes int syncViewId) {
        this.syncViewId = syncViewId;
    }

    @Override
    public void syncHorizontalScroll(RecyclerView recyclerView, int dx, int scrollX) {
        final int childCount = recyclerView.getChildCount();
        for (int j = 0; j < childCount; j++) {
            View childView = recyclerView.getChildAt(j);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(childView);

            View syncView = viewHolder.itemView.findViewById(syncViewId);
            if (syncView == null) {
                continue;
            }
            syncView.scrollBy(dx, 0);
        }
    }

    @Override
    public boolean shouldSyncHorizontalScroll(RecyclerView.ViewHolder touchedViewHolder) {
        return touchedViewHolder.itemView.findViewById(syncViewId) != null;
    }

}
