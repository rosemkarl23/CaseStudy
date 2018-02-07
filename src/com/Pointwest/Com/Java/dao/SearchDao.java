package com.Pointwest.Com.Java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.Pointwest.Com.Java.beans.EmployeeBean;
import com.Pointwest.Com.Java.beans.SeatBean;

public class SearchDao extends PeopleLocatorDao {
	Logger logger = Logger.getLogger(LoginDao.class);

	//METHOD TO RETURN A SEARCH LIST OF EMPLOYEES GIVEN THE KEYWORD AND THE USER SELECTION
	//SELECTION 1 IS FOR SEARCHING BY EMPLOYEE ID
	//SELECTION 2 IS FOR SEARCHING BY EMPLOYEE NAME
	//SELECTION 3 IS FOR SEARCHING BY PROJECT
	public List<EmployeeBean> searchEmployee(String keyword, int selection){
		logger.info("searchEmployee(String keyword, int selection) - Start");
		
		List<EmployeeBean> employeeList = new ArrayList<>();
		
		Connection conn = retrieveConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			if (selection == 1) {
				ps = conn.prepareStatement("SELECT employee.emp_id, employee.first_name, employee.last_name,"
						+ " CONCAT(seat.bldg_id, seat.floor_number, 'F', seat.quadrant,"
						+ " seat.row_number,'-', seat.column_number) AS ConcatenatedString,"
						+ " seat.bldg_id, seat.floor_number, seat.quadrant,"
						+ " seat.row_number, seat.column_number,"
						+ " seat.local_number, employee.shift,"
						+ "	GROUP_CONCAT(employee_project.proj_alias ) AS GroupedProjects"
						+ " FROM ((((employee"
						+ " INNER JOIN employee_seat ON employee.emp_id = employee_seat.emp_id)"
						+ " INNER JOIN seat ON employee_seat.seat_id = seat.seat_id)"
						+ " INNER JOIN employee_project ON employee.emp_id = employee_project.employee_id)"
						+ " INNER JOIN project ON employee_project.proj_alias = project.proj_alias)"
						+ " WHERE employee.emp_id LIKE ?"
						+ " GROUP BY ConcatenatedString"
						+ " ORDER BY employee.emp_id");
			} else if (selection == 2) {
				ps = conn.prepareStatement("SELECT employee.emp_id, employee.first_name, employee.last_name,"
						+ " CONCAT(seat.bldg_id, seat.floor_number, 'F', seat.quadrant,"
						+ " seat.row_number,'-', seat.column_number) AS ConcatenatedString,"
						+ " seat.bldg_id, seat.floor_number, seat.quadrant,"
						+ " seat.row_number, seat.column_number,"
						+ " seat.local_number, employee.shift,"
						+ "	GROUP_CONCAT(employee_project.proj_alias ) AS GroupedProjects"
						+ " FROM ((((employee"
						+ " INNER JOIN employee_seat ON employee.emp_id = employee_seat.emp_id)"
						+ " INNER JOIN seat ON employee_seat.seat_id = seat.seat_id)"
						+ " INNER JOIN employee_project ON employee.emp_id = employee_project.employee_id)"
						+ " INNER JOIN project ON employee_project.proj_alias = project.proj_alias)"
						+ " WHERE CONCAT(employee.first_name, ' ', employee.last_name) LIKE ?"
						+ " GROUP BY ConcatenatedString"
						+ " ORDER BY employee.emp_id");
			} else {
				ps = conn.prepareStatement("SELECT employee.emp_id, employee.first_name, employee.last_name,"
						+ " CONCAT(seat.bldg_id, seat.floor_number, 'F', seat.quadrant,"
						+ " seat.row_number,'-', seat.column_number) AS ConcatenatedString," 
						+ " seat.bldg_id, seat.floor_number, seat.quadrant," 
						+ " seat.row_number, seat.column_number," 
						+ " seat.local_number, employee.shift," 
						+ " GROUP_CONCAT(employee_project.proj_alias ) AS GroupedProjects"
						+ " FROM ((((employee" 
						+ " INNER JOIN employee_seat ON employee.emp_id = employee_seat.emp_id)" 
						+ " INNER JOIN seat ON employee_seat.seat_id = seat.seat_id)" 
						+ " INNER JOIN employee_project ON employee.emp_id = employee_project.employee_id)" 
						+ " INNER JOIN project ON employee_project.proj_alias = project.proj_alias)" 
						+ " WHERE employee.emp_id IN (SELECT employee.emp_id" 
						+ " FROM ((((employee" 
						+ " INNER JOIN employee_seat ON employee.emp_id = employee_seat.emp_id)" 
						+ " INNER JOIN seat ON employee_seat.seat_id = seat.seat_id)" 
						+ " INNER JOIN employee_project ON employee.emp_id = employee_project.employee_id)" 
						+ " INNER JOIN project ON employee_project.proj_alias = project.proj_alias)" 
						+ " WHERE employee_project.proj_alias LIKE ?" 
						+ " ORDER BY employee.emp_id)" 
						+ " GROUP BY ConcatenatedString");
			}

			ps.setString(1, "%" + keyword + "%");
			rs = ps.executeQuery();
		
			while(rs.next()) {
				SeatBean seat = new SeatBean();
				seat.setBuilding(rs.getString("seat.bldg_id"));
				seat.setFloor(rs.getString("seat.floor_number"));
				seat.setQuadrant(rs.getString("seat.quadrant"));
				seat.setRow(rs.getString("seat.row_number"));
				seat.setColumn(rs.getString("seat.column_number"));
				seat.setLocalNumber(rs.getString("seat.local_number"));
				
				EmployeeBean employee = new EmployeeBean();
				employee.setEmployeeID(rs.getString("employee.emp_id"));
				employee.setFirstName(rs.getString("employee.first_name"));
				employee.setLastName(rs.getString("employee.last_name"));
				employee.setShift(rs.getString("employee.shift"));
				employee.setProject(rs.getString("GroupedProjects"));
				employee.setSeat(seat);
				
				employeeList.add(employee);
			}
		} catch (SQLException e){
			logger.error("SQL Query error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		} finally {
			closeConnection();
		}
		logger.info("searchEmployee(String keyword, int selection) - End");
		
		return employeeList;
	}
}