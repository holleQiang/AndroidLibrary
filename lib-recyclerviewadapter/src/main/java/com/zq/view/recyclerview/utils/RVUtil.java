package com.zq.view.recyclerview.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

/**
 * RecyclerView 工具类
 * Created by zhangqiang on 2017/8/22.
 */

public class RVUtil {

    /**
     * 使RecyclerView的change动画生效或者失效
     * @param enable true 生效，false 失效
     * @param mRecyclerView RecyclerView
     */
    public static void setChangeAnimationEnable(RecyclerView mRecyclerView,boolean enable){

        RecyclerView.ItemAnimator itemAnimator = mRecyclerView.getItemAnimator();
        if(itemAnimator instanceof SimpleItemAnimator){
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(enable);
        }
    }

    /**
     * 滚动到指定位置 仅支持 LinearLayoutManager
     * @param mRecyclerView  recyclerView
     * @param position 滚动的位置
     */
    public static void moveToPosition(RecyclerView mRecyclerView,int position) {

        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (position <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(position);
        }else if ( position <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(position - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(position);
            //这里这个变量是用在RecyclerView滚动监听里面的
        }
    }
}
