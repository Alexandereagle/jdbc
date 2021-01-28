package com.atguigu.jdbcpool;

import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 事务
 */
public class TransactionTest {

    @Test
    public void test1() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            //1. 取消自动提交(事务开始)
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(conn, sql1, "AA");

            //int i = 10 / 0; //模拟故障

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(conn, sql2, "BB");

            //2. 提交
            conn.commit();
        } catch (SQLException e) {

            //3. 回滚
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

    //考虑事务的通用查询，适用于任何表
    public <T> T get(Connection conn, Class<T> clazz, String sql, Object... args) {
        T t = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();

            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                t = clazz.newInstance();

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

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            com.atguigu.jdbc.JDBCUtils.close(null, ps, rs);
        }

        return t;
    }

    //考虑事务的通用增删改，适用于任何表
    public int update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        int row = 0;
        try {
            //2. 利用当前连接获取 PreparedStatement, 用于发送 SQL
            ps = conn.prepareStatement(sql);

            //3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //4. 执行 SQL
            row = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭连接
            JDBCUtils.close(null, ps, null);
        }

        return row;
    }

}
