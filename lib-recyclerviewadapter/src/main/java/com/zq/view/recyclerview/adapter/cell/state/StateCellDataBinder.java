package com.zq.view.recyclerview.adapter.cell.state;

import android.support.annotation.NonNull;

import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * Created by zhangqiang on 2017/11/1.
 */
public abstract class StateCellDataBinder<T> implements DataBinder<T> {

    public void bindWidthStateLoading(StateCell<T> stateCell, RVViewHolder viewHolder) {

    }

    public void bindWidthStateError(StateCell<T> stateCell, RVViewHolder viewHolder) {

    }

    public void bindEmptyData(StateCell<T> stateCell, RVViewHolder viewHolder) {

    }

    public void bindData(StateCell<T> stateCell, RVViewHolder viewHolder, @NonNull T data) {
        bindData(viewHolder, data);
    }

    public boolean isEmpty(T data){
        return data == null;
    }
}
