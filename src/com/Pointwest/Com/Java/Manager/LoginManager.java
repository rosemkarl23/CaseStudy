package com.Pointwest.Com.Java.Manager;

import com.Pointwest.Com.Java.beans.EmployeeBean;
import com.Pointwest.Com.Java.dao.LoginDao;
import com.Pointwest.Com.Java.exceptions.Exceptions;
import com.Pointwest.Com.Java.UI.LoginUserInterface;

import org.apache.log4j.Logger;

import com.Pointwest.Com.Java.Manager.LoginManager;

public class LoginManager {
	Logger logger = Logger.getLogger(LoginManager.class);
	
	LoginDao loginDao = new LoginDao();
	LoginUserInterface loginUserInterface = new LoginUserInterface();
	
	//METHOD TO PASS USER INPUT AND RECEIVE USER DETAILS
	public EmployeeBean validateUser (String username, String password) throws Exceptions {
		logger.info("validateUser (String username, String password) - Start");
		EmployeeBean user = loginDao.validateUser(username, password);
		
		if (user == null) {
			logger.info("validateUser (String username, String password) - End");
			return null;
		} else {
			logger.info("validateUser (String username, String password) - End");
			return user;
		}
	}
}
