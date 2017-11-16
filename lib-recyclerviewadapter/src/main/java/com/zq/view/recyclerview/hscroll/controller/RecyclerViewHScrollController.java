package com.zq.view.recyclerview.hscroll.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhangqiang on 2017/10/24.
 */

public abstract class RecyclerViewHScrollController implements HorizontalScrollController<RecyclerView,RecyclerView> {


    @Override
    public void syncHorizontalScroll(RecyclerView syncView, int dx) {

        syncView.scrollBy(dx,0);
    }

    @Override
    public void syncVerticalScroll(RecyclerView anchorView, RecyclerView syncView) {

        LinearLayoutManager layoutManager = (LinearLayoutManager) anchorView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstChild = anchorView.getChildAt(0);
        int offset = firstChild.getLeft();

        layoutManager = (LinearLayoutManager) syncView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, offset);
    }

    @Override
    public Class<RecyclerView> getAnchorViewClass() {
        return RecyclerView.class;
    }

    @Override
    public Class<RecyclerView> getTargetAnchorClass() {
        return RecyclerView.class;
    }
}
