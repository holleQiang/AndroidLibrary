package com.zq.view.recyclerview.adapter.cell;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.zq.view.recyclerview.adapter.BaseObjectRecyclerAdapter;
import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;
import com.zq.view.recyclerview.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17-7-3.
 */

public final class CellAdapter<C extends Cell<RecyclerViewHolder>> extends BaseObjectRecyclerAdapter<C, RecyclerViewHolder> {

    private SparseIntArray viewTypeIds = new SparseIntArray();
    private List<C> headerCells = new ArrayList<>();
    private List<C> footerCells = new ArrayList<>();

    public CellAdapter(List<C> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public RecyclerViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {

        return RecyclerViewHolder.create(context, viewTypeIds.get(viewType), parent);
    }

    @Override
    public void onBindContentViewHolder(RecyclerViewHolder holder, int position) {

        Cell<RecyclerViewHolder> cell = getDataAt(position);
        cell.onBind(holder);
    }

    @Override
    public RecyclerViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {

        return RecyclerViewHolder.create(context, headerCells.get(0).getLayoutId(), parent);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerViewHolder holder, int position) {

        Cell<RecyclerViewHolder> cell = headerCells.get(position);
        cell.onBind(holder);
    }

    @Override
    public RecyclerViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {

        return RecyclerViewHolder.create(context, footerCells.get(0).getLayoutId(), parent);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerViewHolder holder, int position) {

        Cell<RecyclerViewHolder> cell = footerCells.get(position);
        cell.onBind(holder);
    }

    /**
     * 添加一个header
     *
     * @param cell
     */
    public void addHeaderCell(C cell) {

        final int headerCount = headerCells.size();
        headerCells.add(cell);
        if (isHeaderEnable()) {

            notifyItemRangeInserted(headerCount, 1);
        }
    }

    /**
     * 清空Header
     */
    public void clearHeaderCell() {

        final int headerCount = headerCells.size();
        headerCells.clear();
        if (isHeaderEnable()) {

            notifyItemRangeRemoved(0, headerCount);
        }
    }

    /**
     * 移除指定Header
     *
     * @param position
     */
    public void removeHeaderCellAt(int position) {

        final int headerCount = headerCells.size();
        headerCells.remove(position);
        if (isHeaderEnable()) {

            notifyItemRangeRemoved(position, 1);
            if (position != headerCount - 1) {
                notifyItemRangeChanged(position, headerCount - 1 - position);
            }
        }
    }

    /**
     * 添加一个Footer
     *
     * @param cell
     */
    public void addFooterCell(C cell) {

        footerCells.add(cell);
        if (isFooterEnable()) {

            notifyItemRangeInserted(getItemCount() - 1, 1);
        }
    }


    /**
     * 清空Footer
     */
    public void clearFooterCell() {

        final int footerCount = footerCells.size();
        footerCells.clear();
        if (isFooterEnable()) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            notifyItemRangeRemoved(getContentItemCount() + validHeaderCount, footerCount);
        }
    }

