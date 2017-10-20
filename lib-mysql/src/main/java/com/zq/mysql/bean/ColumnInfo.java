package com.zq.mysql.bean;

import java.sql.Types;

/**
 * Created by zhangqiang on 2017/8/24.
 */

public class ColumnInfo {

    private boolean isPrimaryKey;

    private boolean isAutoIncrment;

    private Types type;

    private String name;

    private int length;

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isAutoIncrment() {
        return isAutoIncrment;
    }

    public void setAutoIncrment(boolean autoIncrment) {
        isAutoIncrment = autoIncrment;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
