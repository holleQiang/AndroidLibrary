package com.zq.view.recyclerview.adapter.cell;

import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zq.view.recyclerview.adapter.cell.ob.CellObservable;
import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * cell 基本实现类
 * Created by zhangqiang on 17-7-4.
 */

public class BaseCell implements Cell {

    private Map<String, Object> tags;
    private List<OnAttachStateChangeListener> attachStateChangeListeners;
    private int layoutId;
    private int spanSize;
    private CellObservable cellObservable = new CellObservable();
    private RVViewHolder viewHolder;

    public BaseCell(@LayoutRes int layoutId) {
        this(layoutId, 1);
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
    public void onBindData(RVViewHolder viewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(RVViewHolder viewHolder) {
        this.viewHolder = viewHolder;
        notifyAttachState(viewHolder);
    }

    @Override
    public RVViewHolder getAttachedViewHolder() {
        return viewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(RVViewHolder viewHolder) {
        this.viewHolder = null;
        notifyDetachState(viewHolder);
    }

    public void registerCellObserver(CellObserver observer) {

        if (!cellObservable.hasObserverRegister(observer)) {
            cellObservable.registerObserver(observer);
        }
    }

    public void unRegisterCellObserver(CellObserver observer) {

        if(cellObservable.hasObserverRegister(observer)){
            cellObservable.unregisterObserver(observer);
        }
    }

    @Override
    public void onViewCreated(RVViewHolder viewHolder) {

    }

    public void notifyCellChange(Cell cell) {

        cellObservable.notifyCellChange(cell);
    }

    public void notifyCellChange() {

        cellObservable.notifyCellChange(this);
    }

    public void addTag(String key, Object tag) {

        if (tags == null) {
            tags = new HashMap<>();
        }
        tags.put(key, tag);
    }

    public Object getTag(String key) {

        if (tags == null) {
            return null;
        }
        return tags.get(key);
    }

    public interface OnAttachStateChangeListener{

        void onViewDetachedFromWindow(RVViewHolder viewHolder);

        void onViewAttachedToWindow(RVViewHolder viewHolder);
    }

    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener){

        if(attachStateChangeListeners == null){
            attachStateChangeListeners  = new ArrayList<>();
        }
        if(!attachStateChangeListeners.contains(listener)){
            attachStateChangeListeners.add(listener);
            if(getAttachedViewHolder() != null){
                listener.onViewAttachedToWindow(getAttachedViewHolder());
            }
        }
    }

    public void removeOnAttachStateChangeListener(OnAttachStateChangeListener listener){

        if(attachStateChangeListeners == null){
            return;
        }
        attachStateChangeListeners.remove(listener);
    }

    private void notifyAttachState(RVViewHolder viewHolder){

        if(attachStateChangeListeners != null){
            for (OnAttachStateChangeListener listener:
                    attachStateChangeListeners) {
                listener.onViewAttachedToWindow(viewHolder);
            }
        }
    }

    private void notifyDetachState(RVViewHolder viewHolder){

        if(attachStateChangeListeners != null){
            for (OnAttachStateChangeListener listener:
                    attachStateChangeListeners) {
                listener.onViewDetachedFromWindow(viewHolder);
            }
        }
    }
}