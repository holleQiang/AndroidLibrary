package com.zq.mysql.bean;

import com.zq.mysql.annotations.Column;

import java.lang.reflect.Field;

/**
 * Created by zhangqiang on 2017/8/24.
 */

public class ReflectSQLBean implements SQLBean {

    @Override
    public boolean setValue(String columnName, Object value)  {

      Field field = findFiledWidthColumnName(columnName);

        if(field != null){

            field.setAccessible(true);
            try {
                field.set(this,value);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Object getValue(String columnName) {

        Field field = findFiledWidthColumnName(columnName);

        if(field != null){

            field.setAccessible(true);
            try {
                return field.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected Field findFiledWidthColumnName(String columnName){

        Field[] fields = getClass().getDeclaredFields();

        if(fields == null || fields.length <= 0){
            return null;
        }
        for (Field field:
                fields) {

            Column column = field.getAnnotation(Column.class);
            String cName = column.name();
            if(cName.length() <= 0){
                cName = field.getName();
            }
            if(cName.equals(columnName)){

                return field;
            }
        }
        return null;
    }
}
