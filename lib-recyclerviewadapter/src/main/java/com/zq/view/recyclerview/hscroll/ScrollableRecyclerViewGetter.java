package com.zq.view.recyclerview.hscroll;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * 获取需要滚动的recyclerView接口
 * Created by zhangqiang on 2017/10/20.
 */

public interface ScrollableRecyclerViewGetter<V extends RecyclerView.ViewHolder> {

    /**
     * 获取需要滚动的recyclerView
     * @param viewHolder  父RecyclerView的子view的viewHolder
     * @return 需要滚动的recyclerView，如果返回空则代表不滚动
     */
    @Nullable
    RecyclerView getScrollableRecyclerView(V viewHolder);
}
