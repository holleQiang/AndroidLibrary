package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.viewholder.RecyclerViewHolder;


/**
 * Created by zhangqiang on 2017/8/9.
 */

public class LayoutWrapper<T> {

    private int layoutId;

    private int spanSize;

    private T data;

    private DataBinder<T> dataBinder;

    public LayoutWrapper(@LayoutRes int layoutId) {
        this(layoutId,null,null);
    }

    public LayoutWrapper(@LayoutRes int layoutId, int spanSize) {
        this(layoutId,spanSize,null,null);
    }

    public LayoutWrapper(@LayoutRes int layoutId, T data, DataBinder<T> dataBinder) {
       this(layoutId,1,data,dataBinder);
    }

    public LayoutWrapper(@LayoutRes int layoutId, int spanSize, T data, DataBinder<T> dataBinder) {
        this.layoutId = layoutId;
        this.spanSize = spanSize;
        this.data = data;
        this.dataBinder = dataBinder;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void bindData(RecyclerViewHolder viewHolder){

        if(this.dataBinder == null){
            return;
        }
        this.dataBinder.bindData(viewHolder,data);
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public T getData() {
        return data;
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
}
