package com.zq.db.sql;

import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.dao.Dao;
import com.zq.db.dao.factory.DaoFactory;
import com.zq.db.table.factory.TableHandlerFactory;

/**
 * Created by zhangqiang on 17-6-21.
 */

public interface SQL {

    TableHandlerFactory getTableHandlerFactory();

    DaoFactory getDaoFactory();
}
