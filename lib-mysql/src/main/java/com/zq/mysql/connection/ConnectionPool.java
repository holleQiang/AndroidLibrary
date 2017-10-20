package com.zq.mysql.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhangqiang on 2017/8/24.
 */

public class ConnectionPool {

    private static final int MAX_POLL_SIZE = 10;
    private static final ConnectionPool ourInstance = new ConnectionPool(MAX_POLL_SIZE);

    private String url;
    private String name;
    private String pwd;
    private boolean isInit;

    public static ConnectionPool getInstance() {
        return ourInstance;
    }

    private ConnectionPool(int maxSize) {
        connectionQueue = new LinkedBlockingQueue<>(maxSize);
    }

    private Queue<Connection> connectionQueue;

    public Connection poll(){

        checkInit();

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

        checkInit();

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

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url,name,pwd);
    }

    public void init(String url,String name,String pwd){

        this.url = url;
        this.name = name;
        this.pwd = pwd;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            isInit  = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkInit(){

        if(!isInit){
            throw new RuntimeException("init fail or not init");
        }
    }
}
