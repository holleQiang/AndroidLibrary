package com.zq.view.recyclerview.adapter.cell.collapse;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 2017/8/21.
 */

public class CollapsibleCellHelper {

    private static final String TAG_COLLAPSIBLE_CELL = "collapsibleCells";

    private ICollapsibleCell collapsibleCell;

    private boolean isCollapsible = true;
    private List<Cell> collapsibleCells;

    public CollapsibleCellHelper(ICollapsibleCell groupCell, List<Cell> collapsibleCells) {
        this.collapsibleCell = groupCell;
        this.collapsibleCells = collapsibleCells;
    }


    public int expand(CellAdapter cellAdapter, boolean expandChild) {

        if (!isCollapsible || getCollapsibleCells() == null || getCollapsibleCells().isEmpty()) {
            return -1;
        }

        int index = cellAdapter.getDataIndex(collapsibleCell);
        if (index == -1 || collapsibleCells == null || collapsibleCells.isEmpty()) {
            return -1;
        }

        int expandCount = 0;

        List<Cell> targetCollapsibleCells = collapsibleCell.getTag(TAG_COLLAPSIBLE_CELL);

        if (targetCollapsibleCells == null) {

            targetCollapsibleCells = new ArrayList<>();
            collapsibleCell.addTag(TAG_COLLAPSIBLE_CELL, targetCollapsibleCells);
        }

        targetCollapsibleCells.clear();
        targetCollapsibleCells.addAll(collapsibleCells);

        for (Cell cell : collapsibleCells) {

            index++;
            expandCount++;
            cellAdapter.addDataAtIndex(cell, index);

            if (cell instanceof ICollapsibleCell) {

                ICollapsibleCell childCollapsibleCell = (ICollapsibleCell) cell;
                if (!childCollapsibleCell.isCollapsible()) {
                    //如果已经展开，计算展开的数量
                    int childExpandCount = childCollapsibleCell.getCollapsibleCount();
                    if (childExpandCount > 0) {
                        index += childExpandCount;
                        expandCount += childExpandCount;
                    }
                } else if (expandChild) {
                    //如果展开子cell
                    int childExpandCount = childCollapsibleCell.expand(cellAdapter, true);
                    if (childExpandCount > 0) {
                        index += childExpandCount;
                        expandCount += childExpandCount;
                    }
                }
            }
        }
        isCollapsible = false;
        return expandCount;
    }

    public void collapse(CellAdapter cellAdapter) {

        if (isCollapsible || getCollapsibleCells() == null || getCollapsibleCells().isEmpty()) {
            return;
        }

        int index = cellAdapter.getDataIndex(collapsibleCell);
        if (index == -1) {
            return;
        }
        List<Cell> collapsibleCells = collapsibleCell.getTag(TAG_COLLAPSIBLE_CELL);
        if (collapsibleCells != null) {

            cellAdapter.removeDataFrom(index + 1, getCollapsibleCount(collapsibleCell));
        }
        isCollapsible = true;
    }

    public static int getCollapsibleCount(ICollapsibleCell collapsibleCell) {

        int count = 0;
        if (collapsibleCell.getCollapsibleCells() != null) {

            final int size = collapsibleCell.getCollapsibleCells().size();
            count += size;

            for (Cell cell :
                    collapsibleCell.getCollapsibleCells()) {

                if (cell instanceof CollapsibleCell) {

                    ICollapsibleCell childCollapsibleCell = (CollapsibleCell) cell;
                    if (!childCollapsibleCell.isCollapsible()) {

                        count += getCollapsibleCount(childCollapsibleCell);
                    }
                }
            }
        }
        return count;
    }

    public boolean isCollapsible() {
        return isCollapsible;
    }

    public void setCollapsible(boolean collapsible) {
        isCollapsible = collapsible;
    }

    public List<Cell> getCollapsibleCells() {
        return collapsibleCells;
    }

    public void setCollapsibleCells(List<Cell> collapsibleCells, CellAdapter cellAdapter) {

        if (this.collapsibleCells == collapsibleCells) {
            return;
        }
        if (this.collapsibleCells != null && !isCollapsible && cellAdapter != null) {
            collapse(cellAdapter);
        }
        this.collapsibleCells = collapsibleCells;
    }

    public void setCollapsibleCells(List<Cell> collapsibleCells) {
        setCollapsibleCells(collapsibleCells, null);
    }
}
