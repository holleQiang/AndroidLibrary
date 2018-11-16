package com.zq.widget.ptr.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.widget.ptr.CellConverter;
import com.zq.widget.ptr.loadmore.LoadMoreWidget;
import com.zq.widget.ptr.refresh.RefreshWidget;

import java.util.Collections;
import java.util.List;

public abstract class BasePullToRefreshView<T> implements PullToRefreshView<T>{

    private RefreshWidget mRefreshWidget;
    private LoadMoreWidget mLoadMoreWidget;
    private CellAdapter mAdapter;
    private CellConverter<T> cellConverter;
    private RecyclerView mRecyclerView;

    public BasePullToRefreshView(@NonNull RecyclerView mRecyclerView,
                                 @NonNull RefreshWidget mRefreshWidget,
                                 @NonNull LoadMoreWidget mLoadMoreWidget,
                                 @NonNull CellConverter<T> cellConverter) {
        this.mRefreshWidget = mRefreshWidget;
        this.mLoadMoreWidget = mLoadMoreWidget;
        this.mRecyclerView = mRecyclerView;
        mAdapter = new CellAdapter(mRecyclerView.getContext());
        this.cellConverter = cellConverter;
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setupRefreshData(T t) {
        List<Cell> cellList = cellConverter.convert(t);
        if(cellList == null || cellList.isEmpty()){
            mAdapter.setDataList(Collections.singletonList(getEmptyCell()));
            return;
        }
        mAdapter.setDataList(cellList);
    }

    @Override
    public void setupRefreshError(Throwable e) {

        if (mAdapter.isEmpty() ) {
            mAdapter.setDataList(Collections.singletonList(getErrorCell(e)));
        }else if(hasFixedCell()){
            mAdapter.removeAll();
            mAdapter.setDataList(Collections.singletonList(getErrorCell(e)));
        }
    }

    private boolean hasFixedCell(){

        int contentItemCount = mAdapter.getContentItemCount();
        for (int i = 0; i < contentItemCount; i++) {
            Cell cell = mAdapter.getDataAt(i);
            if(isFixedCell(cell)){
                return true;
            }
        }
        return false;
    }

    protected boolean isFixedCell(Cell cell){
        return cell != null && (cell.equals(getLoadingCell())
        || cell.equals(getErrorCell(null))
        || cell.equals(getEmptyCell()));
    }

    @Override
    public void setupLoadMoreData(T t) {
        List<Cell> cellList = cellConverter.convert(t);
        if (cellList == null || cellList.isEmpty()) {
            return;
        }
        mAdapter.addDataListAtLast(cellList);
    }

    @Override
    public void setupLoadMoreError(Throwable e) {

    }

    @Override
    public void setLoadMoreComplete() {
        mLoadMoreWidget.setLoadMoreComplete();
    }

    @Override
    public void setRefreshComplete() {
        mRefreshWidget.setRefreshComplete();
    }

    @Override
    public void showLoadingView() {
        mAdapter.removeAll();
        mAdapter.setDataList(Collections.singletonList(getLoadingCell()));
    }

    @Override
    public void hideLoadingView() {
        mAdapter.removeData(getLoadingCell());
    }

    @Override
    public void setRefreshEnable(boolean refreshEnable) {
        mRefreshWidget.setRefreshEnable(refreshEnable);
    }

    protected abstract Cell getLoadingCell();

    protected abstract Cell getEmptyCell();

    protected abstract  Cell getErrorCell(@Nullable Throwable e);

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public CellAdapter getAdapter() {
        return mAdapter;
    }

    public RefreshWidget getRefreshWidget() {
        return mRefreshWidget;
    }

    public LoadMoreWidget getLoadMoreWidget() {
        return mLoadMoreWidget;
    }
}
