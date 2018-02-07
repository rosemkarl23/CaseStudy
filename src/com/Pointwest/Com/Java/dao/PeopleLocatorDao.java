package com.Pointwest.Com.Java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class PeopleLocatorDao {
	
	static Logger logger = Logger.getLogger(PeopleLocatorDao.class);
	
	Connection conn = null;
	Statement statement = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	
	//METHOD TO RETRIEVE CONNECTION TO DATABASE
	protected Connection retrieveConnection() {
		logger.info("retrieveConnection() - Start");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String db = "jdbc:mysql://localhost:3306/plsdb";
			String user = "root";
			String password = "abc123";
			conn = DriverManager.getConnection(db, user, password);
		} catch (ClassNotFoundException e) {
			logger.error("MySQL DB Driver not loaded successfully");
		} catch (SQLException e) {
			logger.error("Unable to establish connection to DB plsdb");
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		}
		logger.info("retrieveConnection() - End");
		
		return conn;
	}
	
	//METHOD TO CLOSE CONNECTION TO DATABASE
	protected void closeConnection() {
		logger.info("closeConnection() - Start");
		try {
			if (conn != null) {
				conn.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			logger.error("SQL Query error in closing connections: " + e.getMessage());
		}
		logger.info("closeConnection() - End");
	}
}