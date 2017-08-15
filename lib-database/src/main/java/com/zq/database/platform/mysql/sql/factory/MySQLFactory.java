package com.zq.db.platform.mysql.sql.factory;

import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.platform.mysql.sql.MySQL;
import com.zq.db.sql.SQL;
import com.zq.db.sql.factory.SQLFactory;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MySQLFactory implements SQLFactory{

    private SQL sql;
    private String url;
    private String userName;
    private String password;

    public MySQLFactory(  String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public SQL getSQL() {

        if(sql == null){
            sql = new MySQL(url,userName,password);
        }
        return sql;
    }
}
