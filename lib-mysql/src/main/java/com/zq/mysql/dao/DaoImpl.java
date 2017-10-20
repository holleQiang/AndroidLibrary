package com.zq.mysql.dao;

import com.zq.mysql.bean.SQLBean;
import com.zq.mysql.connection.ConnectionPool;

import java.sql.Connection;
import java.util.List;

/**
 * Created by zhangqiang on 2017/8/24.
 */

public class DaoImpl<T extends SQLBean> implements Dao<T> {

    @Override
    public List<T> query(String sql, Object[] args) {

        Connection connection = ConnectionPool.getInstance().poll();

        return null;
    }

    @Override
    public boolean insert(T data) {
        return false;
    }

    @Override
    public boolean insert(List<T> dataList) {
        return false;
    }

    @Override
    public boolean update(T data) {
        return false;
    }

    @Override
    public boolean delete(T data) {
        return false;
    }
}
