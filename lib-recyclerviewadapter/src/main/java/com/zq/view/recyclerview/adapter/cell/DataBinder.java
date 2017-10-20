package com.zq.view.recyclerview.adapter.cell;


import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * 数据绑定接口
 * Created by zhangqiang on 17-7-3.
 */

public interface DataBinder<T> {

    void bindData(RVViewHolder viewHolder, T data);
}
