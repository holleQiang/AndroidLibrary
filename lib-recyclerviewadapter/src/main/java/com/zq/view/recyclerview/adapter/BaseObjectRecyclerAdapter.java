package com.zq.view.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangqiang on 16-10-10.
 */

public abstract class BaseObjectRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerHeaderFooterAdapter<V> {

    private final List<T> dataList = new ArrayList<>();
    protected Context context;

    public BaseObjectRecyclerAdapter(Context context) {
        this(context, null);
    }

    public BaseObjectRecyclerAdapter(Context context, List<T> dataList) {
        if (dataList != null) {
            this.dataList.addAll(dataList);
        }
        this.context = context;
    }

    @Override
    public int getContentItemCount() {
        return this.dataList.size();
    }

    /**
     * 在指定位置添加数据
     *
     * @param data
     * @param position
     */
    public void addDataAtIndex(T data, int position) {

        this.dataList.add(position, data);

        int startPosition = getFixedPosition(position);
        notifyItemInserted(startPosition);

        notifyItemRangeChangedIfNeed(position);
    }

    /**
     * 在指定位置插入数据列表
     *
     * @param dataList
     * @param position
     */
    public void addDataListAtIndex(List<T> dataList, int position) {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        this.dataList.addAll(position, dataList);

        int startPosition = getFixedPosition(position);
        notifyItemRangeInserted(startPosition, dataList.size());

        notifyItemRangeChangedIfNeed(position);
    }


    /**
     * 移除指定位置的数据
     *
     * @param position
     */
    public void removeDataAtIndex(int position) {

        if (position < 0 || position > getContentItemCount() - 1) {
            return;
        }

        this.dataList.remove(position);
        int startPosition = getFixedPosition(position);
        notifyItemRemoved(startPosition);
        notifyItemRangeChangedIfNeed(position);
    }

    /**
     * 获取数据的索引
     *
     * @param data
     * @return
     */
    public int getDataIndex(T data) {

        if (data == null) {
            return -1;
        }
        return this.dataList.indexOf(data);
    }

    /**
     * 在末尾添加数据
     *
     * @param data
     */
    public void addDataAtLast(T data) {

        addDataAtIndex(data, this.dataList.size());
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {

        return this.dataList != null && this.dataList.isEmpty();
    }

    /**
     * 在末尾插入数据列表
     *
     * @param dataList
     */
    public void addDataListAtLast(List<T> dataList) {

        addDataListAtIndex(dataList, this.dataList.size());
    }

    /**
     * 在开始位置添加数据
     *
     * @param data
     */
    public void addDataAtFirst(T data) {

        addDataAtIndex(data, 0);
    }

    /**
     * 在开始位置添加数据列表
     *
     * @param dataList
     */
    public void addDataListAtFirst(List<T> dataList) {

        addDataListAtIndex(dataList, 0);
    }


    /**
     * 移除一个数据
     *
     * @param data
     */
    public void removeData(T data) {

        removeDataAtIndex(getDataIndex(data));
    }

    /**
     * 从指定位置移除指定数量的数据
     *
     * @param position
     * @param count
     */
    public void removeDataFrom(int position, int count) {

        if (count <= 0 || position < 0 || position + count > getContentItemCount()) {
            return;
        }

        int index = -1;
        Iterator<T> iterator = this.dataList.iterator();
        while (iterator.hasNext()) {

            iterator.next();
            index++;

            if (index >= position && index < position + count) {
                iterator.remove();
            }
            if (index == position + count) {
                break;
            }
        }
        int startPosition = getFixedPosition(position);
        notifyItemRangeRemoved(startPosition, count);
        notifyItemRangeChangedIfNeed(position);
    }

    /**
     * 从指定位置移除所有的数据
     *
     * @param position
     */
    public void removeDataFrom(int position) {

        removeDataFrom(position, getContentItemCount() - position);
    }

    /**
     * 移除所有数据
     */
    public void removeAll() {

        this.dataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置数据
     *
     * @param dataList
     */
    public void setDataList(List<T> dataList) {

        this.dataList.clear();
        if (dataList != null) {
            this.dataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取指定位置的数据
     *
     * @param position
     */
    public T getDataAt(int position) {

        return this.dataList.get(position);
    }

    /**
     * 获取数据列表
     *
     * @return
     */
    public List<T> getDataList() {

        return this.dataList;
    }

    /**
     * 交换指定位置的数据
     * @param fromPosition 从此位置开始
     * @param toPosition 到此位置
     */
    public void swap(int fromPosition,int toPosition){

        Collections.swap(this.dataList,fromPosition,toPosition);
        notifyItemMoved(getFixedPosition(fromPosition),getFixedPosition(toPosition));
    }

    /**
     * 修改指定位置的数据
     *
     * @param position
     * @param data
     */
    public void replace(int position, T data) {

        if(position < 0 || position >= this.dataList.size()){
            return;
        }
        this.dataList.set(position, data);
        notifyItemChanged(getFixedPosition(position));
    }

    /**
     * 替换数据
     * @param position
     * @param dataList
     */
    public void replace(int position,List<T> dataList){

        if(position < 0 || position >= this.dataList.size()){
            return;
        }
        if(dataList == null || dataList.isEmpty()){
            return;
        }
        final int dataSize = dataList.size();
        if(dataSize == 1){
            replace(position,dataList.get(0));
            return;
        }
        this.dataList.remove(position);
        notifyItemRemoved(getFixedPosition(position));
        this.dataList.addAll(position,dataList);
        notifyItemRangeInserted(getFixedPosition(position),dataSize);
        notifyItemRangeChangedIfNeed(position + dataSize);
    }

    /**
     * 移除一组数据
     *
     * @param dataList
     */
    public void removeDataList(List<T> dataList) {

        if (dataList != null && !dataList.isEmpty()) {
            this.dataList.removeAll(dataList);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public ArrayList<T> getArrayDataList() {

        if (dataList instanceof ArrayList) {
            return (ArrayList<T>) dataList;
        }
        return dataList == null ? new ArrayList<T>() : new ArrayList<>(dataList);
    }

    /**
     * 获取adapter position
     *
     * @param position content position
     * @return
     */
    private int getFixedPosition(int position) {

        return (isHeaderEnable() ? getHeaderItemCount() : 0) + position;
    }

    private void notifyItemRangeChangedIfNeed(int fromDataPosition) {

        final int contentCount = getContentItemCount();
        if (fromDataPosition != contentCount - 1) {
            notifyItemRangeChanged(getFixedPosition(fromDataPosition), contentCount - fromDataPosition);
        }
    }
}
