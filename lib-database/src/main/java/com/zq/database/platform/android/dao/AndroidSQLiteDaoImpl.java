package com.zq.db.platform.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zq.db.annotation.Type;
import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.dao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17-6-19.
 */

public class AndroidSQLiteDaoImpl<T extends SQLBean> extends AbstractDao<T> implements AndroidSQLiteDao<T> {

    private SQLiteOpenHelper sqLiteOpenHelper;

    public AndroidSQLiteDaoImpl(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator, SQLiteOpenHelper sqLiteOpenHelper) {
        super(sqlBeanClass, sqlBeanCreator);
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public List<T> query(String[] columns, String selection, Object[] selectionArgs, String groupBy, String having, String orderBy, String limit) {

        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        if(db == null){
            return null;
        }

        Cursor cursor = db.query(getTableName(), columns, selection, objectArrayToStringArray(selectionArgs), groupBy, having, orderBy, limit);

        if(cursor == null){
            return null;
        }

        final int columnCount = cursor.getColumnCount();
        List<T> dataList = new ArrayList<>();
        while(cursor.moveToNext()){

            T data = createSQLBean();

            for (int i = 0; i < columnCount; i++) {

                String columnName = cursor.getColumnName(i);
                Type type = data.getType(columnName);
                switch (type){

                    case INTEGER :
                    case TEXT :
                    case REAL :
                    case NULL:

                        data.setValue(columnName,cursor.getString(i));
                        break;
                    case BLOB :

                        data.setValue(columnName,cursor.getBlob(i));
                        break;
                }
            }
            dataList.add(data);
        }
        cursor.close();
        return dataList;
    }



    @Override
    public int update(String[] columns, Object[] values, String whereClause, Object[] whereArgs) {

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if(db == null || db.isReadOnly()){
            return 0;
        }

        ContentValues contentValues = arrayToContentValues(columns, values);
        if(contentValues == null){
            return 0;
        }

        return db.update(getTableName(),contentValues,whereClause,objectArrayToStringArray(whereArgs));
    }

    @Override
    @Deprecated
    public int update(List<T> dataList) {

        return 0;
    }

    @Override
    public int update(T data, String whereClause, Object[] whereArgs) {

        ContentValues contentValues = sqlBeanToContentValues(data);
        if(contentValues == null){
            return 0;
        }
        return update(contentValues,whereClause,objectArrayToStringArray(whereArgs));
    }

    @Override
    public int delete(String whereClause, Object[] whereArgs) {

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if(db == null || db.isReadOnly()){
            return 0;
        }

        return db.delete(getTableName(),whereClause,objectArrayToStringArray(whereArgs));
    }

    @Override
    public long insert(String[] columns, Object[] values) {

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if(db == null || db.isReadOnly()){
            return -1;
        }

        ContentValues contentValues = arrayToContentValues(columns, values);
        if(contentValues == null){
            return -1;
        }
        return db.insert(getTableName(),null,contentValues);
    }

    @Override
    public boolean insert(List<T> dataList) {

        if(dataList == null || dataList.isEmpty()){
            return false;
        }

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if(db == null || db.isReadOnly()){
            return false;
        }

        db.beginTransaction();
        try {

            final int count = dataList.size();
            for (int i = 0; i < count; i++) {

                T sqlBean = dataList.get(i);

                ContentValues contentValues = sqlBeanToContentValues(sqlBean);
                if(contentValues == null){
                    return false;
                }
                long rowIndex = db.insert(getTableName(),null,contentValues);
                if(rowIndex <= -1){

                    return false;
                }
            }
            db.setTransactionSuccessful();
            return true;
        }finally {

            db.endTransaction();
        }
    }

    @Override
    public long insert(T data) {

        ContentValues contentValues = sqlBeanToContentValues(data);
        if(contentValues == null){
            return -1;
        }
        return insert(null,contentValues);
    }

    @Override
    public long insert( String nullColumnHack, ContentValues values) {

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if(db == null || db.isReadOnly()){
            return -1;
        }

        return db.insert(getTableName(),nullColumnHack,values);
    }

    @Override
    public int update( ContentValues values, String whereClause, String[] whereArgs) {

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if(db == null || db.isReadOnly()){
            return -1;
        }

        return db.update(getTableName(),values,whereClause,whereArgs);
    }

    @Override
    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {

        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        if(db == null){
            return null;
        }

        return db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    private static ContentValues arrayToContentValues(String[] columns, Object[] values){

        if(columns == null || columns.length <= 0 || columns.length > 0 && (values == null || values.length != columns.length)){
            return null;
        }

        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < columns.length; i++) {
            String key = columns[i];
            Object value = values[i];
            if(value == null){
//                contentValues.putNull(key);
            }else if(value instanceof Boolean){
                contentValues.put(key,(Boolean)value);
            }else if(value instanceof Float){
                contentValues.put(key,(Float)value);
            }else if(value instanceof Integer){
                contentValues.put(key,(Integer)value);
            }else if(value instanceof String){
                contentValues.put(key,(String)value);
            }else if(value instanceof Long){
                contentValues.put(key,(Long)value);
            }else if(value instanceof Short){
                contentValues.put(key,(Short)value);
            }else if(value instanceof Byte){
                contentValues.put(key,(Byte)value);
            }else if(value instanceof byte[]){
                contentValues.put(key,(byte[])value);
            }
        }
        return contentValues;
    }

    public static <T extends SQLBean> ContentValues sqlBeanToContentValues(T sqlBean){

        if(sqlBean == null){
            return null;
        }

        List<String> columnNames = sqlBean.columnNames();
        if(columnNames == null || columnNames.isEmpty()){
            return null;
        }
        final int columnCount = columnNames.size();
        String[] columns = new String[columnCount];
        Object[] values = new Object[columnCount];
        for (int j = 0; j < columnCount; j++) {

            String columnName = columnNames.get(j);
            columns[j] = columnName;
            values[j]  = sqlBean.getValue(columnName);
        }
        return arrayToContentValues(columns,values);
    }

    private static String[] objectArrayToStringArray(Object[] objectArray){

        if(objectArray == null){
            return null;
        }

        String[] stringArray = new String[objectArray.length];
        for (int i = 0; i < objectArray.length; i++) {

            Object object = objectArray[i];
            stringArray[i] = object == null ? null : object.toString();
        }
        return stringArray;
    }
}
