package com.zq.view.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by zhangqiang on 2016/6/23.
 */
public interface OnItemLongLickListener{

    boolean onItemLongClick(RecyclerView.ViewHolder holder, int position);
}
