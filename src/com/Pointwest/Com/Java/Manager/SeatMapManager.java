package com.Pointwest.Com.Java.Manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.Pointwest.Com.Java.beans.EmployeeBean;
import com.Pointwest.Com.Java.dao.ViewMapDao;
import com.Pointwest.Com.Java.exceptions.Exceptions;

public class SeatMapManager {
	ViewMapDao viewMapDao = new ViewMapDao();
	
	Logger logger = Logger.getLogger(LoginManager.class);
	
	public String returnCurrentLocations() throws Exceptions {
		logger.info("returnCurrentLocations() - Start");
		
		String currentLocations = viewMapDao.returnCurrentLocations();
		
		if (currentLocations.isEmpty() == false) {
			logger.info("returnCurrentLocations() - End");
			return currentLocations;
		} else {
			logger.info("returnCurrentLocations() - End");
			return null;
		}
	}
	
	public String returnCurrentFloors (String location) throws Exceptions {
		logger.info("checkValidFloors(String location) - Start");
		
		String validFloors = viewMapDao.returnCurrentFloors(location);
		
		if (validFloors.isEmpty() == false) {
			logger.info("checkValidFloors(String location) - End");
			return validFloors;
		} else {
			logger.info("checkValidFloors(String location) - End");
			return null;
		}
	}
	
	//This is to send the location to the dao for validation
	public boolean validateLocation(String location) throws Exceptions {
		logger.info("checkLocation(String location) - Start");
		
		boolean searchLocation = viewMapDao.validateLocation(location);
		
		if (searchLocation == true) {
			logger.info("checkLocation(String location) - End");
			return true;
		} else {
			logger.info("checkLocation(String location) - End");
			return false;
		}
	}
	
	//This is to send the location to the dao for validation
	public String validateFloor(String location, String floor) throws Exceptions {
		logger.info("validateFloor(String location) - Start");
		
		String searchFloor = viewMapDao.validateFloor(location, floor);
		
		if (searchFloor == null) {
			logger.info("validateFloor(String location, String floor) - End");
			return null;
		} else {
			logger.info("validateFloor(String location, String floor) - End");
			return searchFloor;
		}
	}
	
	//This is to send the quadrant to the dao for validation
	public boolean validateQuadrant(String location, String floor, String quadrant) throws Exceptions {
		logger.info("validateQuadrant(String location, String floor, String quadrant) - Start");
		
		boolean searchLocation = viewMapDao.validateQuadrant(location, floor, quadrant);
		
		if (searchLocation == true) {
			logger.info("validateQuadrant(String location, String floor, String quadrant) - End");
			return true;
		} else {
			logger.info("validateQuadrant(String location, String floor, String quadrant) - End");
			return false;
		}
	}
	
	//This is to send the location and floor to the dao for searching
	public List<EmployeeBean> searchSeatMap(String location, String floor, String quadrant, int userChoice) throws Exceptions {
		logger.info("searchSeatMap(String location, String floor) - Start");
		
		List<EmployeeBean> seatList = viewMapDao.searchSeatMap(location, floor, quadrant, userChoice);
		
		logger.info("searchSeatMap(String location, String floor) - End");
		return seatList;
	}
}
