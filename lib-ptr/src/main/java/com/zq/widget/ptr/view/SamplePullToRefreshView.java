package com.zq.widget.ptr.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.viewholder.RVViewHolder;
import com.zq.widget.ptr.CellConverter;
import com.zq.widget.ptr.R;
import com.zq.widget.ptr.loadmore.SampleLoadMoreWidget;
import com.zq.widget.ptr.refresh.SwipeRefreshWidget;

public class SamplePullToRefreshView<T> extends BasePullToRefreshView<T> {

    private final Cell LOADING_CELL = MultiCell.convert(R.layout.view_loading, Cell.FULL_SPAN,"", null);
    private final Cell EMPTY_CELL = MultiCell.convert(R.layout.view_empty, Cell.FULL_SPAN,"", null);
    private final MultiCell<String> ERROR_CELL = MultiCell.convert(R.layout.view_error,Cell.FULL_SPAN, "", new DataBinder<String>() {
        @Override
        public void bindData(RVViewHolder viewHolder, String data) {
            viewHolder.setText(R.id.tv_error, data);
        }
    });

    public SamplePullToRefreshView(@NonNull RecyclerView mRecyclerView, @NonNull SwipeRefreshLayout swipeRefreshLayout, @NonNull CellConverter<T> cellConverter) {
        super(mRecyclerView, new SwipeRefreshWidget(swipeRefreshLayout), new SampleLoadMoreWidget(mRecyclerView), cellConverter);
    }

    @Override
    protected Cell getLoadingCell() {
        return LOADING_CELL;
    }

    @Override
    protected Cell getEmptyCell() {
        return EMPTY_CELL;
    }

    @Override
    protected Cell getErrorCell(@Nullable Throwable e) {
        if (e != null) {
            ERROR_CELL.setData(e.getMessage());
        }
        return ERROR_CELL;
    }
}
