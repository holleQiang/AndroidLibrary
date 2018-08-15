package com.zq.view.recyclerview.adapter.cell.state;

import android.support.annotation.LayoutRes;

import com.zq.view.recyclerview.adapter.cell.BaseCell;
import com.zq.view.recyclerview.viewholder.RVViewHolder;


/**
 * 给实体包装一个状态，区分空闲，加载中，失败和成功的状态
 * Created by zhangqiang on 2017/11/1.
 */

public class StateCell<T> extends BaseCell {

    private StateHelper<T> stateHelper = new StateHelper<>();
    private boolean needRebindWhenAttach;
    private StateCellDataBinder<T> stateCellDataBinder;

    public StateCell(int layoutId) {
        super(layoutId);
        stateHelper.setDataBinder(stateDataBinder);
    }

    public StateCell(int layoutId, int spanSize) {
        super(layoutId, spanSize);
        stateHelper.setDataBinder(stateDataBinder);
    }

    public static <T> StateCell<T> convert(@LayoutRes int layoutResId, StateCellDataBinder<T> dataBinder, OnRequestDataListener listener) {

        StateCell<T> stateCell = new StateCell<>(layoutResId);
        stateCell.setDataBinder(dataBinder);
        stateCell.setOnRequestDataListener(listener);
        return stateCell;
    }

    public static <T> StateCell<T> convert(@LayoutRes int layoutResId, int spanSize, StateCellDataBinder<T> dataBinder, OnRequestDataListener listener) {

        StateCell<T> stateCell = new StateCell<>(layoutResId, spanSize);
        stateCell.setDataBinder(dataBinder);
        stateCell.setOnRequestDataListener(listener);
        return stateCell;
    }

    @Override
    public void onBindData(RVViewHolder viewHolder) {
        stateHelper.dispatchStateBind();
    }

    @Override
    public void onViewAttachedToWindow(RVViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        if (needRebindWhenAttach) {
            needRebindWhenAttach = false;
            stateHelper.dispatchStateBind();
        }
    }

    public void reset() {
        stateHelper.reset();
    }

    public void reload() {
        stateHelper.reload();
    }

    public void setDataBinder(StateCellDataBinder<T> dataBinder) {
        this.stateCellDataBinder = dataBinder;
    }

    public void setOnRequestDataListener(OnRequestDataListener listener) {
        stateHelper.setOnRequestDataListener(listener);
    }

    public void setStateSuccess(T data) {
        stateHelper.setStateSuccess(data);
    }

    public void setStateError() {
        stateHelper.setStateError();
    }

    public OnRequestDataListener getOnRequestDataListener() {
        return stateHelper.getOnRequestDataListener();
    }

    public T getData() {
        return stateHelper.getData();
    }

    public void setNeedRebindWhenAttach(boolean needRebindWhenAttach) {
        this.needRebindWhenAttach = needRebindWhenAttach;
    }

    private StateDataBinder<T> stateDataBinder = new StateDataBinder<T>() {

        @Override
        public void bindWidthStateLoading(StateHelper<T> stateHelper) {

            RVViewHolder viewHolder = getAttachedViewHolder();
            if (viewHolder != null) {
                if (stateCellDataBinder != null) {
                    stateCellDataBinder.bindWidthStateLoading(StateCell.this, viewHolder);
                }
            } else {
                setNeedRebindWhenAttach(true);
            }
        }

        @Override
        public void bindWidthStateError(StateHelper<T> stateHelper) {
            RVViewHolder viewHolder = getAttachedViewHolder();
            if (viewHolder != null) {
                if (stateCellDataBinder != null) {
                    stateCellDataBinder.bindWidthStateError(StateCell.this, viewHolder);
                }
            } else {
                setNeedRebindWhenAttach(true);
            }
        }

        @Override
        public void bindEmptyData(StateHelper<T> stateHelper) {
            RVViewHolder viewHolder = getAttachedViewHolder();
            if (viewHolder != null) {

                if (stateCellDataBinder != null) {
                    stateCellDataBinder.bindEmptyData(StateCell.this, viewHolder);
                }
            } else {
                setNeedRebindWhenAttach(true);
            }
        }

        @Override
        public void bindData(StateHelper<T> stateHelper,T data) {

            RVViewHolder viewHolder = getAttachedViewHolder();
            if (viewHolder != null) {
                if (stateCellDataBinder != null) {
                    stateCellDataBinder.bindData(StateCell.this, viewHolder, data);
                }
            } else {
                setNeedRebindWhenAttach(true);
            }
        }

        @Override
        public boolean isEmpty(T data) {
            if (stateCellDataBinder != null) {
                return stateCellDataBinder.isEmpty(data);
            }
            return false;
        }
    };
}
