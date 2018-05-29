package com.zq.view.recyclerview.hscroll.controller;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangqiang on 2018/2/27.
 */

public class VerticalScrollControllerSet implements VerticalScrollController {

    private List<VerticalScrollController> verticalScrollControllers = new ArrayList<>();

    public VerticalScrollControllerSet(VerticalScrollController ...scrollControllers) {

        if(scrollControllers != null && scrollControllers.length > 0){
            verticalScrollControllers.addAll(Arrays.asList(scrollControllers));
        }
    }

    @Override
    public void syncVerticalScroll(RecyclerView.ViewHolder targetViewHolder) {

        for (VerticalScrollController verticalScrollController : verticalScrollControllers) {
            verticalScrollController.syncVerticalScroll(targetViewHolder);
        }
    }
}
