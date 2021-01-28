package com.atguigu.jdbc;

import org.junit.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * 测试Mysql的连接方式
 */
public class JDBCConnect {
    //利用 MYSQL 厂商提供的驱动，获取与 MYSQL 数据库的连接
    @Test
    public void test1() throws Exception {
        String driverClass = "com.mysql.jdbc.Driver";

        Driver driver = null;

        //①获取驱动
        Class clazz = Class.forName(driverClass);
        driver = (Driver) clazz.newInstance();

        //②获取连接
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");

        Connection conn = driver.connect(url, info);

        System.out.println(conn);
    }

    //利用 DriverManager(驱动管理类)，获取数据库连接
    @Test
    public void test2() throws Exception {
        //①注册驱动
        String driverClass = "com.mysql.jdbc.Driver";
        Class clazz = Class.forName(driverClass);
        Driver driver = (Driver) clazz.newInstance();
        DriverManager.registerDriver(driver); //注册驱动，MySQL 厂商已经完成

        //②获取连接
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        String user = "root";
        String password = "123456";
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);
    }

    @Test
    public void test3() throws Exception {
        String driverClass = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        String user = "root";
        String password = "123456";

        //①加载驱动
        Class.forName(driverClass);

        //②获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);
    }

    //【相对完善写法】
    @Test
    public void test4() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("jdbc.properties"));
        String driverClassName = props.getProperty("driverClassName");
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");

        //①加载驱动
        Class.forName(driverClassName);

        //②获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);
    }
}
