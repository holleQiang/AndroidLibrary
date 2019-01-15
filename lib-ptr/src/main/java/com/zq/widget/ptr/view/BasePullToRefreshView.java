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

/**
 * 下拉刷新view基类
 */
public abstract class BasePullToRefreshView<R, L> implements PullToRefreshView<R, L> {

    private RefreshWidget mRefreshWidget;
    private LoadMoreWidget mLoadMoreWidget;
    private CellAdapter mAdapter;
    private CellConverter<R> refreshCellConverter;
    private CellConverter<L> loadMoreCellConverter;
    private RecyclerView mRecyclerView;
    @Nullable
    private  Cell mLoadingCell;
    @Nullable
    private  Cell mEmptyCell;
    @Nullable
    private  Cell mErrorCell;


    public BasePullToRefreshView(@NonNull RecyclerView mRecyclerView,
                                 @NonNull RefreshWidget mRefreshWidget,
                                 @NonNull LoadMoreWidget mLoadMoreWidget,
                                 @NonNull CellConverter<R> refreshCellConverter,
                                 @NonNull CellConverter<L> loadMoreCellConverter) {
        this.mRefreshWidget = mRefreshWidget;
        this.mLoadMoreWidget = mLoadMoreWidget;
        this.mRecyclerView = mRecyclerView;
        mAdapter = new CellAdapter(mRecyclerView.getContext());
        this.refreshCellConverter = refreshCellConverter;
        this.loadMoreCellConverter = loadMoreCellConverter;
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void setupRefreshData(R r) {
        List<Cell> cellList = refreshCellConverter.convert(r);
        if (cellList == null || cellList.isEmpty()) {
            //如果刷新的数据为空，提示没数据
            Cell emptyCell = getEmptyCell();
            if (emptyCell == null) {
                mAdapter.removeAll();
            } else {
                onShowEmptyCell(emptyCell);
                mAdapter.setDataList(Collections.singletonList(emptyCell));
            }
            return;
        }
        mAdapter.setDataList(cellList);
    }

    @Override
    public final void setupRefreshError(Throwable e) {
        mRefreshWidget.setRefreshComplete();
        if (onInterceptRefreshError(e)) {
            return;
        }
        if (mAdapter.isEmpty() || hasFixedCell()) {
            Cell errorCell = getErrorCell();
            if (errorCell != null) {
                onShowErrorCell(errorCell, e);
                mAdapter.setDataList(Collections.singletonList(errorCell));
            } else {
                setupUnhandledRefreshError(e);
            }
        } else {
            setupUnhandledRefreshError(e);
        }
    }



    @Override
    public void setupLoadMoreData(L l) {
        List<Cell> cellList = loadMoreCellConverter.convert(l);
        if (cellList == null || cellList.isEmpty()) {
            return;
        }
        mAdapter.addDataListAtLast(cellList);
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
        Cell loadingCell = getLoadingCell();
        if (loadingCell == null) {
            return;
        }
        onShowLoadingCell(loadingCell);
        mAdapter.setDataList(Collections.singletonList(loadingCell));
    }

    @Override
    public void hideLoadingView() {
        Cell loadingCell = getLoadingCell();
        if (loadingCell != null) {
            mAdapter.removeData(loadingCell);
        }
    }

    @Override
    public void setupLoadMoreError(Throwable e) {
        mLoadMoreWidget.setupLoadMoreError(e);
    }

    @Override
    public void setRefreshEnable(boolean refreshEnable) {
        mRefreshWidget.setRefreshEnable(refreshEnable);
    }

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

    @Nullable
    public Cell getLoadingCell() {
        return mLoadingCell;
    }

    @Nullable
    public Cell getEmptyCell() {
        return mEmptyCell;
    }

    @Nullable
    public Cell getErrorCell() {
        return mErrorCell;
    }

    public BasePullToRefreshView<R, L> setLoadingCell(@Nullable Cell mLoadingCell) {
        this.mLoadingCell = mLoadingCell;
        return this;
    }

    public BasePullToRefreshView<R, L> setEmptyCell(@Nullable Cell mEmptyCell) {
        this.mEmptyCell = mEmptyCell;
        return this;
    }

    public BasePullToRefreshView<R, L> setErrorCell(@Nullable Cell mErrorCell) {
        this.mErrorCell = mErrorCell;
        return this;
    }

    private boolean hasFixedCell() {
        int contentItemCount = mAdapter.getContentItemCount();
        for (int i = 0; i < contentItemCount; i++) {
            Cell cell = mAdapter.getDataAt(i);
            if (isFixedCell(cell)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFixedCell(Cell cell) {
        return cell != null && (cell.equals(getLoadingCell())
                || cell.equals(getErrorCell())
                || cell.equals(getEmptyCell()));
    }

    protected boolean onInterceptRefreshError(@Nullable Throwable e) {
        return false;
    }

    protected void onShowErrorCell(@NonNull Cell errorCell, @Nullable Throwable e) {

    }

    protected void onShowEmptyCell(@NonNull Cell emptyCell) {

    }

    protected void onShowLoadingCell(@NonNull Cell loadingCell) {

    }

    protected void setupUnhandledRefreshError(@Nullable Throwable e) {

    }
}
