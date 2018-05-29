package com.zq.view.recyclerview.hscroll.controller;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * Created by zhangqiang on 2018/2/27.
 */

public class RecyclerViewVerticalScrollController implements VerticalScrollController {

    private @IdRes int syncViewId;

    public RecyclerViewVerticalScrollController(@IdRes int syncViewId) {
        this.syncViewId = syncViewId;
    }

    @Override
    public void syncVerticalScroll(RecyclerView.ViewHolder targetViewHolder) {

        View anchorView = findAnchorView(targetViewHolder);
        if(anchorView == null || !(anchorView instanceof RecyclerView)){
            return;
        }
        View targetView = targetViewHolder.itemView.findViewById(syncViewId);
        if(targetView == null || !(targetView instanceof RecyclerView)){
            return;
        }
        RecyclerView anchorRecyclerView = (RecyclerView) anchorView;
        if(anchorRecyclerView.getChildCount() <= 0){
            return;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) anchorRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstChild = anchorRecyclerView.getChildAt(0);
        int offset = firstChild.getLeft();

        RecyclerView targetRecyclerView = (RecyclerView) targetView;
        layoutManager = (LinearLayoutManager) targetRecyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, offset);
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
