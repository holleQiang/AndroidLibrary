package com.zq.widget.ptr.loadmore;

public interface LoadMoreWidget {

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);

    void setLoadMoreComplete();

    void setLoadMoreEnable(boolean enable);

    void setupLoadMoreError(Throwable e);

    interface OnLoadMoreListener{

        void onLoadMore();
    }
}