    /**
     * 清除指定Footer
     *
     * @param position
     */
    public void removeFooterCellAt(int position) {

        final int footerCount = footerCells.size();
        footerCells.remove(position);
        if (isFooterEnable()) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            int startPosition = position + getContentItemCount() + validHeaderCount;
            notifyItemRemoved(startPosition);
            if (position != footerCount - 1) {
                notifyItemRangeChanged(startPosition, footerCount - 1 - position);
            }
        }
    }


    @Override
    public int getHeaderItemCount() {
        return headerCells.size();
    }

    @Override
    public int getFooterItemCount() {
        return footerCells.size();
    }

    @Override
    public int getContentItemViewType(int position) {

        Cell<RecyclerViewHolder> cell = getDataAt(position);
        int layoutId = cell.getLayoutId();
        int viewType = viewTypeIds.indexOfValue(layoutId);
        if (viewType == -1) {

            viewType = viewTypeIds.size();
            viewTypeIds.put(viewType, layoutId);
        }
        return viewType;
    }

    @Override
    public void onViewRecycled(RecyclerViewHolder holder) {
        super.onViewRecycled(holder);
        int position = holder.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        if (isHeaderItem(position)) {

            C cell = headerCells.get(position);
            cell.onRecycle(holder);
        } else if (isContentItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            C cell = getDataAt(position - validHeaderCount);
            cell.onRecycle(holder);
        } else if (isFooterItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            C cell = footerCells.get(position - validHeaderCount - getContentItemCount());
            cell.onRecycle(holder);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerViewHolder holder) {

        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup oldSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    if (isContentItem(position)) {

                        final int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
                        C cell = getDataAt(position - validHeaderCount);
                        int spanSize = cell.getSpanSize();
                        if(spanSize == Cell.FULL_SPAN){
                            spanSize = ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        return spanSize;
                    }

                    if (oldSizeLookup != null) {
                        return oldSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        final int position = holder.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        if (isContentItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            C cell = getDataAt(position - validHeaderCount);
            if(cell.getSpanSize() == Cell.FULL_SPAN){

                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

                if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                    p.setFullSpan(true);
                }
            }
        }

        if (isHeaderItem(position)) {

            C cell = headerCells.get(position);
            cell.onAttachToWindow(holder);
            cell.registerCellObserver(cellObserver);
        } else if (isContentItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            C cell = getDataAt(position - validHeaderCount);
            cell.onAttachToWindow(holder);
            cell.registerCellObserver(cellObserver);
        } else if (isFooterItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            C cell = footerCells.get(position - validHeaderCount - getContentItemCount());
            cell.onAttachToWindow(holder);
            cell.registerCellObserver(cellObserver);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        int position = holder.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        if (isHeaderItem(position)) {

            C cell = headerCells.get(position);
            cell.onDetachFromWindow(holder);
        } else if (isContentItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            C cell = getDataAt(position - validHeaderCount);
            cell.onDetachFromWindow(holder);
        } else if (isFooterItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            C cell = footerCells.get(position - validHeaderCount - getContentItemCount());
            cell.onDetachFromWindow(holder);
            cell.unRegisterCellObserver(cellObserver);
        }
    }

    public int getViewTypeOfCell(Cell cell) {

        int position = viewTypeIds.indexOfValue(cell.getLayoutId());
        if(position != -1){
            return viewTypeIds.keyAt(position);
        }
        return -1;
    }

    public int findPositionOfCell(Cell<RecyclerViewHolder> cell) {


        int cellPosition = findCellPositionFromHeader(cell);
        if (cellPosition == -1) {

            cellPosition = findCellPositionFromContent(cell);
            if (cellPosition != -1) {

                cellPosition += isHeaderEnable() ? getHeaderItemCount() : 0;
                return cellPosition;
            } else {

                cellPosition = findCellPositionFromFooter(cell);
                if (cellPosition != -1) {
                    cellPosition += (getContentItemCount() + (isHeaderEnable() ? getHeaderItemCount() : 0));
                }
            }
        }
        return cellPosition;
    }

    private int findCellPositionFromHeader(Cell<RecyclerViewHolder> cell) {

        if (!isHeaderEnable()) {
            return -1;
        }

        final int headerCount = getHeaderItemCount();
        for (int i = 0; i < headerCount; i++) {

            Cell<RecyclerViewHolder> headerCell = headerCells.get(i);
            if (headerCell.equals(cell)) {
                return i;
            }
        }
        return -1;
    }

    private int findCellPositionFromContent(Cell<RecyclerViewHolder> cell) {

        final int headerCount = getContentItemCount();
        for (int i = 0; i < headerCount; i++) {

            Cell<RecyclerViewHolder> headerCell = getDataAt(i);
            if (headerCell.equals(cell)) {
                return i;
            }
        }
        return -1;
    }

    private int findCellPositionFromFooter(Cell<RecyclerViewHolder> cell) {

        final int headerCount = getFooterItemCount();
        for (int i = 0; i < headerCount; i++) {

            Cell<RecyclerViewHolder> headerCell = footerCells.get(i);
            if (headerCell.equals(cell)) {
                return i;
            }
        }
        return -1;
    }

    private CellObserver<RecyclerViewHolder> cellObserver = new CellObserver<RecyclerViewHolder>() {

        @Override
        public void onCellChange(Cell<RecyclerViewHolder> cell) {

            int position = findPositionOfCell(cell);
            if(position != -1){
                notifyItemChanged(position);
            }
        }

        @Override
        public void onCellInsert(Cell<RecyclerViewHolder> cell) {

        }

        @Override
        public void onCellRemove(Cell<RecyclerViewHolder> cell) {

        }
    };
}
