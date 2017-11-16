package com.zq.view.recyclerview.hscroll;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.hscroll.controller.HorizontalScrollController;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * 使RecyclerView的子view中的RecyclerView滚动相同的位置的OnAttachStateChangeListener
 * Created by zhangqiang on 2017/10/20.
 */

class RecyclerViewScrollListener<Anchor extends View,Target extends View> implements Cell.OnAttachStateChangeListener {

    private HorizontalScrollController<Anchor,Target> recyclerViewGetter;

    RecyclerViewScrollListener(HorizontalScrollController<Anchor,Target> recyclerViewGetter) {
        this.recyclerViewGetter = recyclerViewGetter;
    }

    @Override
    public void onViewDetachedFromWindow(RVViewHolder viewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(RVViewHolder viewHolder) {

        Target targetView = recyclerViewGetter.getTargetView(viewHolder);
        if(targetView == null){
            return;
        }

        RecyclerView parentView = (RecyclerView) viewHolder.getView().getParent();
        if(parentView == null){
            return;
        }

        final int childCount = parentView.getChildCount();
        for (int i = 0; i < childCount; i++) {

            RVViewHolder childViewHolder = (RVViewHolder) parentView.getChildViewHolder(parentView.getChildAt(i));

            Anchor anchorView = recyclerViewGetter.getAnchorView(childViewHolder);
            if (anchorView != null && childViewHolder != viewHolder && recyclerViewGetter.shouldSyncVerticalScroll(anchorView,targetView)) {
                recyclerViewGetter.syncVerticalScroll(anchorView,targetView);
                break;
            }
        }
    }
}
