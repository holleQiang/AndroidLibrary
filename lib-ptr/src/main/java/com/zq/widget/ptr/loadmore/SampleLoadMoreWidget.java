package com.zq.widget.ptr.loadmore;

import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.RVLoadMoreHelper;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.widget.ptr.R;

public class SampleLoadMoreWidget implements RVLoadMoreHelper.LoadMoreController,LoadMoreWidget {

    private final MultiCell<Throwable> LOAD_MORE_CELL = new MultiCell<>(R.layout.view_load_more, null, null);
    private RVLoadMoreHelper rvLoadMoreHelper;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView mRecyclerView;

    public SampleLoadMoreWidget(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        rvLoadMoreHelper = new RVLoadMoreHelper(mRecyclerView,this);
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void setLoadMoreComplete() {
        rvLoadMoreHelper.setLoadMoreComplete();
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        rvLoadMoreHelper.setLoadMoreEnable(enable);
    }

    @Override
    public void shouldShowLoadMoreView() {
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof CellAdapter) {

            CellAdapter cellAdapter = ((CellAdapter) adapter);
            cellAdapter.addDataAtLast(LOAD_MORE_CELL);
        }
    }

    @Override
    public void shouldHideLoadMoreView() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof CellAdapter) {

            CellAdapter cellAdapter = ((CellAdapter) adapter);
            cellAdapter.removeData(LOAD_MORE_CELL);
        }
    }
}