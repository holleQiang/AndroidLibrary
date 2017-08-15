package com.zq.view.recyclerview.adapter.cell.ob;

import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.Cell;

/**
 * Created by zhangqiang on 2017/8/14.
 */

public interface CellObserver<VH extends RecyclerView.ViewHolder> {

    void onCellChange(Cell<VH> cell);

    void onCellInsert(Cell<VH> cell);

    void onCellRemove(Cell<VH> cell);

}
