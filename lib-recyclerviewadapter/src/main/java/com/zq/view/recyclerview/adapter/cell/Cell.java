package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * Created by zhangqiang on 17-6-29.
 */

public interface Cell{

    public static final int FULL_SPAN = -1;

    @LayoutRes
    int getLayoutId();

    int getSpanSize();

    void onBindData(RVViewHolder viewHolder);

    void onViewDetachedFromWindow(RVViewHolder viewHolder);

    void onViewAttachedToWindow(RVViewHolder viewHolder);

    RVViewHolder getAttachedViewHolder();

    void registerCellObserver(CellObserver observer);

    void unRegisterCellObserver(CellObserver observer);

    void onViewCreated(RVViewHolder viewHolder);
}
