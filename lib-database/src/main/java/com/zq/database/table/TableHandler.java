package com.zq.db.table;

import com.zq.db.bean.SQLBean;

/**
 * Created by zhangqiang on 17-7-4.
 */

public interface TableHandler {

    String getTableCreateSQL();

    String getTableDropSQL();

    boolean create();

    boolean drop();

    boolean exist();

    boolean update();

    boolean isChange();

    String[] columns();
}
