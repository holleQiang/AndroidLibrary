package com.zq.view.recyclerview.adapter.cell;


import com.zq.view.recyclerview.viewholder.RecyclerViewHolder;

/**
 * Created by zhangqiang on 17-7-3.
 */

public interface DataBinder<T> {

    void bindData(RecyclerViewHolder viewHolder, T data);
}
