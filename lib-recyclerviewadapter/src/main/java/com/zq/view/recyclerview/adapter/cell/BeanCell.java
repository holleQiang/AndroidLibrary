package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.viewholder.RecyclerViewHolder;

/**
 * Created by zhangqiang on 2017/8/9.
 */

public abstract class BeanCell<B> extends BaseCell {

    private B bean;

    public BeanCell(@LayoutRes int layoutId, B bean) {
        super(layoutId);
        this.bean = bean;
    }

    public BeanCell(@LayoutRes int layoutId, int spanSize, B bean) {
        super(layoutId, spanSize);
        this.bean = bean;
    }

    public B getBean() {
        return bean;
    }

    @Override
    public void onBind(RecyclerViewHolder viewHolder) {

        onBind(viewHolder, bean);
    }

    public abstract void onBind(RecyclerViewHolder viewHolder, B bean);
}
