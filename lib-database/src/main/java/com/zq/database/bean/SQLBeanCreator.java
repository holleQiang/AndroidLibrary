package com.zq.db.bean;

/**
 * Created by zhangqiang on 17-6-19.
 */

public interface SQLBeanCreator<T extends SQLBean> {

    T createSQLBean(Class<T> sqlBeanClass);
}
