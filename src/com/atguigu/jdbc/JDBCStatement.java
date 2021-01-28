package com.atguigu.jdbc;

import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class JDBCStatement {
    // 弊端：需要拼写sql语句，并且存在SQL注入的问题
    @Test
    public void testLogin() {
        String userName = "AA";
        String password = "123456";

        String sql = "select user, password from user_table where user = '"
                + userName + "' and password = '" + password + "'";

        System.out.println(sql);

        User user = get(sql, User.class);
        if (user != null) {
            System.out.println("登陆成功!");
        } else {
            System.out.println("用户名或密码错误！");
        }
    }

    public <T> T get(String sql, Class<T> clazz) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        T t = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int count = resultSetMetaData.getColumnCount();
            t = null;
            if (resultSet.next()) {
                t = clazz.newInstance();
                for (int i = 0; i < count; i++) {

                    // 1. 获取列的别名
                    String columnName = resultSetMetaData.getColumnLabel(i + 1); // 注意：
                    // 获取结果集中（相当于数据表）列的名称（别名）

                    // 2. 根据列名获取对应数据表中的数据
                    Object columnVal = resultSet.getObject(columnName);

                    // 3. 将数据表中得到的数据，封装进对象
                    Field field = clazz.getDeclaredField(columnName); // 注意：反射根据Java中类的属性获取
                    // Field对象
                    field.setAccessible(true);
                    field.set(t, columnVal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection, statement, resultSet);
        }
        return t;
    }
}
