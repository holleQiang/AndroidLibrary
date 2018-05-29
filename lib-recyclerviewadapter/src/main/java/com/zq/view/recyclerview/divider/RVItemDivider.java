package com.zq.view.recyclerview.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Created by zhangqiang on 17-6-19.
 */

public class RVItemDivider extends RecyclerView.ItemDecoration {

    private int dividerWidth = 20;
    private Drawable dividerDrawable;
    private boolean skipLast;

    public RVItemDivider(int dividerColor, int dividerWidth) {

        dividerDrawable = new ColorDrawable(dividerColor);
        this.dividerWidth = dividerWidth;
    }

    public RVItemDivider(@NonNull Drawable dividerDrawable, int dividerWidth) {
        this.dividerWidth = dividerWidth;
        this.dividerDrawable = dividerDrawable;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();

            int itemCount = gridLayoutManager.getItemCount();
            int rowCount = itemCount % spanCount == 0 ? itemCount / spanCount : itemCount / spanCount + 1;

            int position = gridLayoutManager.getPosition(view);
            int rowIndex = getRowIndex(position,spanCount);
            int columnIndex = position % spanCount;
            int l = 0, t = 0, r = 0, b = 0;
            int orientation = gridLayoutManager.getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {

                l = getVerticalGridItemLeftOffset(columnIndex, spanCount, dividerWidth);
                t = getVerticalGridItemTopOffset(rowIndex,rowCount,dividerWidth);
            } else if (orientation == LinearLayoutManager.HORIZONTAL) {

                l = getHorizontalGridItemLeftOffset(rowIndex,rowCount,dividerWidth);
                t = getHorizontalGridItemTopOffset(columnIndex, spanCount, dividerWidth);
            }
            outRect.set(l,t,r,b);
        } else if (layoutManager instanceof LinearLayoutManager) {

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (skipLast && linearLayoutManager.getPosition(view) == layoutManager.getItemCount() - 1) {
                return;
            }
            int orientation = linearLayoutManager.getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {

                outRect.set(0, 0, dividerWidth, 0);
            } else if (orientation == LinearLayoutManager.VERTICAL) {

                outRect.set(0, 0, 0, dividerWidth);
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

//            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            throw new IllegalArgumentException(" unSupport ");
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();
            int itemCount = gridLayoutManager.getItemCount();
            int rowCount = itemCount % spanCount == 0 ? itemCount / spanCount : itemCount / spanCount + 1;

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {

                View childView = parent.getChildAt(i);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) childView.getLayoutParams();
                int vl = childView.getLeft() - lp.leftMargin;
                int vt = childView.getTop() - lp.topMargin;
                int vr = childView.getRight() + lp.rightMargin;
                int vb = childView.getBottom() + lp.bottomMargin;
                int position = gridLayoutManager.getPosition(childView);
                int rowIndex = getRowIndex(position,spanCount);
                int columnIndex = position % spanCount;

                int l, t;
                int orientation = gridLayoutManager.getOrientation();
                if (orientation == GridLayoutManager.VERTICAL) {

                    l = getVerticalGridItemLeftOffset(columnIndex, spanCount, dividerWidth);
                    t = getVerticalGridItemTopOffset(rowIndex,rowCount,dividerWidth);
                    if(l > 0){

                        boolean isLast = isVerticalGridItemBottomLast(itemCount,rowIndex,rowCount,columnIndex,spanCount);
                        int fixSize = isLast ? 0 : l;
                        drawDivider(c,vl - l,vt - l,vl,vb + fixSize);
                    }
                    if(t > 0){

                        boolean isLast = isVerticalGridItemRightLast(itemCount,rowIndex,rowCount,columnIndex,spanCount);
                        int fixSize = isLast ? 0 : l;
                        drawDivider(c,vl - t,vt - t,vr + fixSize,vt);
                    }
                } else if (orientation == LinearLayoutManager.HORIZONTAL) {

                    l = getHorizontalGridItemLeftOffset(rowIndex,rowCount,dividerWidth);
                    t = getHorizontalGridItemTopOffset(columnIndex, spanCount, dividerWidth);
                    if(l > 0){

                        boolean isLast = isHorizontalGridBottomLast(itemCount,rowIndex,rowCount,columnIndex,spanCount);
                        int fixSize = isLast ? 0 : l;
                        drawDivider(c,vl - l,vt - l,vl,vb + fixSize);
                    }
                    if(t > 0){

                        boolean isLast = isHorizontalGridRightLast(itemCount,rowIndex,rowCount,columnIndex,spanCount);
                        int fixSize = isLast ? 0 : l;
                        drawDivider(c,vl - t,vt - t,vr + fixSize,vt);
                    }
                }

            }
        } else if (layoutManager instanceof LinearLayoutManager) {

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {

                if(skipLast && i == childCount - 1){
                    return;
                }
                View childView = parent.getChildAt(i);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) childView.getLayoutParams();

                if (orientation == LinearLayoutManager.HORIZONTAL) {

                    //绘制右边
                    int l = childView.getRight() + lp.rightMargin;
                    int t = childView.getTop() - lp.topMargin;
                    int r = l + dividerWidth;
                    int b = childView.getBottom() + lp.bottomMargin;
                    drawDivider(c, l, t, r, b);
                } else if (orientation == LinearLayoutManager.VERTICAL) {

                    //绘制下面
                    int l = childView.getLeft() - lp.leftMargin;
                    int t = childView.getBottom() + lp.bottomMargin;
                    int r = childView.getRight() + lp.rightMargin;
                    int b = t + dividerWidth;
                    drawDivider(c, l, t, r, b);
                }
            }

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        }
    }

    private void drawDivider(Canvas canvas, int l, int t, int r, int b) {

        if (dividerDrawable == null) {
            return;
        }
        dividerDrawable.setBounds(l, t, r, b);
        dividerDrawable.draw(canvas);
    }

    private static int getVerticalGridItemLeftOffset(int columnIndex, int spanCount, int dividerWidth) {

        int l = 0;
        if (spanCount != 1 && columnIndex != 0) {

            l = dividerWidth;
        }
        return l;
    }


    private static int getVerticalGridItemTopOffset(int rowIndex, int rowCount, int dividerWidth) {

        int t = 0;
        if (rowCount != 1 && rowIndex != 0) {

            t = dividerWidth;
        }
        return t;
    }

    private static int getHorizontalGridItemLeftOffset(int rowIndex, int rowCount, int dividerWidth) {

        return getVerticalGridItemTopOffset(rowIndex, rowCount, dividerWidth);
    }


    private static int getHorizontalGridItemTopOffset(int columnIndex, int spanCount, int dividerWidth) {

        return getVerticalGridItemLeftOffset(columnIndex, spanCount, dividerWidth);
    }

    private static int getRowIndex(int position,int spanCount){

        position = position + 1;
        int rowIndex = position % spanCount == 0 ? position / spanCount : position / spanCount + 1;
        return rowIndex - 1;
    }

    private boolean isVerticalGridItemBottomLast(int itemCount,int rowIndex,int rowCount,int columnIndex,int spanCount) {

        return rowIndex == rowCount - 1 || rowIndex == rowCount - 2 && (itemCount) % spanCount != 0 && columnIndex > itemCount % spanCount - 1;
    }

    private boolean isVerticalGridItemRightLast(int itemCount,int rowIndex,int rowCount,int columnIndex,int spanCount){

        return rowIndex == rowCount - 1 && columnIndex == itemCount % spanCount - 1;
    }

    private boolean isHorizontalGridRightLast(int itemCount,int rowIndex,int rowCount,int columnIndex,int spanCount){

        return isVerticalGridItemBottomLast(itemCount, rowIndex, rowCount, columnIndex, spanCount);
    }

    private boolean isHorizontalGridBottomLast(int itemCount,int rowIndex,int rowCount,int columnIndex,int spanCount){

        return isVerticalGridItemRightLast(itemCount, rowIndex, rowCount,columnIndex,spanCount);
    }

    public boolean isSkipLast() {
        return skipLast;
    }

    public void setSkipLast(boolean skipLast) {
        this.skipLast = skipLast;
    }
}
