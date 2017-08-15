package com.zq.db.bean;

import com.zq.db.annotation.Type;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by zhangqiang on 17-6-20.
 */

public class CachedReflectSQLBean extends ReflectSQLBean{

    private static final Map<Class,Map<String,Field>> findFieldCache = new WeakHashMap<>();
    private static final Map<Class,Map<String,Type>> getTypeCache = new WeakHashMap<>();
    private static final Map<Class,List<String>> getColumnNamesCache = new WeakHashMap<>();
    private static final Map<Class,String> getTableNameCache = new WeakHashMap<>();

    @Override
    protected Field findFieldByColumnName(String columnName) {

        Map<String,Field> fieldMap = findFieldCache.get(getClass());
        if(fieldMap == null){
            fieldMap = new HashMap<>();
            synchronized (findFieldCache){
                findFieldCache.put(getClass(),fieldMap);
            }
        }
        Field field = fieldMap.get(columnName);
        if (field == null) {

            field = super.findFieldByColumnName(columnName);
            fieldMap.put(columnName, field);
        }
        return field;
    }

    @Override
    public Type getType(String columnName) {

        Map<String,Type> typeMap = getTypeCache.get(getClass());
        if(typeMap == null){
            typeMap = new HashMap<>();
            synchronized (getTypeCache){
                getTypeCache.put(getClass(),typeMap);
            }
        }
        Type type = typeMap.get(columnName);
        if (type == null) {

            type = super.getType(columnName);
            typeMap.put(columnName, type);
        }
        return type;
    }

    @Override
    public List<String> columnNames() {

        List<String> columnNames = getColumnNamesCache.get(getClass());
        if(columnNames == null){
            columnNames = super.columnNames();
            synchronized (getColumnNamesCache){
                getColumnNamesCache.put(getClass(),columnNames);
            }
        }
        return columnNames;
    }

    @Override
    public String getTableName() {

        String tableName = getTableNameCache.get(getClass());
        if(tableName == null){
            tableName = super.getTableName();
            getTableNameCache.put(getClass(),tableName);
        }
        return tableName;
    }
}
