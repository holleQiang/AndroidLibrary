package com.zq.database.platform.android.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.zq.database.bean.SQLBean;
import com.zq.database.dao.Dao;

/**
 * Created by zhangqiang on 17-6-19.
 */

public interface AndroidSQLiteDao<T extends SQLBean> extends Dao<T> {



    long insert(String nullColumnHack, ContentValues values);

    int update(ContentValues values, String whereClause, String[] whereArgs);

    Cursor query(String[] columns, String selection,
                 String[] selectionArgs, String groupBy, String having,
                 String orderBy);
}
