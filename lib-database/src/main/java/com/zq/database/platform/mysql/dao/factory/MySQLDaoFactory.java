package com.zq.db.platform.mysql.dao.factory;

import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.dao.Dao;
import com.zq.db.dao.factory.DaoFactory;
import com.zq.db.platform.mysql.dao.MySQLDaoIMPL;
import com.zq.db.platform.mysql.sql.ConnectionPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MySQLDaoFactory implements DaoFactory {

    private Map<String,Dao> daoCache = new HashMap<>();
    private ConnectionPool connectionPool;

    public MySQLDaoFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends SQLBean> Dao<T> getDao(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator) {

        String sqlBeanClassName = sqlBeanClass.getName();

        Dao<T> dao = daoCache.get(sqlBeanClassName);

        if(dao == null){


            dao = new MySQLDaoIMPL<>(sqlBeanClass,sqlBeanCreator,connectionPool);

            daoCache.put(sqlBeanClassName,dao);
        }
        return dao;
    }

}
