package com.zq.view.recyclerview.hscroll.controller;

import android.widget.HorizontalScrollView;

/**
 * Created by zhangqiang on 2017/10/24.
 */

public abstract class HorizontalScrollViewScrollController implements HorizontalScrollController<HorizontalScrollView,HorizontalScrollView>{

    @Override
    public void syncHorizontalScroll(HorizontalScrollView syncView, int dx) {
        syncView.scrollBy(dx,0);
    }

    @Override
    public void syncVerticalScroll(HorizontalScrollView anchorView, HorizontalScrollView syncView) {

        int scrollX = anchorView.getScrollX();
        syncView.scrollTo(scrollX,0);
    }

    @Override
    public Class<HorizontalScrollView> getAnchorViewClass() {
        return HorizontalScrollView.class;
    }

    @Override
    public Class<HorizontalScrollView> getTargetAnchorClass() {
        return HorizontalScrollView.class;
    }
}
