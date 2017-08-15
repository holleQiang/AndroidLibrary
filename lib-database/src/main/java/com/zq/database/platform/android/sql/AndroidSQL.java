package com.zq.db.platform.android.sql;

import android.database.sqlite.SQLiteOpenHelper;

import com.zq.db.annotation.Type;
import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.platform.android.dao.factory.AndroidDaoFactory;
import com.zq.db.dao.factory.DaoFactory;
import com.zq.db.platform.android.table.factory.AndroidTableHandlerFactory;
import com.zq.db.sql.AbstractSQL;
import com.zq.db.table.factory.TableHandlerFactory;

import java.util.List;

/**
 * Created by zhangqiang on 17-6-21.
 */

public class AndroidSQL extends AbstractSQL{

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
