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

public interface SQLFactory {

    SQL getSQL();

    class IMPL implements SQLFactory{

        private SQLFactory factory;

        private IMPL(SQLFactory factory) {
            this.factory = factory;
        }

        @Override
        public SQL getSQL() {

            return factory.getSQL();
        }

        public static  SQL getAndroidSQL(SQLiteOpenHelper sqLiteOpenHelper){

            return new IMPL(new AndroidSQLFactory(sqLiteOpenHelper)).getSQL();
        }

        public static  SQL getMySQL(String url, String userName, String password){

            return new IMPL(new MySQLFactory(url,userName,password)).getSQL();
        }

        public static <T extends SQLBean> Dao<T> getDao(SQLiteOpenHelper sqLiteOpenHelper, Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator){

            SQL sql = getAndroidSQL(sqLiteOpenHelper);
            return sql.getDao(sqlBeanClass,sqlBeanCreator);
        }

        public static <T extends SQLBean> Dao<T> getDao(SQLiteOpenHelper sqLiteOpenHelper, Class<T> sqlBeanClass){

            SQL sql = getAndroidSQL(sqLiteOpenHelper);
            return sql.getDao(sqlBeanClass,null);
        }

        public static <T extends SQLBean> Dao<T> getDao(SQL sql, Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator){

            return sql.getDao(sqlBeanClass,sqlBeanCreator);
        }

        public static <T extends SQLBean> Dao<T> getDao(SQL sql,Class<T> sqlBeanClass){

            return getDao(sql,sqlBeanClass,new DefaultSQLBeanCreator<T>());
        }

        public static <T extends SQLBean> TableHandler getTableHandler(SQL sql, Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator){

            return sql.getTableHandler(sqlBeanClass,sqlBeanCreator);
        }

        public static <T extends SQLBean> TableHandler getTableHandler(SQL sql,Class<T> sqlBeanClass){

            return sql.getTableHandler(sqlBeanClass,new DefaultSQLBeanCreator<T>());
        }
    }
}
