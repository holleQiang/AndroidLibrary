package com.zq.view.recyclerview.hscroll.controller;

import android.support.v7.widget.RecyclerView;

/**
 * Created by zhangqiang on 2018/2/27.
 */

public interface VerticalScrollController {

    /**
     * 同步纵向的滚动
     * @param targetViewHolder     需要同步的viewHolder
     */
    void syncVerticalScroll(RecyclerView.ViewHolder targetViewHolder);
}
