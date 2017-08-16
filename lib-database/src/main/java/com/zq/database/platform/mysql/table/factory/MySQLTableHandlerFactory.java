package com.zq.database.platform.mysql.table.factory;



import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.platform.mysql.sql.ConnectionPool;
import com.zq.database.platform.mysql.table.MySQLTableHandler;
import com.zq.database.table.TableHandler;
import com.zq.database.table.factory.TableHandlerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MySQLTableHandlerFactory implements TableHandlerFactory {

    private ConnectionPool connectionPool;
    private TableHandler tableHandler;

    public MySQLTableHandlerFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public <T extends SQLBean> TableHandler getTableHandler(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator) {

        if(tableHandler == null){
            tableHandler = new MySQLTableHandler<>(sqlBeanCreator.createSQLBean(sqlBeanClass), connectionPool);
        }
        return tableHandler;
    }
}
