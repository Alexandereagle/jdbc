package com.atguigu.jdbc;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
    // 获取Mysql连接
    public static Connection getConnection() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("jdbc.properties"));
        String driverClassName = props.getProperty("driverClassName");
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");

        //①加载驱动
        Class.forName(driverClassName);

        //②获取连接
        return DriverManager.getConnection(url, user, password);
    }

    // 断开连接
    public static void close(Connection conn, Statement ps) {

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn, Statement ps, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        close(conn, ps);
    }
}
