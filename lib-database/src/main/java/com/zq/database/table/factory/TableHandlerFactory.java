package com.zq.database.table.factory;


import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.table.TableHandler;

/**
 * Created by zhangqiang on 17-7-4.
 */

public interface TableHandlerFactory {

    <T extends SQLBean> TableHandler getTableHandler(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator);
}
