package com.zq.widget.ptr.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;
import com.zq.view.recyclerview.viewholder.RVViewHolder;
import com.zq.widget.ptr.R;
import com.zq.widget.ptr.loadmore.SampleLoadMoreWidget;
import com.zq.widget.ptr.refresh.SwipeRefreshWidget;

public class SamplePullToRefreshView<T,B> extends BasePullToRefreshView<T, B> {


    public SamplePullToRefreshView(@NonNull RecyclerView mRecyclerView,
                                   @NonNull SwipeRefreshLayout swipeRefreshLayout,
                                   @NonNull CellConverter<T> refreshCellConverter,
                                   @NonNull CellConverter<B> loadMoreCellConverter) {
        super(mRecyclerView,
                new SwipeRefreshWidget(swipeRefreshLayout),
                new SampleLoadMoreWidget(mRecyclerView),
                refreshCellConverter,
                loadMoreCellConverter);
        setLoadingCell(MultiCell.convert(R.layout.view_loading, Cell.FULL_SPAN,"", null));
        setErrorCell(MultiCell.convert(R.layout.view_error,Cell.FULL_SPAN, "", new DataBinder<String>() {
            @Override
            public void bindData(RVViewHolder viewHolder, String data) {
                viewHolder.setText(R.id.tv_error, data);
            }
        }));
        setEmptyCell(MultiCell.convert(R.layout.view_empty, Cell.FULL_SPAN,"", null));
    }

    @Override
    protected  void onShowErrorCell(@NonNull Cell errorCell, @Nullable Throwable e) {
        if (e != null) {
            MultiCell<String> mErrorCell = (MultiCell<String>) errorCell;
            mErrorCell.setData(e.getMessage());
        }
    }

    @Override
    protected void setupUnhandledRefreshError(@Nullable Throwable e) {
        Toast.makeText(getRecyclerView().getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
    }

}
