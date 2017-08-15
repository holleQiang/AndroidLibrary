package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;
import android.util.Log;

import com.zq.view.recyclerview.adapter.cell.ob.CellObservable;
import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;
import com.zq.view.recyclerview.viewholder.RecyclerViewHolder;


/**
 * Created by zhangqiang on 17-7-4.
 */

public abstract class BaseCell implements Cell<RecyclerViewHolder> {

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
    public void onRecycle(RecyclerViewHolder viewHolder) {

        Log.i("Test","==========onRecycle=====" + viewHolder.getLayoutPosition());
    }

    @Override
    public void onAttachToWindow(RecyclerViewHolder viewHolder) {

        Log.i("Test","==========onAttachToWindow=====" + viewHolder.getLayoutPosition());
    }

    @Override
    public void onDetachFromWindow(RecyclerViewHolder viewHolder) {
        Log.i("Test","==========onDetachFromWindow=====" + viewHolder.getLayoutPosition());
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

    public void notifyCellChange(Cell<RecyclerViewHolder> cell){

        cellObservable.notifyCellChange(cell);
    }

    public void notifyCellChange(){

        cellObservable.notifyCellChange(this);
    }
}