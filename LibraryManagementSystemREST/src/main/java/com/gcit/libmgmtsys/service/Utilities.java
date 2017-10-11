/**
 * @Author Shuo Zhang <shuo_zhang@gcitsolutions.com>
 * @Date Sep 28, 2017
 */
package com.gcit.libmgmtsys.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utilities {
	private final String DRIVER   = "com.mysql.cj.jdbc.Driver";
	private final String URL      = "jdbc:mysql://localhost/library?useSSL = false";
	private final String USER     = "root";
	private final String PASSWORD = "root";
	
	//Create a connection to the database
	protected Connection getConnection() 
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName(DRIVER).newInstance();
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		conn.setAutoCommit(Boolean.FALSE);
		return conn;
	}
}
