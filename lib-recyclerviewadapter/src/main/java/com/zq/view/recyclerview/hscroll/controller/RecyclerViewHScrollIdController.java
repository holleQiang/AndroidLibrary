package com.zq.view.recyclerview.hscroll.controller;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * Created by zhangqiang on 2017/10/24.
 */

public class RecyclerViewHScrollIdController extends RecyclerViewHScrollController {

    private int anchorId,targetId;

    public RecyclerViewHScrollIdController(@IdRes int anchorId, @IdRes int targetId) {

        this.anchorId = anchorId;
        this.targetId = targetId;
    }

    @Nullable
    @Override
    public RecyclerView getTargetView(RVViewHolder viewHolder) {

        View view = viewHolder.getView(targetId);
        if(view instanceof RecyclerView){
            return (RecyclerView) view;
        }
        return null;
    }

    @Override
    public RecyclerView getAnchorView(RVViewHolder viewHolder) {
        View view = viewHolder.getView(anchorId);
        if(view instanceof RecyclerView){
            return (RecyclerView) view;
        }
        return null;
    }

    @Override
    public boolean shouldSyncHorizontalScroll(RecyclerView anchorView, RecyclerView targetView) {
        return anchorView.getId() == targetView.getId();
    }

    @Override
    public boolean shouldSyncVerticalScroll(RecyclerView anchorView, RecyclerView targetView) {
        return anchorView.getId() == targetView.getId();
    }
}
