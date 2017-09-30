package com.zq.view.recyclerview.adapter.cell;


import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * Created by zhangqiang on 17-7-3.
 */

public abstract class DataBinder<T> {

    public abstract void bindData(RVViewHolder viewHolder, T data);

    public void onUnBind(RVViewHolder viewHolder) {

    }
}
