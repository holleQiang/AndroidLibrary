package com.zq.view.recyclerview.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangqiang on 16-10-11.
 */

public abstract class RecyclerHeaderFooterAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final int ITEM_TYPE_HEADER = 1;
    private static final int ITEM_TYPE_FOOTER = 2;

    private boolean headerEnable;
    private boolean footerEnable;
    private boolean singleLineHeaderEnable;
    private boolean singleLineFooterEnable;

    private OnItemClickListener onItemClickListener;
    private OnItemLongLickListener onItemLongLickListener;

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_HEADER) {

            return onCreateHeaderViewHolder(parent, viewType);
        } else if (viewType == ITEM_TYPE_FOOTER) {

            return onCreateFooterViewHolder(parent, viewType);
        } else {

            return onCreateContentViewHolder(parent, viewType - ITEM_TYPE_HEADER - ITEM_TYPE_FOOTER);
        }
    }

    @Override
    public final void onBindViewHolder(final VH holder,  int position) {

        int viewType = holder.getItemViewType();
        if (viewType == ITEM_TYPE_HEADER) {

            onBindHeaderViewHolder(holder, position);
        } else if (viewType == ITEM_TYPE_FOOTER) {

            final int headerCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            final int contentCount = getContentItemCount();
            onBindFooterViewHolder(holder, position - headerCount - contentCount);
        } else {

            final int headerCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            onBindContentViewHolder(holder, position - headerCount);
            if (onItemClickListener != null) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onItemClickListener.onItemClick(holder, holder.getAdapterPosition() - headerCount);
                    }
                });
            }

            if (onItemLongLickListener != null) {

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        return onItemLongLickListener.onItemLongClick(holder, holder.getAdapterPosition() - headerCount);
                    }
                });
            }
        }
    }




    @Override
    public final int getItemCount() {

        int itemCount = 0;

        if (isHeaderEnable()) {
            itemCount += getHeaderItemCount();
        }

        if (isFooterEnable()) {
            itemCount += getFooterItemCount();
        }

        itemCount += getContentItemCount();

        return itemCount;
    }

    @Override
    public final int getItemViewType(int position) {

        final int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;

        if (position < validHeaderCount) {

            return ITEM_TYPE_HEADER;
        } else if (position < validHeaderCount + getContentItemCount()) {

            int contentItemViewType = getContentItemViewType(position - validHeaderCount);
            if (contentItemViewType < 0) {

                throw new IllegalArgumentException("getContentItemViewType must return a value >= 0");
            }
            return contentItemViewType + ITEM_TYPE_HEADER + ITEM_TYPE_FOOTER;
        } else {

            return ITEM_TYPE_FOOTER;
        }
    }

    public VH onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public VH onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public abstract VH onCreateContentViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindContentViewHolder(VH holder, int position);

    public void onBindFooterViewHolder(VH holder, int position) {
    }

    public void onBindHeaderViewHolder(VH holder, int position) {
    }

    public int getHeaderItemCount() {
        return 0;
    }

    public int getFooterItemCount() {
        return 0;
    }

    public abstract int getContentItemCount();

    public int getContentItemViewType(int position) {
        return 0;
    }

    public final boolean isHeaderEnable() {
        return headerEnable;
    }

    public void setHeaderEnable(boolean headerEnable) {

        if (!isHeaderEnable() && headerEnable) {

            final int headerCount = getHeaderItemCount();
            notifyItemRangeInserted(0,headerCount);
            notifyItemRangeChanged(headerCount,getContentItemCount());
        }

        if(isHeaderEnable() && !headerEnable){

            final int headerCount = getHeaderItemCount();
            notifyItemRangeRemoved(0,headerCount);
            notifyItemRangeChanged(0,getContentItemCount());
        }
        this.headerEnable = headerEnable;
    }

    public final boolean isFooterEnable() {
        return footerEnable;
    }

    public void setFooterEnable(boolean footerEnable) {

        if (!isFooterEnable() && footerEnable) {

            final int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            final int startPosition = validHeaderCount + getContentItemCount();
            notifyItemRangeInserted(startPosition,getFooterItemCount());
        }

        if (isFooterEnable() && !footerEnable) {

            final int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            final int startPosition = validHeaderCount + getContentItemCount();
            notifyItemRangeRemoved(startPosition, getFooterItemCount());
        }
        this.footerEnable = footerEnable;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongLickListener(OnItemLongLickListener onItemLongLickListener) {
        this.onItemLongLickListener = onItemLongLickListener;
    }

    public boolean isHeaderItem(int position){

        return isHeaderEnable() && position < getHeaderItemCount();
    }

    public boolean isContentItem(int position){

        final int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
        return position >= validHeaderCount && position < getContentItemCount() + validHeaderCount;
    }

    public boolean isFooterItem(int position){

        final int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
        return position >= getContentItemCount() + validHeaderCount;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup oldSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    if(isHeaderItem(position) && singleLineHeaderEnable){
                        return gridLayoutManager.getSpanCount();
                    }
                    if(isFooterItem(position) && singleLineFooterEnable){
                        return gridLayoutManager.getSpanCount();
                    }
                    if(oldSizeLookup != null){
                        return oldSizeLookup.getSpanSize(position);
                    }
                    return 0;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);

        int position = holder.getLayoutPosition();
        if (isHeaderItem(position) && singleLineHeaderEnable || isFooterItem(position) && singleLineFooterEnable) {

            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }

    public boolean isSingleLineHeaderEnable() {
        return singleLineHeaderEnable;
    }

    public void setSingleLineHeaderEnable(boolean singleLineHeaderEnable) {
        this.singleLineHeaderEnable = singleLineHeaderEnable;
    }

    public boolean isSingleLineFooterEnable() {
        return singleLineFooterEnable;
    }

    public void setSingleLineFooterEnable(boolean singleLineFooterEnable) {
        this.singleLineFooterEnable = singleLineFooterEnable;
    }

}
