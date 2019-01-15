package com.zq.widget.ptr.loadmore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.loadmore.LoadMoreHelper;
import com.zq.view.recyclerview.loadmore.LoadMoreListener;
import com.zq.view.recyclerview.viewholder.RVViewHolder;
import com.zq.widget.ptr.R;

public class SampleLoadMoreWidget extends CellLoadMoreWidget {

    public static final int STATE_LOADING = 0;
    public static final int STATE_ERROR = 2;

    public SampleLoadMoreWidget(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    Cell onCreateLoadMoreCell() {
        return new MultiCell<>(R.layout.view_load_more, STATE_LOADING, new DataBinder<Integer>() {
            @Override
            public void bindData(final RVViewHolder viewHolder, Integer data) {
                if (STATE_ERROR == data) {
                    viewHolder.setVisibility(R.id.tv_state, View.VISIBLE);
                    viewHolder.setVisibility(R.id.pb_loading, View.GONE);
                    viewHolder.setText(R.id.tv_state, "加载失败，点击重新加载");
                    viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callLoadMore();
                        }
                    });
                } else {
                    viewHolder.setVisibility(R.id.tv_state, View.GONE);
                    viewHolder.setVisibility(R.id.pb_loading, View.VISIBLE);
                    viewHolder.getView().setOnClickListener(null);
                }
            }
        });
    }

    @Override
    protected void updateCellWhenError(Cell cell, Throwable e) {
        MultiCell<Integer> loadMoreCell = (MultiCell<Integer>) cell;
        loadMoreCell.setData(STATE_ERROR);
        loadMoreCell.notifyCellChange();
    }

    @Override
    protected void onShowLoadMoreCell(Cell loadMoreCell) {
        super.onShowLoadMoreCell(loadMoreCell);
        setStateLoading();
    }

    private void setStateLoading(){
        MultiCell<Integer> loadMoreCell = (MultiCell<Integer>) getLoadMoreCell();
        loadMoreCell.setData(STATE_LOADING);
        loadMoreCell.notifyCellChange();
    }
}