package com.zq.view.recyclerview.hscroll;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zq.view.recyclerview.adapter.cell.BaseCell;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * 使RecyclerView的子view中的RecyclerView滚动相同的位置的OnAttachStateChangeListener
 * Created by zhangqiang on 2017/10/20.
 */

class RecyclerViewScrollListener implements BaseCell.OnAttachStateChangeListener {

    private ScrollableRecyclerViewGetter<RVViewHolder> recyclerViewGetter;

    RecyclerViewScrollListener(ScrollableRecyclerViewGetter<RVViewHolder> recyclerViewGetter) {
        this.recyclerViewGetter = recyclerViewGetter;
    }

    @Override
    public void onViewDetachedFromWindow(RVViewHolder viewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(RVViewHolder viewHolder) {

        RecyclerView recyclerView = recyclerViewGetter.getScrollableRecyclerView(viewHolder);
        if(recyclerView == null){
            return;
        }

        RecyclerView parentView = (RecyclerView) viewHolder.getView().getParent();
        int position = 0;
        int offset = 0;
        final int childCount = parentView.getChildCount();
        for (int i = 0; i < childCount; i++) {

            RVViewHolder childViewHolder = (RVViewHolder) parentView.getChildViewHolder(parentView.getChildAt(i));

            RecyclerView childRecyclerView = recyclerViewGetter.getScrollableRecyclerView(childViewHolder);
            if (childRecyclerView != null && childViewHolder != viewHolder) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) childRecyclerView.getLayoutManager();
                position = layoutManager.findFirstVisibleItemPosition();
                View firstChild = childRecyclerView.getChildAt(0);
                offset = firstChild.getLeft();
                break;
            }
        }

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, offset);
    }
}
