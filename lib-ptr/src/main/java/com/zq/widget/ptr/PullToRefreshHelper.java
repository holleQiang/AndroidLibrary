package com.zq.widget.ptr;

import android.support.annotation.NonNull;

import com.zq.widget.ptr.view.PullToRefreshView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class PullToRefreshHelper<T> {

    private int mPageIndex;
    private int mStartIndex;
    private final PublishSubject<Boolean> refreshEventSubject = PublishSubject.create();
    private final PublishSubject<Boolean> loadMoreEventSubject = PublishSubject.create();
    private SourceFactory<T> sourceFactory;
    private final int mInitPageIndex;
    private final int mPageSize;
    private PullToRefreshView<T> view;

    public PullToRefreshHelper(@NonNull PullToRefreshView<T> view, SourceFactory<T> sourceFactory) {
        this(view, sourceFactory, 0, 20);
    }

    public PullToRefreshHelper(@NonNull PullToRefreshView<T> view, SourceFactory<T> sourceFactory, int initPageIndex, int pageSize) {
        this.view = view;
        this.sourceFactory = sourceFactory;
        this.mInitPageIndex = initPageIndex;
        this.mPageSize = pageSize;
    }

    public void init() {

        refresh(true);
    }

    public void refresh() {

        refresh(false);
    }

    private void refresh(final boolean isInit) {

        if (view == null) {
            return;
        }
        if (isInit) {
            view.setRefreshEnable(false);
            view.showLoadingView();
        }

        refreshEventSubject.onNext(true);
        final int pageIndex = mInitPageIndex;
        final int startIndex = 0;
        final int endIndex = startIndex + mPageSize;
        sourceFactory.createRefreshSource(pageIndex, mPageSize, startIndex, endIndex)
                .takeUntil(refreshEventSubject)
                .subscribe(new Observer<T>() {

                    //防止发射空数据
                    boolean isDone;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        isDone = true;
                        if (isInit) {
                            view.setRefreshEnable(true);
                            view.hideLoadingView();
                        }
                        mPageIndex = pageIndex;
                        mStartIndex = startIndex;
                        view.setupRefreshData(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        isDone = true;
                        if (isInit) {
                            view.setRefreshEnable(true);
                            view.hideLoadingView();
                        }
                        view.setupRefreshError(e);
                    }

                    @Override
                    public void onComplete() {
                        if (!isDone && isInit) {
                            view.setRefreshEnable(true);
                            view.hideLoadingView();
                        }
                        view.setRefreshComplete();
                    }
                });
    }

    public void loadMore() {

        loadMoreEventSubject.onNext(true);
        final int pageIndex = mPageIndex + 1;
        final int startIndex = mStartIndex + mPageSize;
        final int endIndex = startIndex + mPageSize;
        sourceFactory.createLoadMoreSource(pageIndex, mPageSize, startIndex, endIndex)
                .takeUntil(loadMoreEventSubject)
                .takeUntil(refreshEventSubject)
                .subscribe(new Observer<T>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        mPageIndex = pageIndex;
                        mStartIndex = startIndex;
                        view.setupLoadMoreData(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setupLoadMoreError(e);
                    }

                    @Override
                    public void onComplete() {
                        view.setLoadMoreComplete();
                    }
                });
    }

    public PullToRefreshView<T> getView() {
        return view;
    }
}
