package com.zq.db.sql.factory;


import android.database.sqlite.SQLiteOpenHelper;

import com.zq.db.bean.DefaultSQLBeanCreator;
import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.dao.Dao;
import com.zq.db.platform.android.sql.factory.AndroidSQLFactory;
import com.zq.db.platform.mysql.sql.factory.MySQLFactory;
import com.zq.db.sql.SQL;
import com.zq.db.table.TableHandler;

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


        public static SQL getAndroidSQL(SQLiteOpenHelper sqLiteOpenHelper){

            return new IMPL(new AndroidSQLFactory(sqLiteOpenHelper)).getSQL();
        }

        public static SQL getMySQL(String url, String userName, String password){

            return new IMPL(new MySQLFactory(url,userName,password)).getSQL();
        }

        public static <T extends SQLBean> Dao<T> getDao(SQL sql,Class<T> sqlBeanClass,SQLBeanCreator<T> sqlBeanCreator){

            return sql.getDaoFactory().getDao(sqlBeanClass,sqlBeanCreator);
        }

        public static <T extends SQLBean> Dao<T> getDao(SQL sql,Class<T> sqlBeanClass){

            return getDao(sql,sqlBeanClass,new DefaultSQLBeanCreator<T>());
        }

        public static <T extends SQLBean> TableHandler getTableHandler(SQL sql,Class<T> sqlBeanClass,SQLBeanCreator<T> sqlBeanCreator){

            return sql.getTableHandlerFactory().getTableHandler(sqlBeanClass,sqlBeanCreator);
        }

        public static <T extends SQLBean> TableHandler getTableHandler(SQL sql,Class<T> sqlBeanClass){

            return sql.getTableHandlerFactory().getTableHandler(sqlBeanClass,new DefaultSQLBeanCreator<T>());
        }
    }
}
