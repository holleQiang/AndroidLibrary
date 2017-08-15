package com.zq.db.dao.factory;


import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.dao.Dao;

/**
 * Created by zhangqiang on 17-6-19.
 */

public interface DaoFactory {

     <T extends SQLBean> Dao<T> getDao(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator);

}
