package com.zq.widget.ptr.data;

import android.support.annotation.NonNull;

/**
 * Author：zhangqiang
 * Date：2019/1/14 19:36:23
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public interface Callback<T> {

    /**
     * 获取数据回调，可多次调用
     * @param t t
     */
    void onNextData(@NonNull T t);

    /**
     * 获取数据完成回调，只有第一次回调有效
     */
    void onComplete();

    /**
     * 获取数据失败回调，只有第一次回调有效
     * @param e e
     */
    void onError(Throwable e);
}
