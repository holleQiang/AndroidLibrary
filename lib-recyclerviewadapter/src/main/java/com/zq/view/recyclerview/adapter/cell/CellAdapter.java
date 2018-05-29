package com.zq.view.recyclerview.adapter.cell;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import com.zq.view.recyclerview.adapter.BaseObjectRecyclerAdapter;
import com.zq.view.recyclerview.adapter.R;
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

        final int layoutId = viewTypeIds.get(viewType);
        RVViewHolder viewHolder = RVViewHolder.create(context, viewTypeIds.get(viewType), parent);

        final int contentSize = getDataList().size();
        for (int i = 0; i < contentSize; i++) {

            Cell cell = getDataAt(i);
            if(cell.getLayoutId() == layoutId){
                cell.onViewCreated(viewHolder);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindContentViewHolder(RVViewHolder holder, int position) {

        Cell cell = getDataAt(position);
        cell.onBindData(holder);
    }

    @Override
    public RVViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {

        Cell headerCell = headerCells.get(0);
        int layoutId = headerCell.getLayoutId();
        RVViewHolder viewHolder = RVViewHolder.create(context, layoutId, parent);
        headerCell.onViewCreated(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindHeaderViewHolder(RVViewHolder holder, int position) {

        Cell cell = headerCells.get(position);
        cell.onBindData(holder);
    }

    @Override
    public RVViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {

        Cell footerCell = footerCells.get(0);
        int layoutId = footerCell.getLayoutId();
        RVViewHolder viewHolder = RVViewHolder.create(context, layoutId, parent);
        footerCell.onViewCreated(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindFooterViewHolder(RVViewHolder holder, int position) {

        Cell cell = footerCells.get(position);
        cell.onBindData(holder);
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
     * 获取header cell
     * @param index 索引
     * @return
     */
    public Cell getHeaderCellAt(int index){
        return headerCells.get(index);
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

    /**
     * 获取footer cell
     * @param index 索引
     * @return
     */
    public Cell getFooterCellAt(int index){
        return footerCells.get(index);
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
    public boolean onFailedToRecycleView(RVViewHolder holder) {

        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        recyclerView.removeOnAttachStateChangeListener(attachStateChangeListener);
        recyclerView.addOnAttachStateChangeListener(attachStateChangeListener);

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup oldSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    final int fixedPosition = fixLoopPosition(position);
                    if (isContentItem(fixedPosition)) {

                        final int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
                        Cell cell = getDataAt(fixedPosition - validHeaderCount);
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
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnAttachStateChangeListener(attachStateChangeListener);

        final int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View view  = recyclerView.getChildAt(i);
            RVViewHolder viewHolder = (RVViewHolder) recyclerView.getChildViewHolder(view);
            notifyViewDetachedFromWindow(viewHolder);
        }
    }

    @Override
    public void onViewAttachedToWindow(RVViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        notifyViewAttachedToWindow(holder);

        int position = holder.getAdapterPosition();
        position = fixLoopPosition(position);
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
    }

    @Override
    public void onViewDetachedFromWindow(RVViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        notifyViewDetachedFromWindow(holder);
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

    /**
     * 查找指定id的第一个出现的位置
     * @param layoutId 布局id
     * @return 位置 -1表示未找到
     */
    public int findFirstPositionOfView(@LayoutRes int layoutId){

        int headerCount = getHeaderItemCount();
        for (int i = 0; i < headerCount; i++) {

            Cell cell = headerCells.get(i);
            if(cell.getLayoutId() == layoutId){
                return i;
            }
        }

        int contentCount = getContentItemCount();
        for (int i = 0; i < contentCount; i++) {

            Cell cell = getDataAt(i);
            if(cell.getLayoutId() == layoutId){
                return headerCount + i;
            }
        }

        int footerCount = getFooterItemCount();
        for (int i = 0; i < footerCount; i++) {

            Cell cell = footerCells.get(i);
            if(cell.getLayoutId() == layoutId){
                return headerCount + contentCount + i;
            }
        }
       return -1;
    }

    /**
     * 查找指定id的第一个出现的cell
     * @param layoutId 布局id
     * @return cell null表示未找到
     */
    public <T extends Cell> T findFirstCellOfView(@LayoutRes int layoutId){

        int headerCount = getHeaderItemCount();
        for (int i = 0; i < headerCount; i++) {

            Cell cell = headerCells.get(i);
            if(cell.getLayoutId() == layoutId){
                return (T) cell;
            }
        }

        int contentCount = getContentItemCount();
        for (int i = 0; i < contentCount; i++) {

            Cell cell = getDataAt(i);
            if(cell.getLayoutId() == layoutId){
                return (T) cell;
            }
        }

        int footerCount = getFooterItemCount();
        for (int i = 0; i < footerCount; i++) {

            Cell cell = footerCells.get(i);
            if(cell.getLayoutId() == layoutId){
                return (T) cell;
            }
        }
        return null;
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

    private void notifyViewAttachedToWindow(RVViewHolder holder){

        int position = holder.getAdapterPosition();
        position = fixLoopPosition(position);
        Cell cell = findCellAtAdapterPosition(position);
        if(cell != null){

            final RVViewHolder oldViewHolder = cell.getAttachedViewHolder();
            if(oldViewHolder != null && oldViewHolder != holder){
                cell.onViewDetachedFromWindow(oldViewHolder);
            }
            cell.onViewAttachedToWindow(holder);
            cell.registerCellObserver(cellObserver);
            holder.getView().setTag(R.integer.tag_key_cell,cell);
        }
    }

    private void notifyViewDetachedFromWindow(RVViewHolder holder){

        Cell cell = (Cell) holder.getView().getTag(R.integer.tag_key_cell);
        holder.getView().setTag(R.integer.tag_key_cell,null);
        if(cell != null){

            final RVViewHolder oldViewHolder = cell.getAttachedViewHolder();

            if(holder.equals(oldViewHolder)){
                //防止notifyDataChange 造成先attach 然后调用detach的问题
                cell.onViewDetachedFromWindow(holder);
                cell.unRegisterCellObserver(cellObserver);
            }
        }
    }

    //recycler view 是否attach listener
    private View.OnAttachStateChangeListener attachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {

        }

        @Override
        public void onViewDetachedFromWindow(View v) {

            RecyclerView recyclerView = (RecyclerView) v;
            final int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount; i++) {

                View view  = recyclerView.getChildAt(i);
                RVViewHolder viewHolder = (RVViewHolder) recyclerView.getChildViewHolder(view);
                notifyViewDetachedFromWindow(viewHolder);
            }
        }


    };

    public Cell findCellAtAdapterPosition(int position){

        if(position == RecyclerView.NO_POSITION){
            return null;
        }

        if (isHeaderItem(position)) {

            return headerCells.get(position);
        } else if (isContentItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            return getDataAt(position - validHeaderCount);
        } else if (isFooterItem(position)) {

            int validHeaderCount = isHeaderEnable() ? getHeaderItemCount() : 0;
            return footerCells.get(position - validHeaderCount - getContentItemCount());
        }
        return null;
    }

    /**
     * 寻找指定类型的第一个cell
     * @param cellClass cell 的类型
     * @return
     */
    public <T extends Cell> T findCell(Class<T> cellClass){

        List<Cell> cellList = getDataList();
        for (Cell cell: cellList) {

            if(cellClass.isAssignableFrom(cell.getClass())){
                return (T) cell;
            }
        }
        return null;
    }
}
