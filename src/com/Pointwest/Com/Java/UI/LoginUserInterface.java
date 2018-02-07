package com.Pointwest.Com.Java.UI;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.Pointwest.Com.Java.Manager.LoginManager;
import com.Pointwest.Com.Java.beans.EmployeeBean;
import com.Pointwest.Com.Java.exceptions.Exceptions;

public class LoginUserInterface {
	Logger logger = Logger.getLogger(LoginUserInterface.class);
	
	//METHOD TO DISPLAY LOGIN GUI
	public EmployeeBean loginPage() {
		LoginManager loginManager = new LoginManager();
		EmployeeBean user = new EmployeeBean();
		
		String username = null;
		String password = null;
		
		System.out.println("------------------------------------------");
		System.out.println("LOGIN");
		System.out.println("------------------------------------------");
		
		while (true) {
			System.out.print("Username: ");
			Scanner usernameScanner = new Scanner(System.in);
			username = usernameScanner.nextLine();
			System.out.print("Password: ");
			Scanner passwordScanner = new Scanner(System.in);
			password = passwordScanner.nextLine();
					
			try {
				user = loginManager.validateUser(username, password);
			} catch (Exceptions plse){
				System.out.println(plse.getCustomMsg());
			} catch (Exception e) {
				logger.error("Error occurred: " + e);
				System.out.println("Error happened. Try again");
			}
			if (user.getUserRole() == null) {
				System.out.println();
				System.out.println("Invalid username and/or password. Please try again.");
				System.out.println();
				continue;
			} else {
				System.out.println(user.getUserRole());
				break;
			}
		}
		
		return user;
	}
}
