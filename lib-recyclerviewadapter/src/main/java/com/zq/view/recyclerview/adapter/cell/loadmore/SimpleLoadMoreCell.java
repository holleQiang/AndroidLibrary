package com.zq.view.recyclerview.adapter.cell.loadmore;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;

/**
 * Created by zhangqiang on 2017/8/21.
 */

public class SimpleLoadMoreCell extends MultiCell<LMState> {

    public SimpleLoadMoreCell(@LayoutRes int layoutId,DataBinder<LMState> dataBinder) {
        super(layoutId, Cell.FULL_SPAN,LMState.LOADING, dataBinder);
    }


    public LMState getState() {
        return getData();
    }

    public void setState(LMState state) {
        setData(state);
        notifyCellChange();
    }
}
