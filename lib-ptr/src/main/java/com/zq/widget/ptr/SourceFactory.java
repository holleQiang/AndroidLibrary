package com.zq.widget.ptr;

import io.reactivex.Observable;

public abstract class SourceFactory<T> {

    public abstract Observable<T> createRefreshSource(int pageIndex, int pageSize, int startIndex, int endIndex);

    public Observable<T> createLoadMoreSource(int pageIndex, int pageSize, int startIndex, int endIndex) {
        return createRefreshSource(pageIndex, pageSize, startIndex, endIndex);
    }
}