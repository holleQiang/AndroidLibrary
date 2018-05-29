package com.zq.view.recyclerview.adapter.cell.collapse;

import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;

import java.util.List;

/**
 * Created by zhangqiang on 2017/12/23.
 */

public interface ICollapsibleCell extends Cell {

    /**
     * 展开子项
     * @param cellAdapter
     * @return 展开的数量
     */
    int expand(CellAdapter cellAdapter);

    /**
     * 展开子项
     * @param cellAdapter
     * @return 展开的数量
     */
    int expand(CellAdapter cellAdapter,boolean expandChild);

    /**
     * 收起所有子项
     * @param cellAdapter
     */
    void collapse(CellAdapter cellAdapter);

    /**
     * 设置子项
     * @param collapsibleCells
     */
    void setCollapsibleCells(List<Cell> collapsibleCells);

    /**
     * 获取展开的子项数量
     * @return
     */
    int getCollapsibleCount();

    /**
     * 是否已经展开
     * @return
     */
    boolean isCollapsible();

    /**
     * 获取子项
     * @return
     */
    List<Cell> getCollapsibleCells();
}
