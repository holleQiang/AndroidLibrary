package com.zq.view.recyclerview.adapter.cell.ob;

import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.Cell;

/**
 * Created by zhangqiang on 2017/8/14.
 */

public interface CellObserver {

    void onCellChange(Cell cell);

    void onCellInsert(Cell cell);

    void onCellRemove(Cell cell);

}
