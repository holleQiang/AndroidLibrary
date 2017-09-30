package com.zq.database.db;

/**
 * Created by zhangqiang on 2017/8/24.
 */

public interface DBHandler {

    void createDatabase(String dbName);

    void dropDatabase(String dbName);
}
