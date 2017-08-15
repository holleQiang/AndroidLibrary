package com.zq.db.annotation;

/**
 * Created by zhangqiang on 17-6-8.
 */

public enum Type {

    INTEGER("INTEGER","INT"),

    VARCHAR("VARCHAR"),

    CHAR("CHAR"),

    BIGINT("BIGINT"),

    FLOAT("FLOAT"),

    DOUBLE("DOUBLE"),

    DECIMAL("DECIMAL"),

    TINYINT("TINYINT UNSIGNED"),

    BOOLEAN("TINYINT UNSIGNED"),

    DATE("DATE"),

    TIME("TIME"),

    DATETIME("DATETIME"),

    TIMESTAMP("TIMESTAMP"),

    TEXT("TEXT"),

    REAL("REAL"),

    NULL("NULL"),

    BLOB("BLOB");



    private String value;
    private String[] compareTypes;

    Type(String value, String... compareTypes) {
        this.value = value;
        this.compareTypes = compareTypes;
    }

    public String getValue() {
        return value;
    }

    public String[] getCompareTypes() {
        return compareTypes;
    }
}
