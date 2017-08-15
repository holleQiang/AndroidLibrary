package com.zq.database.platform.android.dao.factory;

import android.database.sqlite.SQLiteOpenHelper;

import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.dao.Dao;
import com.zq.database.dao.factory.DaoFactory;
import com.zq.database.platform.android.dao.AndroidSQLiteDaoImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17-6-19.
 */

public class AndroidDaoFactory implements DaoFactory {

    private SQLiteOpenHelper sqLiteOpenHelper;
    private Map<String,Dao> daoCache = new HashMap<>();

    public AndroidDaoFactory(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public <T extends SQLBean> Dao<T> getDao(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator) {

        String sqlBeanClassName = sqlBeanClass.getName();

        Dao<T> dao = daoCache.get(sqlBeanClassName);

        if(dao == null){



            dao = new AndroidSQLiteDaoImpl<T>(sqlBeanClass,sqlBeanCreator, sqLiteOpenHelper);

            daoCache.put(sqlBeanClassName,dao);
        }
        return dao;
    }


}
