package com.zq.database.platform.android.sql;

import android.database.sqlite.SQLiteOpenHelper;

import com.zq.database.dao.factory.DaoFactory;
import com.zq.database.platform.android.dao.factory.AndroidDaoFactory;
import com.zq.database.platform.android.table.factory.AndroidTableHandlerFactory;
import com.zq.database.sql.AbstractSQL;
import com.zq.database.table.factory.TableHandlerFactory;

/**
 * Created by zhangqiang on 17-6-21.
 */

public class AndroidSQL extends AbstractSQL {

    private DaoFactory daoFactory;
    private TableHandlerFactory tableHandlerFactory;

    private SQLiteOpenHelper sqLiteOpenHelper;

    public AndroidSQL( SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public TableHandlerFactory getTableHandlerFactory() {

        if(tableHandlerFactory == null){
            tableHandlerFactory = new AndroidTableHandlerFactory();
        }
        return null;
    }

    @Override
    public DaoFactory getDaoFactory() {

        if(daoFactory == null){
            daoFactory = new AndroidDaoFactory(sqLiteOpenHelper);
        }
        return daoFactory;
    }
}
