package com.zq.db.dao;

import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;

import java.util.List;

/**
 * Created by zhangqiang on 17-6-20.
 */

public abstract class AbstractDao<T extends SQLBean> implements Dao<T> {

    private SQLBeanCreator<T> sqlBeanCreator;
    private Class<T> sqlBeanClass;
    private String tableName;

    public AbstractDao(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator) {
        this.sqlBeanCreator = sqlBeanCreator;
        this.sqlBeanClass = sqlBeanClass;
    }

    public String getTableName() {

        if(tableName == null){
            tableName = sqlBeanCreator.createSQLBean(sqlBeanClass).getTableName();
        }
        return tableName;
    }

    public T createSQLBean() {

        return sqlBeanCreator.createSQLBean(sqlBeanClass);
    }

    @Override
    public List<T> query(String selection, Object[] selectionArgs) {
        return query(null,selection,selectionArgs,null,null,null,null);
    }

    @Override
    public List<T> query(String[] columns, String selection, Object[] selectionArgs) {
        return query(columns,selection,selectionArgs,null,null,null,null);
    }

    @Override
    public List<T> query(String[] columns, String selection, Object[] selectionArgs, String orderBy, String limit) {
        return query(columns,selection,selectionArgs,null,null,orderBy,limit);
    }
}
