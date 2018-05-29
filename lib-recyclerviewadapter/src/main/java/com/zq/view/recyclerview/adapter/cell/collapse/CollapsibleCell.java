package com.zq.view.recyclerview.adapter.cell.collapse;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.adapter.cell.BaseCell;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;
import com.zq.view.recyclerview.adapter.cell.DataBinder;
import com.zq.view.recyclerview.adapter.cell.MultiCell;

import java.util.List;

/**
 * 展开和收起的cell
 * Created by zhangqiang on 2017/8/21.
 */

public class CollapsibleCell<T> extends MultiCell<T> implements ICollapsibleCell{

    private CollapsibleCellHelper collapsibleCellHelper;

    public CollapsibleCell(@LayoutRes int layoutId) {
        super(layoutId);
        init(null);
    }

    public CollapsibleCell(@LayoutRes int layoutId, int spanSize) {
        super(layoutId, spanSize);
        init(null);
    }

    public CollapsibleCell(@LayoutRes int layoutId, T data, DataBinder<T> dataBinder,List<Cell> collapsibleCells) {
        super(layoutId, data, null);
        init(collapsibleCells);
    }

    public CollapsibleCell(@LayoutRes int layoutId, int spanSize, T data, DataBinder<T> dataBinder,List<Cell> collapsibleCells) {
        super(layoutId, spanSize, data, null);
        init(collapsibleCells);
    }

    private void init(List<Cell> collapsibleCells) {
        collapsibleCellHelper = new CollapsibleCellHelper(this,collapsibleCells);
    }


    @Override
    public int expand(CellAdapter cellAdapter) {
        return collapsibleCellHelper.expand(cellAdapter,false);
    }

    @Override
    public int expand(CellAdapter cellAdapter, boolean expandChild) {
        return collapsibleCellHelper.expand(cellAdapter,expandChild);
    }

    @Override
    public void collapse(CellAdapter cellAdapter) {
        collapsibleCellHelper.collapse(cellAdapter);
    }

    @Override
    public void setCollapsibleCells(List<Cell> collapsibleCells) {
        collapsibleCellHelper.setCollapsibleCells(collapsibleCells);
    }

    @Override
    public int getCollapsibleCount() {
        return CollapsibleCellHelper.getCollapsibleCount(this);
    }

    @Override
    public boolean isCollapsible() {
        return collapsibleCellHelper.isCollapsible();
    }

    @Override
    public List<Cell> getCollapsibleCells() {
        return collapsibleCellHelper.getCollapsibleCells();
    }
}
