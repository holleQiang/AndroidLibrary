package com.caiyi.fund.trade.gui.cell.state;

/**
 * Created by zhangqiang on 2017/12/25.
 */

public class StateHelper<T> {

    private StateBean<T> stateBean = new StateBean<>();
    private OnRequestDataListener onRequestDataListener;
    private StateDataBinder<T> dataBinder;
    private RequestInterceptor requestInterceptor;

    public void reload() {

        if (stateBean.getState() == StateBean.STATE.LOADING) {
            return;
        }
        stateBean.setState(StateBean.STATE.IDLE);
        dispatchStateBind();
    }

    public void dispatchStateBind() {


        if (StateBean.STATE.IDLE.equals(stateBean.getState())) {

            stateBean.setState(StateBean.STATE.LOADING);
            if (onRequestDataListener != null || requestInterceptor != null && !requestInterceptor.shouldInterceptRequest()) {
                onRequestDataListener.onRequestData();
            }
        }

        if (StateBean.STATE.LOADING.equals(stateBean.getState())) {

            bindWidthStateLoading();
        } else if (StateBean.STATE.ERROR.equals(stateBean.getState())) {

            bindWidthStateError();
        } else if (StateBean.STATE.SUCCESS.equals(stateBean.getState())) {

            T data = stateBean.getData();
            if(dataBinder != null){
                boolean isEmpty = dataBinder.isEmpty(data);
                if (isEmpty) {
                    dataBinder.bindEmptyData(this);
                } else {
                    dataBinder.bindData(this, data);
                }
            }
        }
    }


    protected void bindWidthStateError() {

        if (dataBinder != null) {
            dataBinder.bindWidthStateError(this);
        }
    }

    protected void bindWidthStateLoading() {

        if (dataBinder != null) {
            dataBinder.bindWidthStateLoading(this);
        }
    }

    public void setStateError() {

        stateBean.setState(StateBean.STATE.ERROR);
        dispatchStateBind();
    }

    public void reset() {

        if(StateBean.STATE.IDLE.equals(stateBean.getState())){
            return;
        }
        stateBean.setState(StateBean.STATE.IDLE);
    }

    public void setStateSuccess(T data) {

        stateBean.setState(StateBean.STATE.SUCCESS);
        stateBean.setData(data);
        dispatchStateBind();
    }

    public T getData() {
        return stateBean.getData();
    }

    public void setOnRequestDataListener(OnRequestDataListener onRequestDataListener) {
        this.onRequestDataListener = onRequestDataListener;
    }

    public void setDataBinder(StateDataBinder<T> dataBinder) {
        this.dataBinder = dataBinder;
    }

    public OnRequestDataListener getOnRequestDataListener() {
        return onRequestDataListener;
    }

    public StateDataBinder<T> getDataBinder() {
        return dataBinder;
    }

    public RequestInterceptor getRequestInterceptor() {
        return requestInterceptor;
    }

    public void setRequestInterceptor(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    public interface RequestInterceptor{

        boolean shouldInterceptRequest();
    }
}
