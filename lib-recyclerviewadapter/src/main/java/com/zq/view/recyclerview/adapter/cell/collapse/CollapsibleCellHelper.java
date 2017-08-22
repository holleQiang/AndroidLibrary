package com.zq.view.recyclerview.adapter.cell.collapse;

import com.zq.view.recyclerview.adapter.cell.BaseCell;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 2017/8/21.
 */

public class CollapsibleCellHelper {

    private static final String TAG_COLLAPSIBLE_CELL = "collapsibleCells";

    public static int expand(CellAdapter cellAdapter,CollapsibleCell parentCell){

        List<Cell> collapsibleCells = parentCell.getCollapsibleCells();
        int index = cellAdapter.getDataIndex(parentCell);
        if(index == -1 || collapsibleCells == null || collapsibleCells.isEmpty()){
            return - 1;
        }

        int expandCount = 0;

        List<Cell> targetCollapsibleCells = (List<Cell>) parentCell.getTag(TAG_COLLAPSIBLE_CELL);

        if(targetCollapsibleCells == null){

            targetCollapsibleCells = new ArrayList<>();
            parentCell.addTag(TAG_COLLAPSIBLE_CELL,targetCollapsibleCells);
        }

        targetCollapsibleCells.clear();
        targetCollapsibleCells.addAll(collapsibleCells);

        for (Cell cell: collapsibleCells) {

            index ++;
            expandCount ++;
            cellAdapter.addDataAtIndex(cell,index);

            if(cell instanceof CollapsibleCell){

                CollapsibleCell childCollapsibleCell = (CollapsibleCell) cell;
                if(!childCollapsibleCell.isCollapsible()){

                    childCollapsibleCell.setCollapsible(true);
                    int childExpandCount = childCollapsibleCell.expand(cellAdapter);
                    if(childExpandCount > 0){
                        index += childExpandCount;
                        expandCount += childExpandCount;
                    }
                }
            }
        }
        return expandCount;
    }

    public static <C extends CollapsibleCell> void collapse(CellAdapter cellAdapter,C parentCell){

        int index = cellAdapter.getDataIndex(parentCell);
        if(index == -1){
            return;
        }
        List<Cell> collapsibleCells = (List<Cell>) parentCell.getTag(TAG_COLLAPSIBLE_CELL);
        if(collapsibleCells != null){

            cellAdapter.removeDataFrom(index + 1,getCollapsibleCount(collapsibleCells));
        }
    }

    private static int getCollapsibleCount(List<Cell> collapsibleCells){

        int count = 0;
        if(collapsibleCells != null){

            final int size = collapsibleCells.size();
            count += size;

            for (Cell cell:
                    collapsibleCells) {

                if(cell instanceof CollapsibleCell){

                    CollapsibleCell childCollapsibleCell = (CollapsibleCell) cell;
                    if(!childCollapsibleCell.isCollapsible()){

                        count += getCollapsibleCount(childCollapsibleCell.getCollapsibleCells());
                    }
                }
            }
        }
        return count;
    }
}
