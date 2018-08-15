package com.caiyi.fund.trade.gui.cell.state;

/**
 * Created by zhangqiang on 2017/12/25.
 */

public interface StateDataBinder<T> {

    void bindWidthStateLoading(StateHelper<T> stateHelper);

    void bindWidthStateError(StateHelper<T> stateHelper);

    void bindEmptyData(StateHelper<T> stateHelper);

    void bindData(StateHelper<T> stateHelper,T data);

    boolean isEmpty(T data);
}
