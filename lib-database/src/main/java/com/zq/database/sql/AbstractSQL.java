package com.zq.database.sql;

import com.zq.database.db.DBHandler;

/**
 * Created by zhangqiang on 17-6-21.
 */

public abstract class AbstractSQL implements SQL {

    @Override
    public DBHandler getDBHandler() {
        throw new RuntimeException("method has not been implemented");
    }
}
