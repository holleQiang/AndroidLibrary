package com.zq.view.recyclerview.adapter.cell.state;

/**
 * Created by zhangqiang on 2017/10/25.
 */

public class StateBean<T> {

    private STATE state;

    private T data;

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public StateBean() {
        setState(STATE.IDLE);
    }

    public enum STATE{

        IDLE,

        LOADING,

        ERROR,

        SUCCESS
    }
}
