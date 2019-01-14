package com.zq.widget.ptr.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zq.widget.ptr.CellConverter;
import com.zq.widget.ptr.loadmore.LoadMoreWidget;
import com.zq.widget.ptr.refresh.RefreshWidget;

/**
 * Author：zhangqiang
 * Date：2019/1/14 19:02:24
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public abstract class SimplePullToRefreshView<T> extends BasePullToRefreshView<T,T> {

    public SimplePullToRefreshView(@NonNull RecyclerView mRecyclerView, @NonNull RefreshWidget mRefreshWidget, @NonNull LoadMoreWidget mLoadMoreWidget, @NonNull CellConverter<T> cellConverter) {
        super(mRecyclerView, mRefreshWidget, mLoadMoreWidget, cellConverter, cellConverter);
    }
}
