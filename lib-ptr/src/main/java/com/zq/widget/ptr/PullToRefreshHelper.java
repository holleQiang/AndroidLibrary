package com.zq.widget.ptr;

import android.support.annotation.NonNull;

import com.zq.widget.ptr.data.DataSource;
import com.zq.widget.ptr.view.MultiPullToRefreshView;

/**
 * Author：zhangqiang
 * Date：2019/1/15 20:10:27
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public class PullToRefreshHelper<T> extends MultiPullToRefreshHelper<T,T> {

    public PullToRefreshHelper(@NonNull MultiPullToRefreshView<T, T> view, @NonNull DataSource<T> refreshDataSource) {
        super(view, refreshDataSource, refreshDataSource);
    }

    public PullToRefreshHelper(@NonNull MultiPullToRefreshView<T, T> view, @NonNull DataSource<T> refreshDataSource, int initPageIndex, int pageSize) {
        super(view, refreshDataSource, refreshDataSource, initPageIndex, pageSize);
    }

    public PullToRefreshHelper(@NonNull MultiPullToRefreshView<T, T> view, @NonNull DataSource<T> refreshDataSource, @NonNull DataSource<T> loadMoreDataSource) {
        super(view, refreshDataSource, loadMoreDataSource);
    }

    public PullToRefreshHelper(@NonNull MultiPullToRefreshView<T, T> view, @NonNull DataSource<T> refreshDataSource, @NonNull DataSource<T> loadMoreDataSource, int initPageIndex, int pageSize) {
        super(view, refreshDataSource, loadMoreDataSource, initPageIndex, pageSize);
    }
}
