package com.atguigu.jdbc;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/*
使用 PreparedStatement 完成图片的处理
 */
public class JDBCPhoto {
    //查询
    @Test
    public void test2() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        InputStream in = null;
        FileOutputStream fos = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth, photo from customers where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, 21);

            rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                //将散列数据封装进对象
                Customer cust = new Customer(id, name, email, birth);
                System.out.println(cust);
                //处理图片
                Blob photo = rs.getBlob("photo");
                in = photo.getBinaryStream();
                fos = new FileOutputStream("2.jpg");

                byte[] b = new byte[1024];
                int len = 0;
                while((len = in.read(b)) != -1){
                    fos.write(b, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps, rs);

            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    //添加图片到数据库
    @Test
    public void test1(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers values(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, 21);
            ps.setString(2, "张三");
            ps.setString(3, "zhangsan@abc.com");
            ps.setDate(4, new Date(new java.util.Date().getTime()));

            //处理图片
            ps.setBlob(5, new FileInputStream("1.jpg"));

            int row = ps.executeUpdate();
            System.out.println("已影响" + row + "行");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps);
        }
    }
}
