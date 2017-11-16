package com.zq.view.recyclerview.hscroll.controller;

import android.support.annotation.Nullable;
import android.view.View;

import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 横向滚动控制器集合
 * Created by zhangqiang on 2017/10/24.
 */

public final class HorizontalScrollControllerSet implements HorizontalScrollController {

    private List<HorizontalScrollController> controllerList = new ArrayList<>();

    public HorizontalScrollControllerSet(HorizontalScrollController... controllers) {

        if (controllers == null) {
            return;
        }
        controllerList.addAll(Arrays.asList(controllers));
    }


    @Override
    public void syncVerticalScroll(View aimView, View syncView) {

        for (HorizontalScrollController controller : controllerList) {

            if(!checkType(aimView,syncView,controller)){
                continue;
            }
            controller.syncVerticalScroll(aimView,syncView);
            break;
        }
    }

    @Override
    public boolean shouldSyncHorizontalScroll(View anchorView, View targetView) {
        for (HorizontalScrollController controller : controllerList) {

            if(!checkType(anchorView,targetView,controller)){
                continue;
            }
            boolean isSync = controller.shouldSyncHorizontalScroll(anchorView,targetView);
            if(isSync){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldSyncVerticalScroll(View anchorView, View targetView) {

        for (HorizontalScrollController controller : controllerList) {

            if(!checkType(anchorView,targetView,controller)){
                continue;
            }
            boolean isSync = controller.shouldSyncVerticalScroll(anchorView,targetView);
            if(isSync){
                return true;
            }
        }
        return false;
    }

    @Override
    public Class getAnchorViewClass() {
        return null;
    }

    @Override
    public Class getTargetAnchorClass() {
        return null;
    }

    @Nullable
    @Override
    public View getTargetView(RVViewHolder viewHolder) {
        for (HorizontalScrollController controller : controllerList) {

            View view = controller.getTargetView(viewHolder);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    @Override
    public View getAnchorView(RVViewHolder viewHolder) {
        for (HorizontalScrollController controller : controllerList) {

            View view = controller.getAnchorView(viewHolder);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    @Override
    public void syncHorizontalScroll(View syncView, int dx) {

        for (HorizontalScrollController controller : controllerList) {

            if(!checkType(syncView,controller)){
                continue;
            }
            controller.syncHorizontalScroll(syncView,dx);
            break;
        }
    }

    private static boolean checkType(View anchorView,View targetView,HorizontalScrollController controller){

        return controller.getAnchorViewClass().isAssignableFrom(anchorView.getClass()) && controller.getTargetAnchorClass().isAssignableFrom(targetView.getClass());
    }

    private static boolean checkType(View targetView,HorizontalScrollController controller){

        return controller.getTargetAnchorClass().isAssignableFrom(targetView.getClass());
    }
}
