package com.zq.database.platform.android.sql.factory;


import android.database.sqlite.SQLiteOpenHelper;

import com.zq.database.platform.android.sql.AndroidSQL;
import com.zq.database.sql.SQL;
import com.zq.database.sql.factory.SQLFactory;

/**
 * Created by zhangqiang on 17-6-21.
 */

public class AndroidSQLFactory implements SQLFactory {

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
