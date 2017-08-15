package com.zq.database.platform.android.table.factory;


import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.platform.android.table.AndroidTableHandler;
import com.zq.database.table.TableHandler;
import com.zq.database.table.factory.TableHandlerFactory;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class AndroidTableHandlerFactory implements TableHandlerFactory {

    private TableHandler tableHandler;

    @Override
    public <T extends SQLBean> TableHandler getTableHandler(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator) {
        if(tableHandler == null){
            tableHandler = new AndroidTableHandler<>(sqlBeanCreator.createSQLBean(sqlBeanClass));
        }
        return tableHandler;
    }
}
