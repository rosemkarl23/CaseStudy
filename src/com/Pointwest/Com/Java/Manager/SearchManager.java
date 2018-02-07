package com.Pointwest.Com.Java.Manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.Pointwest.Com.Java.beans.EmployeeBean;
import com.Pointwest.Com.Java.dao.SearchDao;

public class SearchManager {
	SearchDao searchDao = new SearchDao();
	
	Logger logger = Logger.getLogger(LoginManager.class);
	
	//METHOD TO PASS USER INPUT AND RECEIVE DETAILS
	public List<EmployeeBean> searchChoice(String keyword, int userChoice) throws Exception {
		logger.info("searchIDChoice(String username) - Start");
		
		List<EmployeeBean> employeeList = searchDao.searchEmployee(keyword, userChoice);
		
		logger.info("searchIDChoice(String username) - End");
		return employeeList;
	}
}
