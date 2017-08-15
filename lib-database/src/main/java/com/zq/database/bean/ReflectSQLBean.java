package com.zq.database.bean;

import com.zq.database.annotation.Column;
import com.zq.database.annotation.Table;
import com.zq.database.annotation.Type;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by zhangqiang on 17-6-19.
 */

public class ReflectSQLBean implements SQLBean {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void setValue(String columnName, Object value) {

        Field field = findFieldByColumnName(columnName);
        if (field == null) {
            return;
        }
        field.setAccessible(true);
        try {

            if(value == null){
                return;
            }

            Class<?> fieldClass = field.getType();
            if(Integer.class.isAssignableFrom(fieldClass) || int.class.isAssignableFrom(fieldClass)){

                field.set(this, Integer.valueOf(value.toString()));
            }else if(Date.class.isAssignableFrom(fieldClass)){

                field.set(this,new SimpleDateFormat(dateFormat,Locale.CHINA).parse(value.toString()));
            }else if(Float.class.isAssignableFrom(fieldClass) || float.class.isAssignableFrom(fieldClass)){

                field.set(this, Float.valueOf(value.toString()));
            }else if(Double.class.isAssignableFrom(fieldClass) || double.class.isAssignableFrom(fieldClass)){

                field.set(this, Double.valueOf(value.toString()));
            }else if(String.class.isAssignableFrom(fieldClass)){

                field.set(this, value);
            }else if(Short.class.isAssignableFrom(fieldClass) || short.class.isAssignableFrom(fieldClass)){

                field.set(this, Short.valueOf(value.toString()));
            }else if(Long.class.isAssignableFrom(fieldClass) || long.class.isAssignableFrom(fieldClass)){

                field.set(this, Long.valueOf(value.toString()));
            }else if(Boolean.class.isAssignableFrom(fieldClass) || boolean.class.isAssignableFrom(fieldClass)){

                field.set(this, Integer.valueOf(value.toString()) == 1);
            }else if(Byte.class.isAssignableFrom(fieldClass) || byte.class.isAssignableFrom(fieldClass)){

                field.set(this, Byte.valueOf(value.toString()));
            }else if(Byte[].class.isAssignableFrom(fieldClass) || byte[].class.isAssignableFrom(fieldClass)){

                field.set(this, value);
            }else if(BigDecimal.class.isAssignableFrom(fieldClass)){

                field.set(this, new BigDecimal(value.toString()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getValue(String columnName) {

        Field field = findFieldByColumnName(columnName);
        if (field == null) {
            return null;
        }
        try {

            field.setAccessible(true);

            Object value = field.get(this);

            Class<?> fieldClass = field.getType();
            if(value != null && Date.class.isAssignableFrom(fieldClass)) {

                return new SimpleDateFormat(dateFormat,Locale.CHINA).format(value);
            }else if(value != null && Boolean.class.isAssignableFrom(fieldClass) || boolean.class.isAssignableFrom(fieldClass)){

                return (boolean)value ? 1 : 0;
            }else if(value != null && BigDecimal.class.isAssignableFrom(fieldClass)){

                return value.toString();
            }
            return value;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Type getType(String columnName) {

        Field field = findFieldByColumnName(columnName);
        if (field == null) {
            return null;
        }
        Column column = field.getAnnotation(Column.class);
        if (column == null) {
            return null;
        }
        return column.type();
    }

    @Override
    public List<String> columnNames() {

        Field[] fields = getClass().getDeclaredFields();
        if (fields == null) {
            return null;
        }
        List<String> columnList = new ArrayList<>();
        for (Field field : fields) {

            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            String columnName = column.name();
            if (columnName.length() <= 0) {
                columnName = field.getName();
            }
            columnList.add(columnName);
        }
        return columnList;
    }

    @Override
    public Map<String, Object> values() {

        List<String> columnNames = columnNames();
        if (columnNames == null) {
            return null;
        }

        Map<String, Object> values = new HashMap<>(columnNames.size());
        for (String columnName :
                columnNames) {
            values.put(columnName, getValue(columnName));
        }
        return values;
    }

    @Override
    public ColumnInfo info(String columnName) {

        Field field = findFieldByColumnName(columnName);
        if (field == null) {
            return null;
        }
        Column column = field.getAnnotation(Column.class);
        if(column == null){
            return null;
        }

        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setAutoIncrement(column.autoIncrement());
        columnInfo.setPrimaryKey(column.primaryKey());
        columnInfo.setLength(column.length());
        columnInfo.setType(column.type());
        columnInfo.setColumnName(columnName);
        return columnInfo;
    }

    @Override
    public String getTableName() {

        Table table = getClass().getAnnotation(Table.class);
        if(table == null){
            throw new RuntimeException(getClass().getName() + " must has annotation " + Table.class.getName());
        }
        return table.name();
    }

    protected Field findFieldByColumnName(String columnName) {

        Field[] fields = getClass().getDeclaredFields();
        if (fields == null) {
            return null;
        }
        for (Field field : fields) {

            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            String fieldColumnName = column.name();
            if (fieldColumnName.length() <= 0) {
                fieldColumnName = field.getName();
            }
            if (fieldColumnName.equals(columnName)) {
                return field;
            }
        }
        return null;
    }

}
