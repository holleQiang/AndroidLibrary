package com.zq.view.recyclerview.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * RecyclerView分割线
 * Created by zhangqiang on 17-6-19.
 */

public class RVItemDivider extends RecyclerView.ItemDecoration {

    private int dividerWidth = 20;
    private Drawable dividerDrawable;

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
            int position = gridLayoutManager.getPosition(view);
            int itemCount = gridLayoutManager.getItemCount();

            if (position % spanCount == 0) {

                if (position == itemCount - 1) {

                    outRect.set(0, 0, dividerWidth, dividerWidth);
                } else {
                    outRect.set(0, 0, dividerWidth / 2, dividerWidth);
                }

            } else if (position % spanCount == spanCount - 1) {


                outRect.set(dividerWidth / 2, 0, 0, dividerWidth);
            } else {

                if (position == itemCount - 1) {

                    outRect.set(dividerWidth / 2, 0, dividerWidth, dividerWidth);
                } else {
                    outRect.set(dividerWidth / 2, 0, dividerWidth / 2, dividerWidth);
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();
            if(orientation == LinearLayoutManager.HORIZONTAL){

                outRect.set(0,0,dividerWidth,0);
            }else if(orientation == LinearLayoutManager.VERTICAL){

                outRect.set(0,0,0,dividerWidth);
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

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {

                View childView = parent.getChildAt(i);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) childView.getLayoutParams();
                int position = gridLayoutManager.getPosition(childView);

                if (position % spanCount == 0) {


                    //绘制下面
                    int l = childView.getLeft() - lp.leftMargin;
                    int t = childView.getBottom() + lp.bottomMargin;
                    int r = childView.getRight() + lp.rightMargin + dividerWidth / 2;
                    int b = t + dividerWidth;
                    drawDivider(c,l,t,r,b);

                    if (position == itemCount - 1) {

                        //绘制右边
                        l = childView.getRight() + lp.rightMargin + dividerWidth/2;
                        t = childView.getTop() - lp.topMargin;
                        r = l + dividerWidth;
                        b = childView.getBottom() + lp.bottomMargin + dividerWidth;
                        drawDivider(c,l,t,r,b);
                    } else {

                        //绘制右边
                        l = childView.getRight() + lp.rightMargin;
                        t = childView.getTop() - lp.topMargin;
                        r = l + dividerWidth / 2;
                        b = childView.getBottom() + lp.bottomMargin;
                        drawDivider(c,l,t,r,b);
                    }
                } else if (position % spanCount == spanCount - 1) {

                    //绘制左边
                    int l = childView.getLeft() - lp.leftMargin - dividerWidth / 2;
                    int t = childView.getTop() - lp.topMargin;
                    int r = l + dividerWidth / 2;
                    int b = childView.getBottom() + lp.bottomMargin;
                    drawDivider(c,l,t,r,b);

                    //绘制下面
                    l = childView.getLeft() - lp.leftMargin - dividerWidth / 2;
                    t = childView.getBottom() + lp.bottomMargin;
                    r = childView.getRight() + lp.rightMargin;
                    b = t + dividerWidth;
                    drawDivider(c,l,t,r,b);

                } else {

                    //绘制左边
                    int l = childView.getLeft() - lp.leftMargin - dividerWidth / 2;
                    int t = childView.getTop() - lp.topMargin;
                    int r = l + dividerWidth / 2;
                    int b = childView.getBottom() + lp.bottomMargin;
                    drawDivider(c,l,t,r,b);

                    if (position == itemCount - 1) {

                        //绘制右边
                        l = childView.getRight() + lp.rightMargin + dividerWidth/2;
                        t = childView.getTop() - lp.topMargin;
                        r = l + dividerWidth;
                        b = childView.getBottom() + lp.bottomMargin + dividerWidth;
                        drawDivider(c,l,t,r,b);
                    } else {

                        //绘制右边
                        l = childView.getRight() + lp.rightMargin;
                        t = childView.getTop() - lp.topMargin;
                        r = l + dividerWidth / 2;
                        b = childView.getBottom() + lp.bottomMargin + dividerWidth;
                        drawDivider(c,l,t,r,b);
                    }

                    //绘制下面
                    l = childView.getLeft() - lp.leftMargin - dividerWidth / 2;
                    t = childView.getBottom() + lp.bottomMargin;
                    r = childView.getRight() + lp.rightMargin + dividerWidth/2;
                    b = t + dividerWidth;
                    drawDivider(c,l,t,r,b);
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {

                View childView = parent.getChildAt(i);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) childView.getLayoutParams();

                if(orientation == LinearLayoutManager.HORIZONTAL){

                    //绘制右边
                    int l = childView.getRight() + lp.rightMargin;
                    int t = childView.getTop() - lp.topMargin;
                    int r = l + dividerWidth;
                    int b = childView.getBottom() + lp.bottomMargin;
                    drawDivider(c,l,t,r,b);
                }else if(orientation == LinearLayoutManager.VERTICAL){

                    //绘制下面
                    int l = childView.getLeft() - lp.leftMargin;
                    int t = childView.getBottom() + lp.bottomMargin;
                    int r = childView.getRight() + lp.rightMargin;
                    int b = t + dividerWidth;
                    drawDivider(c,l,t,r,b);
                }
            }

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        }
    }

    private void drawDivider(Canvas canvas,int l,int t, int r ,int b){

        if(dividerDrawable == null){
            return;
        }
        dividerDrawable.setBounds(l,t,r,b);
        dividerDrawable.draw(canvas);
    }
}
