package com.zq.mysql.bean;

public interface SQLBean {

    boolean setValue(String columnName,Object value);

    Object getValue(String columnName);
}
