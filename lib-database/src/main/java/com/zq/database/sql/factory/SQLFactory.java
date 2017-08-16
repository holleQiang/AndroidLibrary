package com.zq.database.sql.factory;


import android.database.sqlite.SQLiteOpenHelper;

import com.zq.database.bean.DefaultSQLBeanCreator;
import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.dao.Dao;
import com.zq.database.platform.android.sql.factory.AndroidSQLFactory;
import com.zq.database.platform.mysql.sql.factory.MySQLFactory;
import com.zq.database.sql.SQL;
import com.zq.database.table.TableHandler;


/**
 * Created by zhangqiang on 17-6-21.
 */

public interface SQLFactory<T extends SQLBean> {

    SQL<T> getSQL();

    class IMPL<T extends SQLBean> implements SQLFactory<T>{

        private SQLFactory<T> factory;

        private IMPL(SQLFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public SQL<T> getSQL() {

            return factory.getSQL();
        }

        public static <T extends SQLBean> SQL<T> getAndroidSQL(SQLiteOpenHelper sqLiteOpenHelper){

            return new IMPL<>(new AndroidSQLFactory<T>(sqLiteOpenHelper)).getSQL();
        }

        public static <T extends SQLBean> SQL getMySQL(String url, String userName, String password){

            return new IMPL<>(new MySQLFactory<T>(url,userName,password)).getSQL();
        }

        public static <T extends SQLBean> Dao<T> getDao(SQLiteOpenHelper sqLiteOpenHelper, Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator){

            SQL<T> sql = getAndroidSQL(sqLiteOpenHelper);
            return sql.getDao(sqlBeanClass,sqlBeanCreator);
        }

        public static <T extends SQLBean> Dao<T> getDao(SQLiteOpenHelper sqLiteOpenHelper, Class<T> sqlBeanClass){

            SQL<T> sql = getAndroidSQL(sqLiteOpenHelper);
            return sql.getDao(sqlBeanClass,null);
        }

        public static <T extends SQLBean> Dao<T> getDao(SQL<T> sql, Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator){

            return sql.getDao(sqlBeanClass,sqlBeanCreator);
        }

        public static <T extends SQLBean> Dao<T> getDao(SQL<T> sql,Class<T> sqlBeanClass){

            return getDao(sql,sqlBeanClass,new DefaultSQLBeanCreator<T>());
        }

        public static <T extends SQLBean> TableHandler getTableHandler(SQL<T> sql, Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator){

            return sql.getTableHandler(sqlBeanClass,sqlBeanCreator);
        }

        public static <T extends SQLBean> TableHandler getTableHandler(SQL<T> sql,Class<T> sqlBeanClass){

            return sql.getTableHandler(sqlBeanClass,new DefaultSQLBeanCreator<T>());
        }
    }
}
