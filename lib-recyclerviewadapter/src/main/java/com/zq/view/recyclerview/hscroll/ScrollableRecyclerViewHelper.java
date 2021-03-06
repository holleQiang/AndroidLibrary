package com.zq.view.recyclerview.hscroll;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.BaseCell;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.OnAttachStateChangeListener;
import com.zq.view.recyclerview.hscroll.controller.HorizontalScrollController;
import com.zq.view.recyclerview.hscroll.controller.VerticalScrollController;

import java.util.List;

/**
 * 使RecyclerView嵌套的recyclerView同步滚动的辅助类
 * Created by zhangqiang on 2017/10/20.
 */

public class ScrollableRecyclerViewHelper {

    private RecyclerView recyclerView;
    private OnAttachStateChangeListener stateChangeListener;
    private DragHorizontalOnItemTouchListener onItemTouchListener;
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
     * 构造方法
     *
     * @param recyclerView               父recyclerView
     * @param verticalScrollController   纵向滚动控制器
     * @param horizontalScrollController 横向滚动控制器
     */
    public ScrollableRecyclerViewHelper(@NonNull RecyclerView recyclerView,
                                        @Nullable VerticalScrollController verticalScrollController,
                                        @Nullable HorizontalScrollController horizontalScrollController) {
        this.recyclerView = recyclerView;
        stateChangeListener = new RecyclerViewScrollListener(verticalScrollController);
        onItemTouchListener = new DragHorizontalOnItemTouchListener(recyclerView, horizontalScrollController);

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || !(adapter instanceof CellAdapter)) {
            throw new NullPointerException("recyclerView must have a cellAdapter");
        }
        adapter.registerAdapterDataObserver(dataObserver);
    }

    /**
     * 和RecyclerView绑定
     */
    public void attachToTarget() {

        CellAdapter cellAdapter = (CellAdapter) recyclerView.getAdapter();
        if (cellAdapter == null || cellAdapter.isEmpty()) {
            return;
        }

        recyclerView.removeOnItemTouchListener(onItemTouchListener);
        recyclerView.addOnItemTouchListener(onItemTouchListener);

        List<Cell> cellList = cellAdapter.getDataList();
        for (Cell cell :
                cellList) {

            cell.addOnAttachStateChangeListener(stateChangeListener);
        }
        if (cellAdapter.isHeaderEnable()) {
            final int headerCount = cellAdapter.getHeaderItemCount();
            for (int i = 0; i < headerCount; i++) {

                Cell headerCell = cellAdapter.getHeaderCellAt(i);
                headerCell.addOnAttachStateChangeListener(stateChangeListener);
            }
        }
        if (cellAdapter.isFooterEnable()) {
            final int footerCount = cellAdapter.getFooterItemCount();
            for (int i = 0; i < footerCount; i++) {

                Cell footerCell = cellAdapter.getFooterCellAt(i);
                footerCell.addOnAttachStateChangeListener(stateChangeListener);
            }
        }
    }

    /**
     * 和父RecyclerView分离 滚动失效
     */
    public void detachFromTarget() {

        recyclerView.removeOnItemTouchListener(onItemTouchListener);

        CellAdapter cellAdapter = (CellAdapter) recyclerView.getAdapter();
        if (cellAdapter == null || cellAdapter.isEmpty()) {
            return;
        }
        cellAdapter.unregisterAdapterDataObserver(dataObserver);
        List<Cell> cellList = cellAdapter.getDataList();
        for (Cell cell :
                cellList) {

            BaseCell baseCell = (BaseCell) cell;
            baseCell.removeOnAttachStateChangeListener(stateChangeListener);
        }
    }

}
