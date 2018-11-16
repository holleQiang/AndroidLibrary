package com.zq.widget.ptr.refresh;

public interface RefreshWidget {

    void setOnRefreshListener(OnRefreshListener onRefreshListener);

    void setRefreshComplete();

    void setRefreshEnable(boolean refreshEnable);

    interface OnRefreshListener{

        void onRefresh();
    }
}
