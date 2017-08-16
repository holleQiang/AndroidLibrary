package com.zq.database.platform.android.sql;

import android.database.sqlite.SQLiteOpenHelper;

import com.zq.database.bean.DefaultSQLBeanCreator;
import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.dao.Dao;
import com.zq.database.dao.factory.DaoFactory;
import com.zq.database.platform.android.dao.factory.AndroidDaoFactory;
import com.zq.database.platform.android.table.factory.AndroidTableHandlerFactory;
import com.zq.database.sql.AbstractSQL;
import com.zq.database.table.TableHandler;
import com.zq.database.table.factory.TableHandlerFactory;

/**
 * Created by zhangqiang on 17-6-21.
 */

public class AndroidSQL<T extends SQLBean> extends AbstractSQL<T> {

    private DaoFactory daoFactory;
    private TableHandlerFactory tableHandlerFactory;

    private SQLiteOpenHelper sqLiteOpenHelper;

    public AndroidSQL( SQLiteOpenHelper sqLiteOpenHelper) {
        this(null,null,sqLiteOpenHelper);
    }

    public AndroidSQL(DaoFactory daoFactory, TableHandlerFactory tableHandlerFactory, SQLiteOpenHelper sqLiteOpenHelper) {
        this.daoFactory = daoFactory;
        this.tableHandlerFactory = tableHandlerFactory;
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public TableHandler getTableHandler(Class<T> sqlBeanClass) {
        return getTableHandler(sqlBeanClass,null);
    }

    @Override
    public TableHandler getTableHandler(Class<T> sqlBeanClass, SQLBeanCreator<T> creator) {

        if(tableHandlerFactory == null){
            tableHandlerFactory = new AndroidTableHandlerFactory();
        }
        if(creator == null){
            creator = new DefaultSQLBeanCreator<>();
        }
        return tableHandlerFactory.getTableHandler(sqlBeanClass,creator);
    }

    @Override
    public Dao<T> getDao(Class<T> sqlBeanClass) {
        return getDao(sqlBeanClass,null);
    }

    @Override
    public Dao<T> getDao(Class<T> sqlBeanClass, SQLBeanCreator<T> creator) {

        if(daoFactory == null){
            daoFactory = new AndroidDaoFactory(sqLiteOpenHelper);
        }
        if(creator == null){
            creator = new DefaultSQLBeanCreator<>();
        }
        return daoFactory.getDao(sqlBeanClass, creator);
    }
}
