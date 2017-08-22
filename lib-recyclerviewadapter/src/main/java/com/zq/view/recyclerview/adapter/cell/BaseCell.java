package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.adapter.cell.ob.CellObservable;
import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangqiang on 17-7-4.
 */

public abstract class MultiCell implements Cell {

    private Map<String,Object> tags;

    private int layoutId;
    private int spanSize;
    private CellObservable cellObservable = new CellObservable();

    public MultiCell(@LayoutRes int layoutId) {
        this(layoutId,1);
    }

    public MultiCell(@LayoutRes int layoutId, int spanSize) {
        this.layoutId = layoutId;
        this.spanSize = spanSize;
    }

    @Override
    public int getLayoutId() {
        return layoutId;
    }

    @Override
    public int getSpanSize() {
        return spanSize;
    }

    @Override
    public void onRecycle(RVViewHolder viewHolder) {

    }

    @Override
    public void onAttachToWindow(RVViewHolder viewHolder) {

    }

    @Override
    public void onDetachFromWindow(RVViewHolder viewHolder) {

    }

    public void registerCellObserver(CellObserver observer){

        if(cellObservable.hasObserverRegister(observer)){
            return;
        }
        cellObservable.registerObserver(observer);
    }

    public void unRegisterCellObserver(CellObserver observer){

        cellObservable.unregisterObserver(observer);
    }

    public void notifyCellChange(Cell cell){

        cellObservable.notifyCellChange(cell);
    }

    public void notifyCellChange(){

        cellObservable.notifyCellChange(this);
    }

    public void addTag(String key,Object tag){

        if(tags == null){
            tags = new HashMap<>();
        }
        tags.put(key, tag);
    }

    public Object getTag(String key){

        if(tags == null){
            return null;
        }
        return tags.get(key);
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

        return new MultiCell(new LayoutWrapper<>(layoutRes, bean, dataBinder));
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