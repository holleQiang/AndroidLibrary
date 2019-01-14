package com.zq.widget.ptr.loadmore;

import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.loadmore.LoadMoreHelper;
import com.zq.view.recyclerview.loadmore.LoadMoreListener;
import com.zq.widget.ptr.R;

public class SampleLoadMoreWidget implements LoadMoreWidget {

    private final MultiCell<Throwable> LOAD_MORE_CELL = new MultiCell<>(R.layout.view_load_more, null, null);
    private LoadMoreHelper loadMoreHelper;
    private RecyclerView mRecyclerView;

    public SampleLoadMoreWidget(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        loadMoreHelper = new LoadMoreHelper(mRecyclerView);
    }

    @Override
    public void setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
        loadMoreHelper.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {

                showLoadMoreView();
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }
            }
        });
    }

    private void showLoadMoreView() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof CellAdapter) {

            CellAdapter cellAdapter = ((CellAdapter) adapter);
            cellAdapter.addDataAtLast(LOAD_MORE_CELL);
        }
    }

    @Override
    public void setLoadMoreComplete() {
        loadMoreHelper.finishLoadMore();
        hideLoadMoreView();
    }

    private void hideLoadMoreView() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof CellAdapter) {

            CellAdapter cellAdapter = ((CellAdapter) adapter);
            cellAdapter.removeData(LOAD_MORE_CELL);
        }
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        loadMoreHelper.setLoadMoreEnable(enable);
    }
}