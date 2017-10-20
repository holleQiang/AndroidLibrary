package com.zq.mysql.dao;

import com.zq.mysql.bean.SQLBean;

import java.util.List;

/**
 * Created by zhangqiang on 2017/8/24.
 */

public interface Dao<T extends SQLBean> {

    List<T> query(String sql,Object[] args);

    boolean insert(T data);

    boolean insert(List<T> dataList);

    boolean update(T data);

    boolean delete(T data);
}
