package com.zq.db.platform.android.table;

import com.zq.db.annotation.Type;
import com.zq.db.bean.SQLBean;
import com.zq.db.bean.SQLBeanCreator;
import com.zq.db.table.TableHandler;

import java.util.List;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class AndroidTableHandler<T extends SQLBean> implements TableHandler {

    private T sqlBean;

    public AndroidTableHandler(T sqlBean) {
        this.sqlBean = sqlBean;
    }

    @Override
    public String getTableCreateSQL() {

        String tableCreateSql = "CREATE TABLE IF NOT EXISTS " + sqlBean.getTableName();

        List<String> columnNames = sqlBean.columnNames();

        if(columnNames == null || columnNames.isEmpty()){

            tableCreateSql += ";";
        }else{

            tableCreateSql += "(";

            final int count = columnNames.size();
            for (int i = 0; i < count; i++) {

                String columnName = columnNames.get(i);

                SQLBean.ColumnInfo columnInfo = sqlBean.info(columnName);

                tableCreateSql += columnName + " " + columnInfo.getType().getValue();

                boolean isPrimary = columnInfo.isPrimaryKey();
                if(isPrimary){
                    tableCreateSql += " PRIMARY KEY";

                    if(columnInfo.isAutoIncrement() && columnInfo.getType() == Type.INTEGER){
                        tableCreateSql += " AUTOINCREMENT ";
                    }
                }

                if(i != count - 1){
                    tableCreateSql += " , ";
                }
            }
            tableCreateSql += ");";
        }
        return tableCreateSql;
    }

    @Override
    public String getTableDropSQL() {

        return  "drop table if exists " + sqlBean.getTableName() + ";";
    }

    @Override
    public boolean create() {
        throw new RuntimeException("method has not impl");
    }

    @Override
    public boolean drop() {
        throw new RuntimeException("method has not impl");
    }

    @Override
    public boolean exist() {
        throw new RuntimeException("method has not impl");
    }

    @Override
    public boolean update() {
        throw new RuntimeException("method has not impl");
    }

    @Override
    public boolean isChange() {
        throw new RuntimeException("method has not impl");
    }

    @Override
    public String[] columns() {
        throw new RuntimeException("method has not impl");
    }

}
