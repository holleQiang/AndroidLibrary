package com.zq.view.recyclerview.hscroll.controller;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * Created by zhangqiang on 2018/2/27.
 */

public class HorizontalScrollViewVerticalScrollController implements VerticalScrollController {

    private @IdRes
    int syncViewId;

    public HorizontalScrollViewVerticalScrollController(@IdRes int syncViewId) {
        this.syncViewId = syncViewId;
    }

    @Override
    public void syncVerticalScroll(RecyclerView.ViewHolder targetViewHolder) {

        View anchorView = findAnchorView(targetViewHolder);
        if(anchorView == null || !(anchorView instanceof HorizontalScrollView)){
            return;
        }
        View targetView = targetViewHolder.itemView.findViewById(syncViewId);
        if(targetView == null || !(targetView instanceof HorizontalScrollView)){
            return;
        }

        int scrollX = anchorView.getScrollX();
        targetView.scrollTo(scrollX,0);
    }

    @Nullable
    private View findAnchorView(RecyclerView.ViewHolder viewHolder){

        RecyclerView parentView = (RecyclerView) viewHolder.itemView.getParent();
        if(parentView == null){
            return null;
        }

        final int childCount = parentView.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View childItemView = parentView.getChildAt(i);
            RVViewHolder childViewHolder = (RVViewHolder) parentView.getChildViewHolder(childItemView);
            if(childViewHolder.equals(viewHolder)){
                continue;
            }
            View anchorView = childItemView.findViewById(syncViewId);
            if(anchorView != null){
                return anchorView;
            }
        }
        return null;
    }
}
