package com.atguigu.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/*
使用 PreparedStatement 完成批量处理
 */
public class JDBCBatch {
    @Test
    public void test2(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into emp values(?,?)";
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < 100000; i++) {
                ps.setInt(1, i+1);
                ps.setString(2, "emp_" + i);

                //积攒 SQL 语句
                ps.addBatch();
                if((i+1) % 500 == 0){
                    //批量处理 SQL 语句
                    ps.executeBatch();

                    //清空 SQL 语句
                    ps.clearBatch();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps);
        }
    }
}
