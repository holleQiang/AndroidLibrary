package com.zq.widget.ptr;

import android.support.annotation.NonNull;

import com.zq.widget.ptr.view.PullToRefreshView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class PullToRefreshHelper<T> {

    private int mPageIndex;
    private int mStartIndex;
    private final PublishSubject<Boolean> refreshEventSubject = PublishSubject.create();
    private final PublishSubject<Boolean> loadMoreEventSubject = PublishSubject.create();
    private DataSource<T> dataSource;
    private final int mInitPageIndex;
    private final int mPageSize;
    private PullToRefreshView<T> view;

    public PullToRefreshHelper(@NonNull PullToRefreshView<T> view, DataSource<T> sourceFactory) {
        this(view, sourceFactory, 0, 20);
    }

    public PullToRefreshHelper(@NonNull PullToRefreshView<T> view, DataSource<T> dataSource, int initPageIndex, int pageSize) {
        this.view = view;
        this.dataSource = dataSource;
        this.mInitPageIndex = initPageIndex;
        this.mPageSize = pageSize;
    }

    /**
     * 会显示加载中的页面
     */
    public void initRefresh() {

        refreshInternal(true);
    }

    public void refresh() {

        refreshInternal(false);
    }

    /**
     * 刷新
     * @param isInitRefresh 是否初始化刷新
     */
    private void refreshInternal(final boolean isInitRefresh) {

        if (view == null) {
            return;
        }
        stopRefresh();
        stopLoadMore();
        if (isInitRefresh) {
            view.setRefreshEnable(false);
            view.showLoadingView();
        }
        final int pageIndex = mInitPageIndex;
        final int startIndex = 0;
        final int endIndex = startIndex + mPageSize;
        dataSource.createRefreshSource(pageIndex, mPageSize, startIndex, endIndex)
                .takeUntil(refreshEventSubject)
                .subscribe(new Observer<T>() {

                    boolean isComplete;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        //刷新成功
                        mPageIndex = pageIndex;
                        mStartIndex = startIndex;
                        if (isInitRefresh) {

                            view.setRefreshEnable(true);
                            view.hideLoadingView();
                        } else {
                            view.setRefreshComplete();
                        }
                        isComplete = true;
                        view.setupRefreshData(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        isComplete = true;
                        //刷新失败
                        if (isInitRefresh) {
                            view.setRefreshEnable(true);
                            view.hideLoadingView();
                        } else {
                            view.setRefreshComplete();
                        }
                        view.setupRefreshError(e);
                    }

                    @Override
                    public void onComplete() {

                        if (isComplete) {
                            return;
                        }
                        if (isInitRefresh) {
                            view.setRefreshEnable(true);
                            view.hideLoadingView();
                        } else {
                            view.setRefreshComplete();
                        }
                    }
                });
    }

    /**
     * 加载更多
     */
    public void loadMore() {

        stopLoadMore();
        final int pageIndex = mPageIndex + 1;
        final int startIndex = mStartIndex + mPageSize;
        final int endIndex = startIndex + mPageSize;
        dataSource.createLoadMoreSource(pageIndex, mPageSize, startIndex, endIndex)
                .takeUntil(loadMoreEventSubject)
                .takeUntil(refreshEventSubject)
                .subscribe(new Observer<T>() {

                    boolean isComplete;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        isComplete = true;
                        mPageIndex = pageIndex;
                        mStartIndex = startIndex;
                        view.setupLoadMoreData(t);
                        view.setLoadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        isComplete = true;
                        view.setupLoadMoreError(e);
                        view.setLoadMoreComplete();
                    }

                    @Override
                    public void onComplete() {
                        if (!isComplete) {
                            view.setLoadMoreComplete();
                        }
                    }
                });
    }

    public void stopRefresh() {
        refreshEventSubject.onNext(true);
    }

    public void stopLoadMore() {
        refreshEventSubject.onNext(true);
    }
}
