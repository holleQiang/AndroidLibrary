package com.zq.view.recyclerview.hscroll.controller;

import android.support.v7.widget.RecyclerView;

/**
 * Created by zhangqiang on 2018/2/27.
 */

public interface HorizontalScrollController {

    /**
     * 同步横向滚动
     * @param recyclerView  recyclerView 容器
     * @param dx 横向滚动的值
     * @param scrollX 横向滚动的总值
     */
    void syncHorizontalScroll(RecyclerView recyclerView,int dx, int scrollX);

    /**
     * 是否要同步横向滚动
     * @param touchedViewHolder 按下的viewHolder
     * @return true 同步，false 不同步
     */
    boolean shouldSyncHorizontalScroll(RecyclerView.ViewHolder touchedViewHolder);

}
