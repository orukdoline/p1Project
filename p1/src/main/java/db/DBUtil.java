package db;

import java.sql.*;

public class DBUtil {
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/p1", "root", "java1234");
		return conn;
	}

}
