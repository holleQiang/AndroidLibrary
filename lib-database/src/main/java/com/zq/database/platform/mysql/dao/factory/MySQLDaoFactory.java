package com.zq.database.platform.mysql.dao.factory;


import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.dao.Dao;
import com.zq.database.dao.factory.DaoFactory;
import com.zq.database.platform.mysql.dao.MySQLDaoIMPL;
import com.zq.database.platform.mysql.sql.ConnectionPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MySQLDaoFactory<T extends SQLBean> implements DaoFactory<T> {

    private ConnectionPool connectionPool;
    private Dao<T> dao;

    public MySQLDaoFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public  Dao<T> getDao(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator) {

        if(dao == null){

            dao = new MySQLDaoIMPL<>(sqlBeanClass,sqlBeanCreator,connectionPool);
        }
        return dao;
    }

}
