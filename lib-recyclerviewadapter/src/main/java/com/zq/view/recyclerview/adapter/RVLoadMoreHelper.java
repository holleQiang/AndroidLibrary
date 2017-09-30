package com.zq.view.recyclerview.adapter;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * recyclerView 加载更多辅助类
 * Created by zhangqiang on 2017/8/17.
 */

public class RVLoadMoreHelper {

    private boolean isLoadingMore = false;
    private boolean isLoadMoreEnable;
    private LoadMoreController loadMoreController;

    public RVLoadMoreHelper(RecyclerView recyclerView, LoadMoreController loadMoreController) {

        recyclerView.addOnScrollListener(internalScrollerListener);
        this.loadMoreController = loadMoreController;
    }

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        isLoadMoreEnable = loadMoreEnable;
    }

    private RecyclerView.OnScrollListener internalScrollerListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (isLoadingMore || !isLoadMoreEnable || dy <= 0) {
                return;
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            int lastVisibleItem = findLastVisibleItem(layoutManager);
            int totalItemCount = layoutManager.getItemCount();
            int visibleItemCount = layoutManager.getChildCount();

            if (totalItemCount - lastVisibleItem == 1 && totalItemCount > visibleItemCount) {

//                View childView = recyclerView.getChildAt(visibleItemCount - 1);
//                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
//                if (childView.getBottom() + layoutParams.bottomMargin > recyclerView.getBottom() - recyclerView.getPaddingBottom()) {
//                    return;
//                }

                isLoadingMore = true;
                if (loadMoreController != null) {

                    ViewCompat.postOnAnimation(recyclerView, new Runnable() {
                        @Override
                        public void run() {
                            loadMoreController.shouldShowLoadMoreView();
                        }
                    });
                }
            }
        }
    };

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

    public interface LoadMoreController {

        void shouldShowLoadMoreView();

        void shouldHideLoadMoreView();
    }

    public void setLoadMoreComplete() {

        this.isLoadingMore = false;
        if (loadMoreController != null) {
            loadMoreController.shouldHideLoadMoreView();
        }
    }
}
