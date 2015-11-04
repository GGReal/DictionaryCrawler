package com.crawler.jdbc;

import com.crawler.tools.Constant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by LVY on 2015/11/3.
 */
public class SqliteUtils {
    private Statement statement;

    private volatile static SqliteUtils sqliteUtils; //volatile关键字告诉多线程正确的处理这个对象

    private SqliteUtils(){

    }
    public static SqliteUtils GetSingleInstance(){
        if(sqliteUtils == null){
            synchronized (SqliteUtils.class){
                if (sqliteUtils == null){
                    sqliteUtils = new SqliteUtils();
                }
            }
        }
        return sqliteUtils;
    }

    public void InitializeDB(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite://"+ Constant.SAVE_PATH+"dictionary.db");
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void InsertCharacterToDB(String sql){
        synchronized (SqliteUtils.class){
            try {
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
