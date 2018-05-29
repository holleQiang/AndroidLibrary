package com.zq.view.recyclerview.adapter.cell.ob;

import android.database.Observable;

import com.zq.view.recyclerview.adapter.cell.BaseCell;

/**
 * Created by zhangqiang on 2017/8/14.
 */

public class CellObservable extends Observable<CellObserver> {


    public void notifyCellChange(BaseCell cell){

        final int count = mObservers.size();
        for (int i = 0; i < count; i++) {

            mObservers.get(i).onCellChange(cell);
        }
    }

    public boolean hasObserverRegister(CellObserver observer){

        return observer != null && mObservers.contains(observer);
    }

    public void notifyItemChange(){


    }

    public void notifyAdapterDataChange(){


    }

}
