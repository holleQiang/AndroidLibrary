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
 * cell 基本实现类
 * Created by zhangqiang on 17-7-4.
 */

public abstract class Cell {

    public static final int FULL_SPAN = -1;

    private Map<String, Object> tags;
    private List<OnAttachStateChangeListener> attachStateChangeListeners;
    private int layoutId;
    private int spanSize;
    private CellObservable cellObservable = new CellObservable();
    private RVViewHolder viewHolder;

    public Cell(@LayoutRes int layoutId) {
        this(layoutId, 1);
    }

    public Cell(@LayoutRes int layoutId, int spanSize) {
        this.layoutId = layoutId;
        this.spanSize = spanSize;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public abstract void onBindData(RVViewHolder viewHolder);

    public void onViewAttachedToWindow(RVViewHolder viewHolder) {
        this.viewHolder = viewHolder;
        notifyAttachState(viewHolder);
    }

    public RVViewHolder getAttachedViewHolder() {
        return viewHolder;
    }

    public void onViewDetachedFromWindow(RVViewHolder viewHolder) {
        this.viewHolder = null;
        notifyDetachState(viewHolder);
    }

     void registerCellObserver(CellObserver observer) {

        if (!cellObservable.hasObserverRegister(observer)) {
            cellObservable.registerObserver(observer);
        }
    }

     void unRegisterCellObserver(CellObserver observer) {

        if(cellObservable.hasObserverRegister(observer)){
            cellObservable.unregisterObserver(observer);
        }
    }

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