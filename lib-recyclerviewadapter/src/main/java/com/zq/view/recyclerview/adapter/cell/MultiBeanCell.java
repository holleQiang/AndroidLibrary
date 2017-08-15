package com.zq.view.recyclerview.adapter.cell;


import com.zq.view.recyclerview.viewholder.RecyclerViewHolder;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MultiBeanCell extends BeanCell<LayoutWrapper> {


    public MultiBeanCell(LayoutWrapper bean) {
        super(bean.getLayoutId(), bean.getSpanSize(), bean);
    }


    @Override
    public void onBind(RecyclerViewHolder viewHolder, LayoutWrapper bean) {

        bean.bindData(viewHolder);
    }


}
