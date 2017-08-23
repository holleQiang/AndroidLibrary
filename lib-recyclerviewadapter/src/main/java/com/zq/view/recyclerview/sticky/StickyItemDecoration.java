package com.zq.view.recyclerview.sticky;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zq.view.recyclerview.adapter.cell.CellAdapter;

/**
 * Created by zhangqiang on 2017/8/22.
 */

public class StickyItemDecoration extends RecyclerView.ItemDecoration {

    private CellAdapter cellAdapter;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        boolean isContent = cellAdapter.isContentItem(position);
        if(!isContent){
            return;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
