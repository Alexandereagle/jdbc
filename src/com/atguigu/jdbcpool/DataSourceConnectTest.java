package com.atguigu.jdbcpool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnectTest {

    //使用 c3p0 连接池的方式一：
    @Test
    public void test1() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test");
        cpds.setUser("root");
        cpds.setPassword("123456");

        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(10);

        Connection conn = cpds.getConnection();

        System.out.println(conn);

        conn.close();
    }

    //使用 c3p0 连接池方式二：
    @Test
    public void test2() throws SQLException {
        // helloc3p0 是 c3p0-config.xml 的 <named-config name="helloc3p0">
        DataSource ds = new ComboPooledDataSource("helloc3p0");

        Connection conn = ds.getConnection();

        System.out.println(conn);
    }
}
