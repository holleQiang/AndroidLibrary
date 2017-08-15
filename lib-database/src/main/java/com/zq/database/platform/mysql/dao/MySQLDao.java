package com.zq.db.platform.mysql.dao;

import com.zq.db.bean.SQLBean;
import com.zq.db.dao.Dao;

import java.sql.Connection;

public interface MySQLDao<T extends SQLBean> extends Dao<T> {

}
