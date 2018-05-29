package com.zq.view.recyclerview.adapter.cell;

import com.zq.view.recyclerview.viewholder.RVViewHolder;

/**
 * Created by zhangqiang on 2017/12/23.
 */

public interface OnAttachStateChangeListener {

    void onViewDetachedFromWindow(RVViewHolder viewHolder);

    void onViewAttachedToWindow(RVViewHolder viewHolder);
}
