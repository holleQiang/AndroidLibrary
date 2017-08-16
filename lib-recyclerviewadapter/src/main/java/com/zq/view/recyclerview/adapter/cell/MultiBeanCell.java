package com.zq.view.recyclerview.adapter.cell;


import com.zq.view.recyclerview.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

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

    public static <T> MultiBeanCell convert(int layoutRes, T bean, DataBinder<T> dataBinder) {

        return new MultiBeanCell(new LayoutWrapper<>(layoutRes, bean, dataBinder));
    }

    public static <T> List<MultiBeanCell> convert(int layoutRes, List<T> beanList, DataBinder<T> dataBinder) {

        if (beanList == null) {
            return null;
        }

        List<MultiBeanCell> multiBeanCellList = new ArrayList<>();

        for (T bean :
                beanList) {

            multiBeanCellList.add(convert(layoutRes, bean, dataBinder));
        }

        return multiBeanCellList;
    }
}
