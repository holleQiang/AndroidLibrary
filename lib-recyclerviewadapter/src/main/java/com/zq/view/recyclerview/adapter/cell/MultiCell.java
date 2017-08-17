package com.zq.view.recyclerview.adapter.cell;


import com.zq.view.recyclerview.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MultiCell extends BeanCell<LayoutWrapper> {


    public MultiCell(LayoutWrapper bean) {
        super(bean.getLayoutId(), bean.getSpanSize(), bean);
    }


    @Override
    public void onBind(RecyclerViewHolder viewHolder, LayoutWrapper bean) {

        bean.bindData(viewHolder);
    }

    public static <T> MultiCell convert(int layoutRes, T bean, DataBinder<T> dataBinder) {

        return new MultiCell(new LayoutWrapper<>(layoutRes, bean, dataBinder));
    }

    public static <T> List<MultiCell> convert(int layoutRes, List<T> beanList, DataBinder<T> dataBinder) {

        if (beanList == null) {
            return null;
        }

        List<MultiCell> multiBeanCellList = new ArrayList<>();

        for (T bean :
                beanList) {

            multiBeanCellList.add(convert(layoutRes, bean, dataBinder));
        }

        return multiBeanCellList;
    }
}
