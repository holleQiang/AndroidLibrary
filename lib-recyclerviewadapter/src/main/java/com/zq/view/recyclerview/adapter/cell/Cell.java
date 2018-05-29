package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * cell
 * Created by zhangqiang on 2017/12/23.
 */

public interface Cell {

    int FULL_SPAN = -1;

    @Nullable
    RVViewHolder getAttachedViewHolder();

    void addTag(String key, Object tag);

    @Nullable
    <T> T getTag(String key);

    void onViewAttachedToWindow(RVViewHolder viewHolder);

    void onViewDetachedFromWindow(RVViewHolder viewHolder);

    void onBindData(RVViewHolder viewHolder);

    void onViewCreated(RVViewHolder viewHolder);

    @LayoutRes
    int getLayoutId();

    int getSpanSize();

    void registerCellObserver(CellObserver cellObserver);

    void unRegisterCellObserver(CellObserver observer);

    void addOnAttachStateChangeListener(OnAttachStateChangeListener stateChangeListener);

    void removeOnAttachStateChangeListener(OnAttachStateChangeListener listener);
}
