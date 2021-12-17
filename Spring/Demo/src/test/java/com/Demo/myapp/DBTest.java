package com.Demo.myapp;

import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.Test;

public class DBTest {
@Test
public void test() throws Exception {
    Class.forName("org.mariadb.jdbc.Driver"); // 마리아DB
    // Class.forName("com.mysql.jdbc.Driver"); MySQL
    //13.209.74.171:3306
    //"jdbc:mariadb://admin.cale1xkqt56e.ap-northeast-2.rds.amazonaws.com:3306/FineDustJava", "admin","javaproject!!"
    Connection con = DriverManager.getConnection("jdbc:mariadb://admin.cale1xkqt56e.ap-northeast-2.rds.amazonaws.com:3306/FineDustJava", "admin","javaproject!!");// 마리아DB
    // Connection con =     DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test","root","passwd"); MySQL
    System.out.println(con);
    }
}