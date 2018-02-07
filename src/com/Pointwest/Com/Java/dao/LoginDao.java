package com.Pointwest.Com.Java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.Pointwest.Com.Java.beans.EmployeeBean;
import com.Pointwest.Com.Java.dao.LoginDao;

public class LoginDao extends PeopleLocatorDao {
	Logger logger = Logger.getLogger(LoginDao.class);
		
	//METHOD TO VALIDATE IF THE PASSWORD INPUTTED MATCHES THE PASSWORD OF THE USERNAME
	public EmployeeBean validateUser(String username, String password){
		logger.info("validateUser(String username, String password) - Start");
		
		Connection conn = retrieveConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;

		EmployeeBean user = new EmployeeBean();
		
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, role"
					+ " FROM employee"
					+ " WHERE binary username = ?"
					+ " AND binary password = ?");
			
			ps.setString(1, username);
			ps.setString(2, password);
			rs = ps.executeQuery();

			while (rs.next()) {
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setUserRole(rs.getString("role"));
			}
		} catch (SQLException e){
			logger.error("SQL Query error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		} finally {
			closeConnection();
		}
		logger.info("searchUserByInput(String username) - END");
		return user;
	}
}