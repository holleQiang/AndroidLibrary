package com.zq.widget.ptr.loadmore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.loadmore.LoadMoreHelper;
import com.zq.view.recyclerview.loadmore.LoadMoreListener;

/**
 * Author：zhangqiang
 * Date：2019/1/15 10:58:27
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public abstract class CellLoadMoreWidget implements LoadMoreWidget {
    private final Cell mLoadMoreCell;
    private LoadMoreHelper loadMoreHelper;
    private RecyclerView mRecyclerView;
    private OnLoadMoreListener onLoadMoreListener;

    @NonNull
    protected abstract Cell onCreateLoadMoreCell();

    protected abstract void updateCellWhenError(Cell cell, Throwable e);

    protected abstract void initLoadMoreCell(Cell loadMoreCell);

    public CellLoadMoreWidget(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        loadMoreHelper = new LoadMoreHelper(mRecyclerView);
        mLoadMoreCell = onCreateLoadMoreCell();
    }

    @Override
    public void setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        loadMoreHelper.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                callLoadMore();
            }
        });
    }

    protected void callLoadMore() {
        showLoadMoreCell();
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public void setLoadMoreComplete() {
        loadMoreHelper.finishLoadMore();
        hideLoadMoreCell();
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        loadMoreHelper.setLoadMoreEnable(enable);
    }

    @Override
    public final void setupLoadMoreError(Throwable e) {
        loadMoreHelper.finishLoadMore();
        updateCellWhenError(mLoadMoreCell, e);
    }

    private void showLoadMoreCell() {

        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof CellAdapter) {
            CellAdapter cellAdapter = (CellAdapter) adapter;
            if (cellAdapter.findPositionOfCell(mLoadMoreCell) < 0) {
                cellAdapter.addDataAtLast(mLoadMoreCell);
            }
        }
        initLoadMoreCell(mLoadMoreCell);
    }


    private void hideLoadMoreCell() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof CellAdapter) {

            CellAdapter cellAdapter = ((CellAdapter) adapter);
            cellAdapter.removeData(mLoadMoreCell);
        }
    }

    private void hideLoadMoreCellDelay(long delayMillis) {
        mRecyclerView.postDelayed(hideRunnable, delayMillis);
    }

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hideLoadMoreCell();
        }
    };

    public Cell getLoadMoreCell() {
        return mLoadMoreCell;
    }


}
