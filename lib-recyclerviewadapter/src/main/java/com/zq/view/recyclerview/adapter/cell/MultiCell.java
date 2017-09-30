package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 2017/8/9.
 */

public class MultiCell<T> extends BaseCell {

    private T data;
    private DataBinder<T> dataBinder;

    public MultiCell(@LayoutRes int layoutId) {
        super(layoutId);
    }

    public MultiCell(@LayoutRes int layoutId, int spanSize) {
        super(layoutId, spanSize);
    }

    public MultiCell(@LayoutRes int layoutId, T data, DataBinder<T> dataBinder) {
        super(layoutId);
        this.data = data;
        this.dataBinder = dataBinder;
    }

    public MultiCell(@LayoutRes int layoutId, int spanSize, T data, DataBinder<T> dataBinder) {
        super(layoutId, spanSize);
        this.data = data;
        this.dataBinder = dataBinder;
    }

    public T getData() {
        return data;
    }

    @Override
    public final void onBind(RVViewHolder viewHolder) {

        if(dataBinder != null){
            dataBinder.bindData(viewHolder,data);
        }
    }

    @Override
    public void onDetachFromWindow(RVViewHolder viewHolder) {
        super.onDetachFromWindow(viewHolder);
        if(dataBinder != null){
            dataBinder.onUnBind(viewHolder);
        }
    }

    public void setData(T data) {
        this.data = data;
    }

    public DataBinder<T> getDataBinder() {
        return dataBinder;
    }

    public void setDataBinder(DataBinder<T> dataBinder) {
        this.dataBinder = dataBinder;
    }

    /**
     * 将实体转换为MultiCell
     * @param layoutRes
     * @param bean
     * @param dataBinder
     * @param <T>
     * @return
     */
    public static <T> MultiCell convert(int layoutRes, T bean, DataBinder<T> dataBinder) {

        return new MultiCell<>(layoutRes, bean, dataBinder);
    }

    /**
     * 将实体List转换为MultiCell List
     * @param layoutRes
     * @param beanList
     * @param dataBinder
     * @param <T>
     * @return
     */
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

    /**
     * 将实体List转换为Cell List
     * @param layoutRes
     * @param beanList
     * @param dataBinder
     * @param <T>
     * @return
     */
    public static <T> List<Cell> convert2(int layoutRes, List<T> beanList, DataBinder<T> dataBinder) {

        if (beanList == null) {
            return null;
        }

        List<Cell> multiBeanCellList = new ArrayList<>();

        for (T bean :
                beanList) {

            multiBeanCellList.add(convert(layoutRes, bean, dataBinder));
        }
        return multiBeanCellList;
    }
}
