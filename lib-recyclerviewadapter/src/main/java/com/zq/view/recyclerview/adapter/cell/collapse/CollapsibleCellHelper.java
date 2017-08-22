package com.zq.view.recyclerview.adapter.cell.expand;

import com.zq.view.recyclerview.adapter.cell.BaseCell;
import com.zq.view.recyclerview.adapter.cell.Cell;
import com.zq.view.recyclerview.adapter.cell.CellAdapter;

import java.util.List;

/**
 * Created by zhangqiang on 2017/8/21.
 */

public class CollapsibleCellHelper {

    public static void expand(CellAdapter cellAdapter, BaseCell attachedCell, List<Cell> collapsibleCells){

        int index = cellAdapter.getDataIndex(attachedCell);
        if(index == -1){
            return;
        }
        attachedCell.addTag("collapsibleCells",collapsibleCells);
        cellAdapter.addDataListAtIndex(collapsibleCells,index);
    }

    public static void collapse(CellAdapter cellAdapter,BaseCell attachedCell){

        int index = cellAdapter.getDataIndex(attachedCell);
        if(index == -1){
            return;
        }
        List<Cell> collapsibleCells = (List<Cell>) attachedCell.getTag("collapsibleCells");
        if(collapsibleCells != null){
            cellAdapter.removeDataFrom(index,collapsibleCells.size());
        }
    }
}
