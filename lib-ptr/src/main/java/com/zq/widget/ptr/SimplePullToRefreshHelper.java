package com.zq.widget.ptr;

import android.support.annotation.NonNull;

import com.zq.widget.ptr.data.DataSource;
import com.zq.widget.ptr.view.PullToRefreshView;

public class SimplePullToRefreshHelper<T> extends PullToRefreshHelper<T,T>{

    public SimplePullToRefreshHelper(@NonNull PullToRefreshView<T, T> view, @NonNull DataSource<T> dataSource) {
        this(view, dataSource, dataSource);
    }

    public SimplePullToRefreshHelper(@NonNull PullToRefreshView<T, T> view, @NonNull DataSource<T> refreshDataSource, @NonNull DataSource<T> loadMoreDataSource) {
        super(view, refreshDataSource, loadMoreDataSource);
    }

    public SimplePullToRefreshHelper(@NonNull PullToRefreshView<T, T> view, @NonNull DataSource<T> refreshDataSource, @NonNull DataSource<T> loadMoreDataSource, int initPageIndex, int pageSize) {
        super(view, refreshDataSource, loadMoreDataSource, initPageIndex, pageSize);
    }
}
