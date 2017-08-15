package com.zq.db.platform.mysql.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhangqiang on 17-7-4.
 */

public abstract class ConnectionPool {

    ConnectionPool(int maxSize) {
        connectionQueue = new LinkedBlockingQueue<>(maxSize);
    }

    private Queue<Connection> connectionQueue;

    public Connection poll(){

        Connection connection = connectionQueue.poll();

        try {
            if(connection == null){

                synchronized (this){

                    connection = openConnection();
                }
            }

            if(connection == null){
                return null;
            }

            if(connection.isClosed()){

                return poll();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean offer(Connection connection){

        try {
            if(connection == null || connection.isClosed()){
                return false;
            }
            return connectionQueue.add(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (IllegalStateException e){
            e.printStackTrace();
            System.out.print("==========池容量已达到上限");
        }
        return false;
    }

    public abstract Connection openConnection() throws SQLException;
}
