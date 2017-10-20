package com.zq.mysql.annotations;

/**
 * {@link java.sql.Types}
 * Created by zhangqiang on //.
 */

public enum Type {

    BIT("BIT"),
    TINYINT("TINYINT"),
    SMALLINT("SMALLINT"),
    INTEGER("INTEGER"),
    BIGINT("BIGINT"),
    FLOAT("FLOAT"),
    REAL("REAL"),
    DOUBLE("DOUBLE"),
    NUMERIC("NUMERIC"),
    DECIMAL("DECIMAL"),
    CHAR("CHAR"),
    VARCHAR("VARCHAR"),
    LONGVARCHAR("LONGVARCHAR"),
    DATE("DATE"),
    TIME("TIME"),
    TIMESTAMP("TIMESTAMP"),
    BINARY("BINARY"),
    VARBINARY("VARBINARY"),
    LONGVARBINARY("LONGVARBINARY"),
    NULL("NULL"),
    OTHER("OTHER"),
    JAVA_OBJECT("JAVA_OBJECT"),
    DISTINCT("DISTINCT"),
    STRUCT("STRUCT"),
    ARRAY("ARRAY"),
    BLOB("BLOB"),
    CLOB("CLOB"),
    REF("REF"),
    DATALINK("DATALINK"),
    BOOLEAN("BOOLEAN"),
    ROWID("ROWID"),
    NCHAR("NCHAR"),
    NVARCHAR("NVARCHAR"),
    LONGNVARCHAR("LONGNVARCHAR"),
    NCLOB("NCLOB"),
    SQLXML("SQLXML"),
    REF_CURSOR("REF_CURSOR"),
    TIME_WITH_TIMEZONE("TIME_WITH_TIMEZONE"),
    TIMESTAMP_WITH_TIMEZONE("TIMESTAMP_WITH_TIMEZONE");

    private String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
