package com.zq.view.recyclerview.utils;

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
}
