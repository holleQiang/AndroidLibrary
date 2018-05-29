package com.zq.view.recyclerview.hscroll;

import com.zq.view.recyclerview.adapter.cell.OnAttachStateChangeListener;
import com.zq.view.recyclerview.hscroll.controller.VerticalScrollController;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * 使RecyclerView的子view中的RecyclerView滚动相同的位置的OnAttachStateChangeListener
 * Created by zhangqiang on 2017/10/20.
 */

class RecyclerViewScrollListener implements OnAttachStateChangeListener {

    private VerticalScrollController verticalScrollController;

    RecyclerViewScrollListener(VerticalScrollController verticalScrollController) {
        this.verticalScrollController = verticalScrollController;
    }

    @Override
    public void onViewDetachedFromWindow(RVViewHolder viewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(RVViewHolder viewHolder) {

        if(verticalScrollController != null){
            verticalScrollController.syncVerticalScroll(viewHolder);
        }
    }

}
