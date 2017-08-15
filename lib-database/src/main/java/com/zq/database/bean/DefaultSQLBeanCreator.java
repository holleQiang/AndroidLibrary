package com.zq.database.bean;

/**
 * Created by zhangqiang on 17-6-20.
 */

public class DefaultSQLBeanCreator<T extends SQLBean> implements SQLBeanCreator<T> {

    @Override
    public T createSQLBean(Class<T> sqlBeanClass) {
        try {
            return sqlBeanClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
