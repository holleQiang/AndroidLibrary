package com.zq.view.recyclerview.adapter.cell.collapse;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;

import java.util.List;

/**
 * 展开和收起的cell
 * Created by zhangqiang on 2017/8/21.
 */

public abstract class CollapsibleCell<T> extends MultiCell<T> implements DataBinder<T>{

    private boolean isCollapsible = true;
    private List<Cell> collapsibleCells;

    public CollapsibleCell(@LayoutRes int layoutId) {
        super(layoutId);
    }

    public CollapsibleCell(@LayoutRes int layoutId, int spanSize) {
        super(layoutId, spanSize);
    }

    public CollapsibleCell(@LayoutRes int layoutId, T data, List<Cell> collapsibleCells) {
        super(layoutId, data, null);
        this.collapsibleCells = collapsibleCells;
        setDataBinder(this);
    }

    public CollapsibleCell(@LayoutRes int layoutId, int spanSize, T data, List<Cell> collapsibleCells) {
        super(layoutId, spanSize, data, null);
        this.collapsibleCells = collapsibleCells;
        setDataBinder(this);
    }

    /**
     * 展开子项
     * @param cellAdapter
     * @return 展开的数量
     */
    public int expand(CellAdapter cellAdapter){

        if(!isCollapsible){
            return - 1;
        }
        int count = CollapsibleCellHelper.expand(cellAdapter,this);
        isCollapsible = false;
        return count;
    }

    /**
     * 收起所有子项
     * @param cellAdapter
     */
    public void collapse(CellAdapter cellAdapter){

        if(isCollapsible){
            return;
        }
        CollapsibleCellHelper.collapse(cellAdapter,this);
        isCollapsible = true;
    }

    public boolean isCollapsible() {
        return isCollapsible;
    }

    public final List<Cell> getCollapsibleCells() {
        return collapsibleCells;
    }

    void setCollapsible(boolean collapsible) {
        isCollapsible = collapsible;
    }
}
