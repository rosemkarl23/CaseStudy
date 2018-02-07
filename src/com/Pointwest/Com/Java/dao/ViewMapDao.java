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

public class ViewMapDao extends PeopleLocatorDao {
	Logger logger = Logger.getLogger(LoginDao.class);
	
	//METHOD TO RETURN CURRENT LOCATIONS IN THE DATABASE
	public String returnCurrentLocations(){
		logger.info("returnCurrentLocations() - Start");
		
		String validLocations = null;
		
		Connection conn = retrieveConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("SELECT GROUP_CONCAT(bldg_id)"
					+ " FROM (SELECT DISTINCT bldg_id FROM seat) AS TT");
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				validLocations = rs.getString("GROUP_CONCAT(bldg_id)");
			}
		} catch (SQLException e){
			logger.error("SQL Query error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		} finally {
			closeConnection();
		}
		logger.info("returnCurrentLocations() - End");
		
		return validLocations;
	}
	
	//METHOD TO RETURN CURRENT FLOORS GIVEN THE LOCATION IN THE DATABASE
	public String returnCurrentFloors(String location){
		logger.info("returnCurrentFloors(String location) - Start");
		
		String validFloors = null;
		
		Connection conn = retrieveConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("SELECT GROUP_CONCAT(floor_number)"
					+ " FROM (SELECT DISTINCT floor_number FROM seat"
					+ " WHERE bldg_id LIKE ?"
					+ " ) AS TT");
			
			ps.setString(1, location);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				validFloors = rs.getString("GROUP_CONCAT(floor_number)");
			}
		} catch (SQLException e){
			logger.error("SQL Query error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		} finally {
			closeConnection();
		}
		logger.info("returnCurrentFloors(String location) - End");
		
		return validFloors;
	}
	
	//METHOD TO VALIDATE THE LOCATION INPUTTED AGAINST DATABASE
	public boolean validateLocation (String location){
		logger.info("validateLocation (String location) - Start");
		
		Connection conn = retrieveConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("SELECT bldg_id"
					+ " FROM seat"
					+ " WHERE bldg_id = ?");
			
			ps.setString(1, location);
			rs = ps.executeQuery();

			if (rs.next() == false) {
				return true;
			}
		} catch (SQLException e){
			logger.error("SQL Query error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		} finally {
			closeConnection();
		}
		logger.info("validateLocation (String location) - End");
		
		return false;
	}
	
	//METHOD TO VALIDATE THE FLOOR INPUTTED AGAINST DATABASE
	public String validateFloor (String location, String floor){
		logger.info("validateFloor(String location, String floor) - Start");
						
		Connection conn = retrieveConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;

		String address = null;
		
		try {
			ps = conn.prepareStatement("SELECT location.bldg_address"
					+ " FROM (location"
					+ " INNER JOIN seat ON seat.bldg_id = location.bldg_id)"
					+ " WHERE location.bldg_id = ?"
					+ "	AND seat.floor_number = ?"
					+ " GROUP BY seat.floor_number");
			
			ps.setString(1, location);
			ps.setString(2, floor);
			rs = ps.executeQuery();

			while (rs.next()) {
				address = rs.getString("bldg_address");
			}	
		} catch (SQLException e){
			logger.error("SQL Query error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		} finally {
			closeConnection();
		}
		logger.info("validateFloor(String location, String floor) - End");
		
		return address;
	}	
	
	//METHOD TO VALIDATE THE QUADRANT INPUTTED AGAINST DATABASE
	public boolean validateQuadrant(String location, String floor, String quadrant){
		logger.info("validateQuadrant(String location, String floor, String quadrant) - Start");
			
		Connection conn = retrieveConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
			
		try {
			ps = conn.prepareStatement("SELECT location.bldg_address"
					+ " FROM (location"
					+ " INNER JOIN seat ON seat.bldg_id = location.bldg_id)"
					+ " WHERE location.bldg_id = ?"
					+ "	AND seat.floor_number = ?"
					+ " AND seat.quadrant = ?"
					+ " GROUP BY seat.floor_number");
			
			ps.setString(1, location);
			ps.setString(2, floor);
			ps.setString(3, quadrant);
			rs = ps.executeQuery();
				if (rs.next() == false) {
				return true;
			}
		} catch (SQLException e){
			logger.error("SQL Query error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		} finally {
			closeConnection();
		}
		logger.info("validateQuadrant (String location, String floor, String quadrant) - End");
		
		return false;
	}	
		
	//METHOD TO RETURN A SEARCH LIST OF EMPLOYEES AND SEATS GIVEN THE KEYWORD AND THE USER SELECTION
	//SELECTION 1 IS FOR SEARCHING BY LOCATION AND FLOOR
	//SELECTION 2 IS FOR SEARCHING BY LOCATION, FLOOR, AND QUADRANT
	public List<EmployeeBean> searchSeatMap(String location, String floor, String quadrant, int selection){
		logger.info("searchSeatMapByFloorLocation(String location, String floor) - Start");
		
		Connection conn = retrieveConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List<EmployeeBean> seatList = new ArrayList<>();
		
		try {
			if (selection == 1) {
				ps = conn.prepareStatement("SELECT employee.first_name, employee.last_name,"
						+ " CONCAT(seat.bldg_id, floor_number, 'F',quadrant, column_number, '-',row_number ) as ConcatenatedString,"
						+ " seat.bldg_id, seat.floor_number, seat.quadrant, seat.row_number, seat.column_number,"
						+ " seat.local_number, employee_seat.effect_date,"
						+ " CONCAT(seat.quadrant,"
						+ " seat.column_number,'-', seat.row_number) AS OrderString"
						+ " FROM ((seat"
						+ " LEFT JOIN employee_seat ON seat.seat_id = employee_seat.seat_id)"
						+ " LEFT JOIN employee ON employee.emp_id = employee_seat.emp_id)"
						+ " WHERE (SELECT"
						+ " (seat.bldg_id LIKE ?"
						+ " AND floor_number LIKE ?)"
						+ " AND (employee_seat.effect_date <= CURDATE()"
						+ " OR effect_date IS NULL))"
						+ " GROUP BY ConcatenatedString"
						+ " ORDER BY row_number, quadrant, column_number");
				
				ps.setString(1, location);
				ps.setString(2, floor);
				rs = ps.executeQuery();
			} else {
				ps = conn.prepareStatement("SELECT employee.first_name, employee.last_name,"
						+ " CONCAT(seat.bldg_id, floor_number, 'F',quadrant, column_number, '-',row_number ) as ConcatenatedString,"
						+ " seat.bldg_id, seat.floor_number, seat.quadrant, seat.row_number, seat.column_number,"
						+ " seat.local_number, employee_seat.effect_date,"
						+ " CONCAT(seat.quadrant,"
						+ " seat.column_number,'-', seat.row_number) AS OrderString"
						+ " FROM ((seat"
						+ " LEFT JOIN employee_seat ON seat.seat_id = employee_seat.seat_id)"
						+ " LEFT JOIN employee ON employee.emp_id = employee_seat.emp_id)"
						+ " WHERE (SELECT"
						+ " (seat.bldg_id LIKE ?"
						+ " AND seat.floor_number LIKE ?"
						+ " AND seat.quadrant LIKE ?)"
						+ " AND (employee_seat.effect_date <= CURDATE()"
						+ " OR effect_date IS NULL))"
						+ " GROUP BY ConcatenatedString"
						+ " ORDER BY row_number, quadrant, column_number");
				
				ps.setString(1, location);
				ps.setString(2, floor);
				ps.setString(3, quadrant);
				rs = ps.executeQuery();
			}

			while(rs.next()) {
				EmployeeBean employee = new EmployeeBean();
				employee.setFirstName(rs.getString("employee.first_name"));
				employee.setLastName(rs.getString("employee.last_name"));
				
				SeatBean seat = new SeatBean();
				seat.setBuilding(rs.getString("seat.bldg_id"));
				seat.setFloor(rs.getString("seat.floor_number"));
				seat.setQuadrant(rs.getString("seat.quadrant"));
				seat.setRow(rs.getString("seat.row_number"));
				seat.setColumn(rs.getString("seat.column_number"));
				seat.setLocalNumber(rs.getString("seat.local_number"));
				employee.setSeat(seat);
				
				seatList.add(employee);
			}		
		} catch (SQLException e){
			logger.error("SQL Query error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
		} finally {
			closeConnection();
		}
		logger.info("searchSeatMapByFloorLocation(String location, String floor) - End");
		
		return seatList;
	}
}