package com.zq.database.platform.mysql.dao;

import com.zq.database.bean.SQLBean;
import com.zq.database.bean.SQLBeanCreator;
import com.zq.database.dao.AbstractDao;
import com.zq.database.platform.mysql.sql.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MySQLDaoIMPL<T extends SQLBean> extends AbstractDao<T> {

    private ConnectionPool connectionPool;

    public MySQLDaoIMPL(Class<T> sqlBeanClass, SQLBeanCreator<T> sqlBeanCreator, ConnectionPool connectionPool) {
        super(sqlBeanClass, sqlBeanCreator);
        this.connectionPool = connectionPool;
    }

    @Override
    public List<T> query(String[] columns, String selection, Object[] selectionArgs, String groupBy, String having, String orderBy, String limit) {

        Connection connection = connectionPool.poll();
        if (connection == null) {
            return null;
        }

        try {

            StringBuilder sql = new StringBuilder("SELECT ");
            if (columns != null && columns.length > 0) {

                for (int i = 0; i < columns.length; i++) {

                    String column = columns[i];
                    sql.append(column);
                    if (i != columns.length - 1) {
                        sql.append(",");
                    }
                }
            }else{

                sql.append("*");
            }
            sql.append(" FROM ");
            sql.append(getTableName());
            sql.append(" WHERE ");
            sql.append(selection);

            if (groupBy != null) {

                sql.append(" GROUP BY ");
                sql.append(groupBy);
            }

            if (having != null) {

                sql.append(" HAVING ");
                sql.append(having);
            }

            if (orderBy != null) {

                sql.append(" ORDER BY ");
                sql.append(orderBy);
            }

            if (limit != null) {

                sql.append(" LIMIT ");
                sql.append(limit);
            }
            sql.append(";");

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            if (selectionArgs != null && selectionArgs.length > 0) {

                for (int i = 1; i <= selectionArgs.length; i++) {

                    Object arg = selectionArgs[i - 1];
                    preparedStatement.setObject(i, arg);
                }
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();

            final int columnCount = metaData.getColumnCount();

            List<T> dataList = new ArrayList<>();

            while (resultSet.next()) {

                T sqlBean = createSQLBean();

                for (int i = 1; i <= columnCount ; i++) {

                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(columnName);
                    sqlBean.setValue(columnName, value);
                }
                dataList.add(sqlBean);
            }
            return dataList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.offer(connection);
        }
        return null;
    }


    @Override
    public int update(String[] columns, Object[] values, String whereClause, Object[] whereArgs) {

        Connection connection = connectionPool.poll();
        if (connection == null) {
            return 0;
        }

        try {

            if (columns == null || values == null || columns.length != values.length || whereClause == null) {
                return 0;
            }

            StringBuilder sql = new StringBuilder("UPDATE ");
            sql.append(getTableName());
            sql.append(" SET ");

            for (int i = 0; i < columns.length; i++) {

                String column = columns[i];

                sql.append(column);
                sql.append("=?");
                if (i != columns.length - 1) {
                    sql.append(",");
                }
            }
            sql.append(" WHERE ");
            sql.append(whereClause);

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            for (int i = 1; i <= values.length; i++) {

                preparedStatement.setObject(i,values[i - 1]);
            }

            if (whereArgs != null && whereArgs.length > 0) {

                int offset = values.length;

                for (int i = offset + 1; i <= whereArgs.length + offset; i++) {

                    Object arg = whereArgs[i - 1 - offset];
                    preparedStatement.setObject(i, arg);
                }
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.offer(connection);
        }
        return 0;
    }

    @Override
    @Deprecated
    public int update(List<T> dataList) {
        return 0;
    }

    @Override
    public int update(T sqlBean, String whereClause, Object[] whereArgs) {

        List<String> columnList = sqlBean.columnNames();
        List<String> updateColumnList = new ArrayList<>();
        List<Object> updateValuesList = new ArrayList<>();
        for (int i = 0; i < columnList.size(); i++) {

            String columnName = columnList.get(i);
            SQLBean.ColumnInfo columnInfo = sqlBean.info(columnName);
            if(columnInfo.isPrimaryKey()){
                continue;
            }
            updateColumnList.add(columnName);
            updateValuesList.add(sqlBean.getValue(columnName));
        }
        String[] columns = new String[updateColumnList.size()];
        Object[] values = new Object[columns.length];
        updateColumnList.toArray(columns);
        updateValuesList.toArray(values);

        return update(columns,values,whereClause,whereArgs);
    }

    @Override
    public int delete(String whereClause, Object[] whereArgs) {

        Connection connection = connectionPool.poll();
        if (connection == null) {
            return 0;
        }

        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(getTableName());
        sql.append(" WHERE ");
        sql.append(whereClause);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            if (whereArgs != null && whereArgs.length > 0) {

                for (int i = 1; i <= whereArgs.length; i++) {

                    Object arg = whereArgs[i - 1];
                    preparedStatement.setObject(i, arg);
                }
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            connectionPool.offer(connection);
        }
        return 0;
    }

    @Override
    public long insert(String[] columns, Object[] values) {

        Connection connection = connectionPool.poll();
        if (connection == null) {
            return 0;
        }

        try {

            if (columns == null || values == null || columns.length != values.length) {
                return 0;
            }

            StringBuilder sql = new StringBuilder("INSERT INTO ");
            sql.append(getTableName());
            sql.append(" ( ");

            String valuesTemp = " VALUES ( ";
            for (int i = 0; i < columns.length; i++) {

                String column = columns[i];

                sql.append(column);
                valuesTemp += "?";
                if (i != columns.length - 1) {
                    sql.append(",");
                    valuesTemp += ",";
                }
            }
            sql.append(")");
            valuesTemp += ")";
            sql.append(valuesTemp);

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            for (int i = 1; i <= values.length; i++) {

                Object arg = values[i - 1];
                preparedStatement.setString(i, arg == null ? null : arg.toString());
            }
            return preparedStatement.execute() ? 1 : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.offer(connection);
        }
        return 0;
    }

    @Override
    public boolean insert(List<T> dataList) {

        Connection connection = connectionPool.poll();
        if (connection == null) {
            return false;
        }

        try {

            if (dataList == null || dataList.isEmpty()) {
                return false;
            }
            connection.setAutoCommit(false);
            for (T sqlBean :
                    dataList) {

                List<String> columnList = sqlBean.columnNames();
                String[] columns = new String[columnList.size()];
                Object[] values = new Object[columns.length];
                columnList.toArray(columns);
                for (int i = 0; i < columns.length; i++) {

                    values[i] = sqlBean.getValue(columns[i]);
                }

                StringBuilder sql = new StringBuilder("INSERT INTO ");
                sql.append(getTableName());
                sql.append(" ( ");

                String valuesTemp = " VALUES ( ";
                for (int i = 0; i < columns.length; i++) {

                    String column = columns[i];

                    sql.append(column);
                    valuesTemp += "?";
                    if (i != columns.length - 1) {
                        sql.append(",");
                        valuesTemp += ",";
                    }
                }
                sql.append(")");
                valuesTemp += ")";
                sql.append(valuesTemp);

                PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

                for (int i = 1; i <= values.length; i++) {

                    Object arg = values[i - 1];
                    preparedStatement.setString(i, arg.toString());
                }

                preparedStatement.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connectionPool.offer(connection);
        }
        return false;
    }

    @Override
    public long insert(T data) {

        List<String> columnList = data.columnNames();
        String[] columns = new String[columnList.size()];
        Object[] values = new Object[columns.length];
        columnList.toArray(columns);
        for (int i = 0; i < columns.length; i++) {

            values[i] = data.getValue(columns[i]);
        }
        return insert(columns, values);
    }

    @Override
    public void execute(String sql, String[] args) throws SQLException {

        Connection connection = connectionPool.poll();
        PreparedStatement statement = connection.prepareStatement(sql);
        if(args != null){

        }
    }

//    private static void fillParams(PreparedStatement preparedStatement,Object[] args){
//
//        if(args == null || args.length <= 0){
//            return;
//        }
//
//        for (int i = 0; i < args.length; i++) {
//
//            Object object = args[i];
//            if(object == null){
//
//
//            }
//        }
//    }
}
