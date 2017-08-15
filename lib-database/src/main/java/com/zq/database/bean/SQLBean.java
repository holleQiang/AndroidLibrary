package com.zq.db.bean;

import com.zq.db.annotation.Type;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 17-6-19.
 */

public interface SQLBean {

    void setValue(String columnName, Object value);

    Object getValue(String columnName);

    Type getType(String columnName);

    List<String> columnNames();

    Map<String,Object> values();

    ColumnInfo info(String columnName);

    String getTableName();

    class ColumnInfo{

        private String columnName;

        private boolean isPrimaryKey;

        private boolean isAutoIncrement;

        private int length;

        private Type type;

        public boolean isPrimaryKey() {
            return isPrimaryKey;
        }

        public void setPrimaryKey(boolean primaryKey) {
            isPrimaryKey = primaryKey;
        }

        public boolean isAutoIncrement() {
            return isAutoIncrement;
        }

        public void setAutoIncrement(boolean autoIncrement) {
            isAutoIncrement = autoIncrement;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public String toString() {
            return "ColumnInfo{" +
                    "columnName='" + columnName + '\'' +
                    ", isPrimaryKey=" + isPrimaryKey +
                    ", isAutoIncrement=" + isAutoIncrement +
                    ", length=" + length +
                    ", type=" + type.getValue() +
                    '}';
        }
    }
}
