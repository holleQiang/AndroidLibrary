package com.zq.view.recyclerview.hscroll.controller;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * Created by zhangqiang on 2017/10/24.
 */

public class HorizontalScrollViewIdScrollController extends HorizontalScrollViewScrollController {

    private int anchorId,targetId;

    public HorizontalScrollViewIdScrollController(@IdRes int anchorId, @IdRes int targetId) {

        this.anchorId = anchorId;
        this.targetId = targetId;
    }

    @Nullable
    @Override
    public HorizontalScrollView getTargetView(RVViewHolder viewHolder) {
        View view = viewHolder.getView(targetId);
        if(view instanceof HorizontalScrollView){
            return (HorizontalScrollView) view;
        }
        return null;
    }

    @Override
    public HorizontalScrollView getAnchorView(RVViewHolder viewHolder) {
        View view = viewHolder.getView(anchorId);
        if(view instanceof HorizontalScrollView){
            return (HorizontalScrollView) view;
        }
        return null;
    }

    @Override
    public boolean shouldSyncHorizontalScroll(HorizontalScrollView anchorView, HorizontalScrollView targetView) {
        return anchorView.getId() == targetView.getId();
    }

    @Override
    public boolean shouldSyncVerticalScroll(HorizontalScrollView anchorView, HorizontalScrollView targetView) {
        return anchorView.getId() == targetView.getId();
    }
}
