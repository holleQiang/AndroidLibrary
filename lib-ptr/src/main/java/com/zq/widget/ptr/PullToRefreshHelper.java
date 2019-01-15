package com.zq.widget.ptr;

import android.support.annotation.NonNull;

import com.zq.widget.ptr.data.Callback;
import com.zq.widget.ptr.data.DataSource;
import com.zq.widget.ptr.view.PullToRefreshView;

/**
 * Author：zhangqiang
 * Date：2019/1/12 17:29:41
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public class PullToRefreshHelper<T, B> {

    private int mPageIndex;
    private int mStartIndex;
    private DataSource<T> refreshDataSource;
    private DataSource<B> loadMoreDataSource;
    private final int mInitPageIndex;
    private final int mPageSize;
    @NonNull
    private PullToRefreshView<T, B> view;

    public PullToRefreshHelper(@NonNull PullToRefreshView<T, B> view,
                               @NonNull DataSource<T> refreshDataSource,
                               @NonNull DataSource<B> loadMoreDataSource) {
        this(view, refreshDataSource, loadMoreDataSource, 0, 20);
    }

    public PullToRefreshHelper(@NonNull PullToRefreshView<T, B> view,
                               @NonNull DataSource<T> refreshDataSource,
                               @NonNull DataSource<B> loadMoreDataSource,
                               int initPageIndex,
                               int pageSize) {
        this.view = view;
        this.refreshDataSource = refreshDataSource;
        this.loadMoreDataSource = loadMoreDataSource;
        this.mInitPageIndex = initPageIndex;
        this.mPageSize = pageSize;
    }

    /**
     * 会显示加载中的页面
     */
    public void initRefresh() {

        stopRefresh();
        stopLoadMore();
        view.setRefreshEnable(false);
        view.showLoadingView();
        final int pageIndex = mInitPageIndex;
        final int startIndex = 0;
        final int endIndex = startIndex + mPageSize;
        mPageIndex = pageIndex;
        mStartIndex = startIndex;
        refreshDataSource.loadData(pageIndex, mPageSize, startIndex, endIndex, new InternalCallback<>(new Callback<T>() {

            @Override
            public void onNextData(@NonNull T t) {
                view.setupRefreshData(t);
            }

            @Override
            public void onError(Throwable e) {
                //刷新失败
                view.setupRefreshError(e);
                view.setRefreshEnable(true);
                view.hideLoadingView();
            }

            @Override
            public void onComplete() {
                view.setRefreshEnable(true);
                view.hideLoadingView();
            }
        }));
    }

    public void refresh() {

        stopRefresh();
        stopLoadMore();
        final int pageIndex = mInitPageIndex;
        final int startIndex = 0;
        final int endIndex = startIndex + mPageSize;
        refreshDataSource.loadData(pageIndex, mPageSize, startIndex, endIndex, new InternalCallback<>(new Callback<T>() {

            @Override
            public void onNextData(@NonNull T t) {
                view.setupRefreshData(t);
            }

            @Override
            public void onError(Throwable e) {
                //刷新失败
                view.setupRefreshError(e);
            }

            @Override
            public void onComplete() {
                //刷新成功
                mPageIndex = pageIndex;
                mStartIndex = startIndex;
                view.setRefreshComplete();
            }
        }));
    }

    /**
     * 加载更多
     */
    public void loadMore() {

        stopLoadMore();
        final int pageIndex = mPageIndex + 1;
        final int startIndex = mStartIndex + mPageSize;
        final int endIndex = startIndex + mPageSize;
        loadMoreDataSource.loadData(pageIndex, mPageSize, startIndex, endIndex, new InternalCallback<>(new Callback<B>() {
            @Override
            public void onNextData(@NonNull B b) {
                view.setupLoadMoreData(b);
            }

            @Override
            public void onComplete() {
                mPageIndex = pageIndex;
                mStartIndex = startIndex;
                view.setLoadMoreComplete();
            }

            @Override
            public void onError(Throwable e) {
                view.setupLoadMoreError(e);
            }
        }));
    }

    public void stopRefresh() {
        refreshDataSource.dispose();
    }

    public void stopLoadMore() {
        loadMoreDataSource.dispose();
    }

    /**
     * 对callback进行过滤，如果完成不再接受数据和错误，如果错误，不再接受数据和完成
     *
     * @param <T>
     */
    private static class InternalCallback<T> implements Callback<T> {

        private Callback<T> delegate;
        private boolean isError;
        private boolean isComplete;

        InternalCallback(Callback<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void onNextData(@NonNull T t) {
            if (isError || isComplete) {
                return;
            }
            if (delegate != null) {
                delegate.onNextData(t);
            }
        }

        @Override
        public void onComplete() {
            if (isError || isComplete) {
                return;
            }
            isComplete = true;

            if (delegate != null) {
                delegate.onComplete();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (isComplete || isError) {
                return;
            }
            isError = true;
            if (delegate != null) {
                delegate.onError(e);
            }
        }
    }
}
