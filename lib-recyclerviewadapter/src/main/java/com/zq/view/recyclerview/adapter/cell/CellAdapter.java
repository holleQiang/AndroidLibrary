package com.zq.view.recyclerview.adapter.cell;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.zq.view.recyclerview.adapter.BaseObjectRecyclerAdapter;
import com.zq.view.recyclerview.adapter.cell.ob.CellObserver;
import com.zq.view.recyclerview.viewholder.RVViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17-7-3.
 */

public final class CellAdapter extends BaseObjectRecyclerAdapter<Cell, RVViewHolder> {

    private SparseIntArray viewTypeIds = new SparseIntArray();
    private List<Cell> headerCells = new ArrayList<>();
    private List<Cell> footerCells = new ArrayList<>();

    public CellAdapter(Context context) {
        super(context);
    }

    public CellAdapter(Context context, List<Cell> dataList) {
        super(context, dataList);
    }

    @Override
    public RVViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {

        return RVViewHolder.create(context, viewTypeIds.get(viewType), parent);
    }

    @Override
    public void onBindContentViewHolder(RVViewHolder holder, int position) {

        Cell cell = getDataAt(position);
        cell.onBind(holder);
    }

    @Override
    public RVViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {

        return RVViewHolder.create(context, headerCells.get(0).getLayoutId(), parent);
    }

    @Override
    public void onBindHeaderViewHolder(RVViewHolder holder, int position) {

        Cell cell = headerCells.get(position);
        cell.onBind(holder);
    }

    @Override
    public RVViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {

        return RVViewHolder.create(context, footerCells.get(0).getLayoutId(), parent);
    }

    @Override
    public void onBindFooterViewHolder(RVViewHolder holder, int position) {

        Cell cell = footerCells.get(position);
        cell.onBind(holder);
    }

    /**
     * 添加一个header
     *
     * @param cell
     */
    public void addHeaderCell(Cell cell) {

        final int headerCount = headerCells.size();
        headerCells.add(cell);
        if (isHeaderEnable()) {

            notifyItemRangeInserted(headerCount, 1);
        }
    }

    /**
     * 移除一个header
     *
     * @param cell
     */
    public void removeHeaderCell(Cell cell) {

        if(cell == null){
            return;
        }
        removeHeaderCellAt(headerCells.indexOf(cell));
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

        if(position < 0 || position > headerCount - 1){
            return;
        }

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
    public void addFooterCell(Cell cell) {

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

        if(position < 0 || position > footerCount - 1){
            return;
        }

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

    /**
     * 清除指定Footer
     *
     * @param cell
     */
    public void removeFooterCell(Cell cell) {

        if(cell == null){
            return;
        }
        removeFooterCellAt(footerCells.indexOf(cell));
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

        Cell cell = getDataAt(position);
        int layoutId = cell.getLayoutId();
        int viewType = viewTypeIds.indexOfValue(layoutId);
        if (viewType == -1) {

            viewType = viewTypeIds.size();
            viewTypeIds.put(viewType, layoutId);
        }
        return viewType;
    }


    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(RVViewHolder holder) {

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
                        Cell cell = getDataAt(position - validHeaderCount);
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
    public void onViewAttachedToWindow(RVViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        final int position = holder.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        if (isContentItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            Cell cell = getDataAt(position - validHeaderCount);
            if(cell.getSpanSize() == Cell.FULL_SPAN){

                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

                if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                    p.setFullSpan(true);
                }
            }
        }

        if (isHeaderItem(position)) {

            Cell cell = headerCells.get(position);
            cell.onAttachToWindow(holder);
            cell.registerCellObserver(cellObserver);
        } else if (isContentItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            Cell cell = getDataAt(position - validHeaderCount);
            cell.onAttachToWindow(holder);
            cell.registerCellObserver(cellObserver);
        } else if (isFooterItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            Cell cell = footerCells.get(position - validHeaderCount - getContentItemCount());
            cell.onAttachToWindow(holder);
            cell.registerCellObserver(cellObserver);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RVViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        int position = holder.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        if (isHeaderItem(position)) {

            Cell cell = headerCells.get(position);
            cell.onDetachFromWindow(holder);
        } else if (isContentItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            Cell cell = getDataAt(position - validHeaderCount);
            cell.onDetachFromWindow(holder);
        } else if (isFooterItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            Cell cell = footerCells.get(position - validHeaderCount - getContentItemCount());
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

    public int findPositionOfCell(Cell cell) {

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

    private int findCellPositionFromHeader(Cell cell) {

        if (!isHeaderEnable() || cell == null) {
            return -1;
        }

        return headerCells.indexOf(cell);
    }

    private int findCellPositionFromContent(Cell cell) {

        return getDataIndex(cell);
    }

    private int findCellPositionFromFooter(Cell cell) {

        if (!isFooterEnable() || cell == null) {
            return -1;
        }

        return footerCells.indexOf(cell);
    }

    private CellObserver cellObserver = new CellObserver() {

        @Override
        public void onCellChange(Cell cell) {

            int position = findPositionOfCell(cell);
            if(position != -1){
                notifyItemChanged(position);
            }
        }

        @Override
        public void onCellInsert(Cell cell) {

            
        }

        @Override
        public void onCellRemove(Cell cell) {

        }
    };
}
