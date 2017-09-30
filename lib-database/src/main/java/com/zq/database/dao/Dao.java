package com.zq.database.dao;

import com.zq.database.bean.SQLBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhangqiang on 17-6-19.
 */

public interface Dao<T extends SQLBean>{

    List<T> query(String[] columns, String selection,
                  Object[] selectionArgs, String groupBy, String having,
                  String orderBy, String limit);

    List<T> query(String[] columns, String selection,
                  Object[] selectionArgs);

    List<T> query(String[] columns, String selection,
                  Object[] selectionArgs,
                  String orderBy, String limit);

    List<T> query(String selection, Object[] selectionArgs);

    int update(String[] columns, Object[] values, String whereClause, Object[] whereArgs);

    @Deprecated
    int update(List<T> dataList);

    int update(T data, String whereClause, Object[] whereArgs);

    int delete(String whereClause, Object[] whereArgs);

    long insert(String[] columns, Object[] values);

    boolean insert(List<T> dataList);

    long insert(T data);

    void execute(String sql,String[] args) throws SQLException, android.database.SQLException;
}
