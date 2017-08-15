package com.zq.database.platform.mysql.table;

import com.zq.database.annotation.Type;
import com.zq.database.bean.SQLBean;
import com.zq.database.platform.mysql.sql.ConnectionPool;
import com.zq.database.table.TableHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhangqiang on 17-7-4.
 */

public class MySQLTableHandler<T extends SQLBean> implements TableHandler {

    private T sqlBean;
    private ConnectionPool connectionPool;

    public MySQLTableHandler(T sqlBean, ConnectionPool connectionPool) {
        this.sqlBean = sqlBean;
        this.connectionPool = connectionPool;
    }

    @Override
    public String getTableCreateSQL() {

        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append(sqlBean.getTableName());

        List<String> columnNames = sqlBean.columnNames();

        if (columnNames != null && !columnNames.isEmpty()) {

            sql.append("(");

            final int count = columnNames.size();
            for (int i = 0; i < count; i++) {

                String columnName = columnNames.get(i);
                SQLBean.ColumnInfo columnInfo = sqlBean.info(columnName);

                sql.append(createColumnSQLInfo(columnInfo));

                if(i != count - 1){
                    sql.append(",");
                }
            }
            sql.append(")");
        }
        sql.append(";");

        return sql.toString();
    }

    @Override
    public String getTableDropSQL() {

        return  "DROP TABLE IF EXISTS " + sqlBean.getTableName() + ";";
    }

    @Override
    public boolean create() {

        Connection connection = connectionPool.poll();
        if(connection == null){
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getTableCreateSQL());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connectionPool.offer(connection);
        }
        return false;
    }

    @Override
    public boolean drop() {

        Connection connection = connectionPool.poll();
        if(connection == null){
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getTableDropSQL());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connectionPool.offer(connection);
        }
        return false;
    }

    @Override
    public boolean exist() {

        Connection connection = connectionPool.poll();
        if(connection == null){
            return false;
        }
        try {

            ResultSet resultSet = connection.getMetaData().getTables(null,null,sqlBean.getTableName(),null);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connectionPool.offer(connection);
        }
        return false;
    }


    @Override
    public boolean isChange() {

        Connection connection = connectionPool.poll();
        if(connection == null){
            return false;
        }
        try {


            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + sqlBean.getTableName());
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<String> columnList = sqlBean.columnNames();

            tag:for (String beanColumnName:
                    columnList) {

                SQLBean.ColumnInfo columnInfo = sqlBean.info(beanColumnName);

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);

                    if(columnName.equals(beanColumnName)){

                        if(isColumnChange(columnInfo,metaData,i)){

                            return true;
                        }
                        continue tag;
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();

        }finally {

            connectionPool.offer(connection);
        }
        return false;
    }

    @Override
    public String[] columns() {

        Connection connection = connectionPool.poll();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + sqlBean.getTableName());
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {

                columns[i - 1] = metaData.getColumnName(i);
            }
            return columns;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connectionPool.offer(connection);
        }
        return null;
    }

    @Override
    public boolean update() {


        Connection connection = connectionPool.poll();
        if(connection == null){
            return false;
        }
        try {

            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + sqlBean.getTableName());
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            List<String> columnList = sqlBean.columnNames();

            tag:for (String beanColumnName:
                    columnList) {

                SQLBean.ColumnInfo columnInfo = sqlBean.info(beanColumnName);

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);

                    if(columnName.equals(beanColumnName)){

                        if(isColumnChange(columnInfo,metaData,i)){

                            updateColumn(connection,sqlBean.getTableName(),columnInfo);
                        }
                        continue tag;
                    }
                }
                addColumn(connection,sqlBean.getTableName(),columnInfo);
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
        }finally {

            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connectionPool.offer(connection);
        }
        return false;
    }

    private static boolean isColumnChange(SQLBean.ColumnInfo columnInfo,ResultSetMetaData metaData,int index) throws SQLException{

        int length = metaData.getColumnDisplaySize(index);
        String columnTypeName = metaData.getColumnTypeName(index);
        boolean autoIncrement = metaData.isAutoIncrement(index);

//        System.out.println("===columnTypeName=" + columnTypeName + "=====length=" + length
//                +"=====autoIncrement=" + autoIncrement + "=========columnInfo=" + columnInfo.toString());

        if(columnInfo.getLength() > 0 &&  length != columnInfo.getLength()){

            System.out.println(1);
            return true;
        }

        if(!columnTypeName.equals(columnInfo.getType().getValue())){

            String[] compareTypes = columnInfo.getType().getCompareTypes();
            if(compareTypes == null) {
                return true;
            }

            for (String compareType:
                    compareTypes ) {

                if(columnTypeName.equals(compareType)){
                    return false;
                }
            }
            System.out.println(2);
            return true;
        }

        if(columnInfo.isAutoIncrement() ^ autoIncrement){

            System.out.println(3);
            return true;
        }
        return false;
    }

    private static String createColumnSQLInfo(SQLBean.ColumnInfo columnInfo){

        String info = columnInfo.getColumnName() + " " + columnInfo.getType().getValue();

        if(columnInfo.isPrimaryKey()){

            info += " PRIMARY KEY ";
            if(columnInfo.isAutoIncrement() && columnInfo.getType() == Type.INTEGER){
                info += " AUTO_INCREMENT ";
            }
        }

        if(columnInfo.getLength() > 0){
            info += "(" + columnInfo.getLength() + ")";
        }
        return info;
    }

    private static boolean addColumn(Connection connection, String tableName,SQLBean.ColumnInfo columnInfo) throws SQLException {

        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + createColumnSQLInfo(columnInfo);

        System.out.println(sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        return preparedStatement.execute();
    }

    private static boolean updateColumn(Connection connection, String tableName,SQLBean.ColumnInfo columnInfo) throws SQLException {

        String sql = "ALTER TABLE " + tableName + " MODIFY " + createColumnSQLInfo(columnInfo);

        System.out.println(sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        return preparedStatement.execute();
    }
}
