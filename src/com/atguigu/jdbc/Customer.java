package com.atguigu.jdbc;

import java.sql.Date;
/*
    ORM （Object Relational Mapping）: 对象关系映射
    数据库中一张表   ----   Java 中的一个类
    数据表中一个字段   ----   Java 中的一个属性
    数据表中一条数据   ----  Java 中的一个对象
 */
public class Customer {

    private Integer id;
    private String name;
    private String email;
    private Date birth;

    public Customer() {
    }

    public Customer(Integer id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }
}
