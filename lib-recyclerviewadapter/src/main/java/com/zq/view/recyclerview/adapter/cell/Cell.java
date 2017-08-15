package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;

/**
 * Created by zhangqiang on 17-6-29.
 */

public interface Cell<VH extends RecyclerView.ViewHolder>{

    public static final int FULL_SPAN = -1;

    @LayoutRes
    int getLayoutId();

    int getSpanSize();

    void onBind(VH viewHolder);

    void onRecycle(VH viewHolder);

    void onDetachFromWindow(VH viewHolder);

    void onAttachToWindow(VH viewHolder);

    void registerCellObserver(CellObserver observer);

    void unRegisterCellObserver(CellObserver observer);
}
