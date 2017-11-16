package com.zq.view.recyclerview.hscroll.controller;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * 获取需要滚动的recyclerView接口
 * Created by zhangqiang on 2017/10/20.
 */

public interface HorizontalScrollController<Anchor extends View, Target extends View> {

    /**
     * 获取需要滚动的recyclerView
     *
     * @param viewHolder 父RecyclerView的子view的viewHolder
     * @return 需要滚动的recyclerView，如果返回空则代表不滚动
     */
    @Nullable
    Target getTargetView(RVViewHolder viewHolder);

    Anchor getAnchorView(RVViewHolder viewHolder);

    /**
     * 同步横向滚动 ，在这里实现view滚动的逻辑
     *
     * @param syncView 需要同步滚动的view
     * @param dx       aimView 滚动的
     */
    void syncHorizontalScroll(Target syncView, int dx);

    /**
     * 同步纵向滚动，在这里实现view滚动的逻辑
     *
     * @param anchorView 目标view
     * @param syncView   需要同步的view
     */
    void syncVerticalScroll(Anchor anchorView, Target syncView);

    /**
     * 其他子view是否应该滚动
     *
     * @param anchorView 按下的viewHolder中的滚动的view
     * @param targetView 其他的子viewHolder中的滚动的view
     * @return 是否需要滚动
     */
    boolean shouldSyncHorizontalScroll(Anchor anchorView, Target targetView);

    boolean shouldSyncVerticalScroll(Anchor anchorView, Target targetView);

    Class<Anchor> getAnchorViewClass();

    Class<Target> getTargetAnchorClass();
}
