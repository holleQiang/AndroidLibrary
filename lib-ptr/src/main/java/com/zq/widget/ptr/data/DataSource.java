package com.zq.widget.ptr.data;

/**
 * Author：zhangqiang
 * Date：2019/1/14 19:35:26
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public interface DataSource<T> {

    void loadData(int pageIndex, int pageSize, int startIndex, int endIndex,Callback<T> callback);

    void dispose();
}
