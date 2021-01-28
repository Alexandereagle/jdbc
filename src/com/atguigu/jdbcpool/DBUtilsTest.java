package com.atguigu.jdbcpool;

import com.atguigu.jdbc.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBUtilsTest {

    public QueryRunner qr = new QueryRunner();

    //1. 添加数据到数据库
    @Test
    public void test1(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers(id, name, email, birth) values(?,?,?,?)";
            int row = qr.update(conn, sql, 22, "zhangsan", "zhangsan@abc.com", "1999-10-10");
            System.out.println("已影响" + row + "行");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, null, null);
        }
    }

    //2. 查询单个对象
    @Test
    public void test2(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id = ?";
            // BeanHandler
            Customer cust = qr.query(conn, sql, new BeanHandler<Customer>(Customer.class), 22);
            System.out.println(cust);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, null, null);
        }
    }

    //3. 查询多个对象并存入 List 中
    @Test
    public void test3(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id <= ?";
            List<Customer> list = qr.query(conn, sql, new BeanListHandler<Customer>(Customer.class), 22);

            for (Customer customer : list) {
                System.out.println(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            com.atguigu.jdbc.JDBCUtils.close(conn, null, null);
        }
    }

    //4. 查询多个对象组成集合
    @Test
    public void test4(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id <= ?";
            List<Map<String, Object>> list = qr.query(conn, sql, new MapListHandler(), 22);

            for (Map<String, Object> map : list) {
                Set<Map.Entry<String, Object>> entries = map.entrySet();

                for (Map.Entry<String, Object> entry : entries) {
                    System.out.println(entry.getKey() + " === " + entry.getValue());
                }

                System.out.println("--------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            com.atguigu.jdbc.JDBCUtils.close(conn, null, null);
        }
    }

    //5. 查询特殊值
    @Test
    public void test5(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            conn.setAutoCommit(false);
//            String sql = "select max(birth) from customers";
            String sql = "select count(*) from customers";
            Object max = qr.query(conn, sql, new ScalarHandler());
            System.out.println(max);

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            com.atguigu.jdbc.JDBCUtils.close(conn, null, null);
        }
    }
}
