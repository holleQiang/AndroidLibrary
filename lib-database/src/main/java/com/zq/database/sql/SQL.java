package com.zq.database.sql;


import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.dao.Dao;
import com.zq.database.db.DBHandler;
import com.zq.database.table.TableHandler;

/**
 * Created by zhangqiang on 17-6-21.
 */

public interface SQL {

    <T extends SQLBean> TableHandler getTableHandler(Class<T> sqlBeanClass);

    <T extends SQLBean> TableHandler getTableHandler(Class<T> sqlBeanClass,SQLBeanCreator<T> creator);

    <T extends SQLBean> Dao<T> getDao(Class<T> sqlBeanClass);

    <T extends SQLBean> Dao<T> getDao(Class<T> sqlBeanClass, SQLBeanCreator<T> creator);

    DBHandler getDBHandler();
}
