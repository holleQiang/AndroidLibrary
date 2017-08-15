package com.zq.db.platform.android.sql.factory;


import android.database.sqlite.SQLiteOpenHelper;

import com.zq.db.bean.DefaultSQLBeanCreator;
import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.dao.factory.DaoFactory;
import com.zq.db.sql.SQL;
import com.zq.db.platform.android.sql.AndroidSQL;
import com.zq.db.sql.factory.SQLFactory;

/**
 * Created by zhangqiang on 17-6-21.
 */

public class AndroidSQLFactory implements SQLFactory{

    private SQLiteOpenHelper sqLiteOpenHelper;

    private SQL sql;

    public AndroidSQLFactory( SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public SQL getSQL() {

        if(sql == null){
            sql = new AndroidSQL(sqLiteOpenHelper);
        }
        return sql;
    }
}
