package com.zq.widget.ptr.view;

public interface PullToRefreshView<R,L> {

    /**
     * 填充刷新数据
     * @param r data
     */
    void setupRefreshData(R r);

    /**
     * 处理刷新失败
     * @param e e
     */
    void setupRefreshError(Throwable e);

    /**
     * 填充加载更多数据
     * @param l t
     */
    void setupLoadMoreData(L l);

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

    /**
     * 显示加载中view
     */
    void showLoadingView();

    /**
     * 隐藏加载中view
     */
    void hideLoadingView();

    /**
     * 设置是否能刷新
     * @param refreshEnable refreshEnable
     */
    void setRefreshEnable(boolean refreshEnable);
}
