package org.spring.sample;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

public class MariaDBConnectionTest {
	
	@Test
	public void test() throws Exception{
		// 마리아DB
		Class.forName("org.mariadb.jdbc.Driver");
		
		Connection con = DriverManager.getConnection("jdbc:mariadb://admin.cale1xkqt56e.ap-northeast-2.rds.amazonaws.com:3306/test", "admin", "javaproject!!");
		
		System.out.println("\n>>>>>>>>>>>> Connection 출력 : "+con+"\n");

	}
}
