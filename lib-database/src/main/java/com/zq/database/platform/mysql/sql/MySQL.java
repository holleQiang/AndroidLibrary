package com.zq.database.platform.mysql.sql;


import com.zq.database.bean.DefaultSQLBeanCreator;
import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.dao.Dao;
import com.zq.database.dao.factory.DaoFactory;
import com.zq.database.platform.mysql.dao.factory.MySQLDaoFactory;
import com.zq.database.platform.mysql.table.factory.MySQLTableHandlerFactory;
import com.zq.database.sql.AbstractSQL;
import com.zq.database.table.TableHandler;
import com.zq.database.table.factory.TableHandlerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * mySql impl
 * Created by zhangqiang on 17-7-4.
 */

public class MySQL<T extends SQLBean> extends AbstractSQL<T> {

    private DaoFactory<T> daoFactory;
    private TableHandlerFactory tableHandlerFactory;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MySQL(String url, String userName, String password) {
        Pool.getInstance().init(url, userName, password);
    }

    @Override
    public TableHandler getTableHandler(Class<T> sqlBeanClass) {
        return getTableHandler(sqlBeanClass, null);
    }

    @Override
    public TableHandler getTableHandler(Class<T> sqlBeanClass, SQLBeanCreator<T> creator) {

        if (tableHandlerFactory == null) {
            tableHandlerFactory = new MySQLTableHandlerFactory(Pool.getInstance());
        }

        if (creator == null) {
            creator = new DefaultSQLBeanCreator<>();
        }
        return tableHandlerFactory.getTableHandler(sqlBeanClass, creator);
    }

    @Override
    public Dao<T> getDao(Class<T> sqlBeanClass) {
        return getDao(sqlBeanClass, null);
    }

    @Override
    public Dao<T> getDao(Class<T> sqlBeanClass, SQLBeanCreator<T> creator) {

        if (creator == null) {
            creator = new DefaultSQLBeanCreator<>();
        }

        if (daoFactory == null) {
            daoFactory = new MySQLDaoFactory<>(Pool.getInstance());
        }
        return daoFactory.getDao(sqlBeanClass, creator);
    }

    private static class Pool extends ConnectionPool {

        private static final int MAX_POOL_SIZE = 10;
        private String url;
        private String userName;
        private String password;

        private static final Pool instance = new Pool();

        static Pool getInstance() {
            return instance;
        }

        private Pool() {
            super(MAX_POOL_SIZE);
        }

        synchronized void init(String url, String userName, String password) {
            this.url = url;
            this.userName = userName;
            this.password = password;
        }

        @Override
        public Connection openConnection() throws SQLException {
            return DriverManager.getConnection(url, userName, password);
        }
    }
}
