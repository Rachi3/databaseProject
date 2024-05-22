package dbProject.dbConnect;

import java.sql.*;

public class dbConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/db1";
	
	//관리자 ID/PW
	private static final String adminUser="root";
	private static final String adminPassword="1234";
	
	//사용자 ID/PW
	private static final String normalUser="user1";
	private static final String normalPassword="user1";
	
	public static Connection dbConnect(boolean isAdmin) {
		try {
			if(isAdmin) return DriverManager.getConnection(URL,adminUser,adminPassword);//root 계정으로 접속
		    else return DriverManager.getConnection(URL,normalUser,normalPassword);//user 계정으로 접속
		}catch(SQLException e) {
			throw new RuntimeException("DB connection failed",e);
		}
	}//db연결해주는 매소드 관리자 계정인지 아닌지 확인후 연
}
