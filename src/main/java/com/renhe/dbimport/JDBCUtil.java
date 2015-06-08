package com.renhe.dbimport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

	private static final String DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@192.168.89.64:1521:b2b";
	private static final String USER = "m2f";
	private static final String PASS_WORD = "m2f";

	public static Connection openConnection() {
		try {
			Class.forName(DRIVER_CLASS);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			return java.sql.DriverManager.getConnection(URL, USER, PASS_WORD);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {

			}
		}
	}
}
