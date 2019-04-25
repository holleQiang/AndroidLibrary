package com.zq.view.recyclerview.adapter.cell;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

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

public abstract class BaseCell implements Cell {

    private Map<String, Object> tags;
    private List<OnAttachStateChangeListener> attachStateChangeListeners;
    private int layoutId;
    private int spanSize;
    private CellObservable cellObservable = new CellObservable();
    @Nullable
    private RVViewHolder viewHolder;
    private Context context;

    public BaseCell(@LayoutRes int layoutId) {
        this(layoutId, 1);
    }

    public BaseCell(@LayoutRes int layoutId, int spanSize) {
        this.layoutId = layoutId;
        this.spanSize = spanSize;
    }

    public BaseCell(Context context,@LayoutRes int layoutId, int spanSize) {
        this.context = context;
        this.layoutId = layoutId;
        this.spanSize = spanSize;
    }

    public abstract void onBindData(RVViewHolder viewHolder);

    @Override
    public int getLayoutId() {
        return layoutId;
    }

    public int getSpanSize() {
        return spanSize;
    }

    @Override
    public void onViewAttachedToWindow(RVViewHolder viewHolder) {
        this.viewHolder = viewHolder;
        notifyAttachState(viewHolder);
    }

    @Override
    @Nullable
    public RVViewHolder getAttachedViewHolder() {
        return viewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(RVViewHolder viewHolder) {
        this.viewHolder = null;
        notifyDetachState(viewHolder);
    }

    @Override
     public void registerCellObserver(CellObserver observer) {

        if (!cellObservable.hasObserverRegister(observer)) {
            cellObservable.registerObserver(observer);
        }
    }

    @Override
    public void unRegisterCellObserver(CellObserver observer) {

        if(cellObservable.hasObserverRegister(observer)){
            cellObservable.unregisterObserver(observer);
        }
    }

    @Override
    public RVViewHolder createView(ViewGroup parent) {
        Context tempContext = context;
        if (tempContext == null) {
            tempContext = parent.getContext();
        }
        RVViewHolder rvViewHolder = RVViewHolder.create(tempContext, getLayoutId(), parent);
        onViewCreated(rvViewHolder);
        return rvViewHolder;
    }

    public void onViewCreated(RVViewHolder viewHolder) {

    }

    public void notifyCellChange(BaseCell cell) {

        cellObservable.notifyCellChange(cell);
    }

    public void notifyCellChange() {

        cellObservable.notifyCellChange(this);
    }


    @Override
    public void addTag(String key, Object tag) {
        if (tags == null) {
            tags = new HashMap<>();
        }
        tags.put(key, tag);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getTag(String key) {
        if (tags == null) {
            return null;
        }
        return (T) tags.get(key);
    }

    @Override
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

    @Override
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