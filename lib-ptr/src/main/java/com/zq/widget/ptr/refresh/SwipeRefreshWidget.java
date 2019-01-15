package com.zq.widget.ptr.refresh;

import android.support.v4.widget.SwipeRefreshLayout;

public class SwipeRefreshWidget implements RefreshWidget {

    private SwipeRefreshLayout swipeRefreshLayout;

    public SwipeRefreshWidget(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshListener.onRefresh();
            }
        });
    }

    @Override
    public void setRefreshComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setRefreshEnable(boolean refreshEnable) {
        swipeRefreshLayout.setEnabled(refreshEnable);
    }

}
