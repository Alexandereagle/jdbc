package com.atguigu.jdbc;

import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class JDBCPreparedStatement {

    @Test
    public void testUpdate() {
        String sql = "insert into customers(id, name, email, birth) values(?,?,?,?)";//?:占位符
        int row = update(sql, 25, "张三", "zhangsan@abc.com", "1999-09-09");
        System.out.println(row);
    }

    @Test
    public void testQuery() {
        String sql = "select id, name, email, birth from customers";
        List<Customer> customers = get(Customer.class, sql);
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    //通用的增删改，适用于任何表
    public int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        int row = 0;
        try {
            //1. 获取连接
            conn = JDBCUtils.getConnection();

            //2. 利用当前连接获取 PreparedStatement, 用于发送 SQL
            ps = conn.prepareStatement(sql);

            //3. 填充占位符,SQL的下表从1开始
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //4. 执行 SQL
            row = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭连接
            JDBCUtils.close(conn, ps);
        }
        return row;
    }

        /*
    需要改动的位置：
        1. 返回值类型处（我们不能确定返回什么类型的对象）
            ①利用泛型，确定返回值类型
            ②利用反射，在运行时创建运行时类的对象

        2. 结果集的处理（我们不能确定结果集的列数，每个列的名称是什么）
            ResultSetMetaData : 结果集的元数据
                >getColumnCount() : 获取结果集的列数
                >getColumnName(int columnIndex) : 获取结果集的列名
                >getColumnLabel(int columnIndex) : 获取列的别名，有别名获取别名，没别名获取列名
     */

    //（了解）通用查询，适用于任何表
    public <T> List<T> get(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();

            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            list = new ArrayList<T>();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列名
                    String columnName = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(columnName);

                    //利用反射为对象的属性设置值
                    //注意：必须保证生成结果集的列名与对象的属性名保持一致！！！！！！
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps, rs);
        }

        return list;
    }
}
