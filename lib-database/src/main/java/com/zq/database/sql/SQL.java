package com.zq.database.sql;


import com.zq.database.dao.factory.DaoFactory;
import com.zq.database.table.factory.TableHandlerFactory;

/**
 * Created by zhangqiang on 17-6-21.
 */

public interface SQL {

    TableHandlerFactory getTableHandlerFactory();

    DaoFactory getDaoFactory();
}
