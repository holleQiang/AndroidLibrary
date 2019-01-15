package com.zq.view.recyclerview.loadmore;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Author：zhangqiang
 * Date：2019/1/14 21:35:29
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public class LoadMoreHelper {

    private boolean isLoadingMore = false;
    private boolean isLoadMoreEnable;
    private LoadMoreListener mLoadMoreListener;
    private RecyclerView mRecyclerView;

    public LoadMoreHelper(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        recyclerView.addOnScrollListener(new LoadMoreHelper.InternalScrollerListener());
    }

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        isLoadMoreEnable = loadMoreEnable;
    }


    private class InternalScrollerListener extends RecyclerView.OnScrollListener {

        private int mLastPosition;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (isLoadingMore || !isLoadMoreEnable || dy <= 0 || mLoadMoreListener == null) {
                return;
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager == null) {
                return;
            }
            int totalItemCount = layoutManager.getItemCount();
            int visibleItemCount = layoutManager.getChildCount();
            if (totalItemCount <= visibleItemCount) {
                return;
            }
            int lastVisibleItem = findLastVisibleItem(layoutManager);

            if (mLastPosition != lastVisibleItem && totalItemCount - 1 == lastVisibleItem) {

                Log.i("Test","=========post=========" + mLastPosition);
                isLoadingMore = true;
                recyclerView.removeCallbacks(loadMoreRunnable);
                recyclerView.post(loadMoreRunnable);
            }
            mLastPosition = lastVisibleItem;
        }
    }

    private int findLastVisibleItem(RecyclerView.LayoutManager layoutManager) {

        if (layoutManager instanceof LinearLayoutManager) {

            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

            int[] info = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(info);
            return findMax(info);
        } else {

            throw new IllegalArgumentException("unSupport layoutManager : " + layoutManager);
        }
    }

    private int findMax(int[] positions) {

        int max = Integer.MIN_VALUE;
        for (int a :
                positions) {

            if (a > max) {
                max = a;
            }
        }
        return max;
    }

    public void finishLoadMore() {

        this.isLoadingMore = false;
        mRecyclerView.removeCallbacks(loadMoreRunnable);
    }

    private Runnable loadMoreRunnable = new Runnable() {
        @Override
        public void run() {

            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }
        }
    };

    public LoadMoreHelper setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
        return this;
    }
}
