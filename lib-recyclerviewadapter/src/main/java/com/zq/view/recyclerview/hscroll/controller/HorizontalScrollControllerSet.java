package com.zq.view.recyclerview.hscroll.controller;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangqiang on 2018/2/27.
 */

public class HorizontalScrollControllerSet implements HorizontalScrollController {

    private List<HorizontalScrollController> horizontalScrollControllers = new ArrayList<>();
    private HorizontalScrollController targetScrollController;

    public HorizontalScrollControllerSet(HorizontalScrollController ...scrollControllers) {

        if(scrollControllers != null && scrollControllers.length > 0){
            horizontalScrollControllers.addAll(Arrays.asList(scrollControllers));
        }
    }

    @Override
    public void syncHorizontalScroll(RecyclerView recyclerView, int dx, int scrollX) {
        if(targetScrollController != null){
            targetScrollController.syncHorizontalScroll(recyclerView, dx, scrollX);
        }
    }

    @Override
    public boolean shouldSyncHorizontalScroll(RecyclerView.ViewHolder touchedViewHolder) {

        for (HorizontalScrollController horizontalScrollController : horizontalScrollControllers) {
            boolean shouldSyncHorizontalScroll = horizontalScrollController.shouldSyncHorizontalScroll(touchedViewHolder);
            if(shouldSyncHorizontalScroll){
                targetScrollController = horizontalScrollController;
                return true;
            }
        }
        return false;
    }

}
