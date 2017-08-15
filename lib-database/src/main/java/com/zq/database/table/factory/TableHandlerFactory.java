package com.zq.db.table.factory;

import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.table.TableHandler;

/**
 * Created by zhangqiang on 17-7-4.
 */

public interface TableHandlerFactory {

    <T extends SQLBean> TableHandler getTableHandler(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator);
}
