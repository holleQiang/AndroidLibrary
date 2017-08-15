package com.zq.db.platform.mysql.sql;

import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.dao.factory.DaoFactory;
import com.zq.db.platform.mysql.dao.factory.MySQLDaoFactory;
import com.zq.db.platform.mysql.table.factory.MySQLTableHandlerFactory;
import com.zq.db.sql.AbstractSQL;
import com.zq.db.table.factory.TableHandlerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MySQL extends AbstractSQL {

    private DaoFactory daoFactory;
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
    public TableHandlerFactory getTableHandlerFactory() {
        if(tableHandlerFactory == null){
            tableHandlerFactory = new MySQLTableHandlerFactory(Pool.getInstance());
        }
        return tableHandlerFactory;
    }

    @Override
    public DaoFactory getDaoFactory() {
        if(daoFactory == null){
            daoFactory = new MySQLDaoFactory(Pool.getInstance());
        }
        return daoFactory;
    }

    private static class Pool extends ConnectionPool {

        private static final int MAX_POOL_SIZE = 10;
        private String url;
        private String userName;
        private String password;

        private static final Pool instance = new Pool();

        public static Pool getInstance() {
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
            return DriverManager.getConnection(url,userName,password);
        }
    }
}
