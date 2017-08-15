package com.zq.database.dao.factory;


import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.dao.Dao;

/**
 * Created by zhangqiang on 17-6-19.
 */

public interface DaoFactory {

     <T extends SQLBean> Dao<T> getDao(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator);

}
