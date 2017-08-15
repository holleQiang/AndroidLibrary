package com.zq.db.platform.android.table.factory;

import android.database.sqlite.SQLiteDatabase;

import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.platform.android.table.AndroidTableHandler;
import com.zq.db.table.TableHandler;
import com.zq.db.table.factory.TableHandlerFactory;

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
