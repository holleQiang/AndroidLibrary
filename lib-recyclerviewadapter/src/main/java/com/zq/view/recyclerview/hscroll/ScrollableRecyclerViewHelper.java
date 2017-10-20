package com.zq.view.recyclerview.hscroll;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.BaseCell;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.List;

/**
 * 使RecyclerView嵌套的recyclerView同步滚动的辅助类
 * Created by zhangqiang on 2017/10/20.
 */

public class ScrollableRecyclerViewHelper {

    private RecyclerView recyclerView;
    private BaseCell.OnAttachStateChangeListener stateChangeListener;
    private DraggedHorizontalOnItemTouchListener onItemTouchListener;
    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            attachToTarget();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            attachToTarget();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            attachToTarget();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            attachToTarget();
        }
    };

    /**
     *构造方法
     * @param recyclerView 父recyclerView
     * @param recyclerViewGetter 子recyclerView获取接口
     */
    public ScrollableRecyclerViewHelper(@NonNull RecyclerView recyclerView, @NonNull ScrollableRecyclerViewGetter<RVViewHolder> recyclerViewGetter) {
        this.recyclerView = recyclerView;
        stateChangeListener = new RecyclerViewScrollListener(recyclerViewGetter);
        onItemTouchListener = new DraggedHorizontalOnItemTouchListener(recyclerView,recyclerViewGetter);

        CellAdapter cellAdapter = (CellAdapter) recyclerView.getAdapter();
        if(cellAdapter == null){
            throw new NullPointerException("recyclerView must have a cellAdapter");
        }
        cellAdapter.registerAdapterDataObserver(dataObserver);
    }

    /**
     * 和RecyclerView绑定
     */
    public void  attachToTarget(){

        CellAdapter cellAdapter = (CellAdapter) recyclerView.getAdapter();
        if(cellAdapter == null || cellAdapter.isEmpty()){
            return;
        }

        recyclerView.removeOnItemTouchListener(onItemTouchListener);
        recyclerView.addOnItemTouchListener(onItemTouchListener);

        List<Cell> cellList = cellAdapter.getDataList();
        for (Cell cell:
             cellList) {

            BaseCell baseCell = (BaseCell) cell;
            baseCell.addOnAttachStateChangeListener(stateChangeListener);
        }
    }

    /**
     * 和父RecyclerView分离 滚动失效
     */
    public void detachFromTarget(){

        recyclerView.removeOnItemTouchListener(onItemTouchListener);

        CellAdapter cellAdapter = (CellAdapter) recyclerView.getAdapter();
        if(cellAdapter == null || cellAdapter.isEmpty()){
            return;
        }
        cellAdapter.unregisterAdapterDataObserver(dataObserver);
        List<Cell> cellList = cellAdapter.getDataList();
        for (Cell cell:
                cellList) {

            BaseCell baseCell = (BaseCell) cell;
            baseCell.removeOnAttachStateChangeListener(stateChangeListener);
        }
    }
}
