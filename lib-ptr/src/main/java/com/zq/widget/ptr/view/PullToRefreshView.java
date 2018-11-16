package com.zq.widget.ptr.view;

public interface PullToRefreshView<T> {

    /**
     * 填充刷新数据
     * @param t data
     */
    void setupRefreshData(T t);

    /**
     * 处理刷新失败
     * @param e e
     */
    void setupRefreshError(Throwable e);

    /**
     * 填充加载更多数据
     * @param t t
     */
    void setupLoadMoreData(T t);

    /**
     * 处理加载更多失败
     * @param e e
     */
    void setupLoadMoreError(Throwable e);

    /**
     * 加载更多成功
     */
    void setLoadMoreComplete();

    /**
     * 刷新成功
     */
    void setRefreshComplete();

    void showLoadingView();

    void hideLoadingView();

    void setRefreshEnable(boolean refreshEnable);
}
