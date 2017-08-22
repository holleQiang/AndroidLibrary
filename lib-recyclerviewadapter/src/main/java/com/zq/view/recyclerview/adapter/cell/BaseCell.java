package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.adapter.cell.ob.CellObservable;
import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhangqiang on 17-7-4.
 */

public abstract class BaseCell implements Cell {

    private Map<String,Object> tags;

    private int layoutId;
    private int spanSize;
    private CellObservable cellObservable = new CellObservable();

    public BaseCell(@LayoutRes int layoutId) {
        this(layoutId,1);
    }

    public BaseCell(@LayoutRes int layoutId, int spanSize) {
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
}