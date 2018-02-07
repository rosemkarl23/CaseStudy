package com.Pointwest.Com.Java.Main;

import java.util.ArrayList;
import java.util.List;

import com.Pointwest.Com.Java.UI.HomePageUserInterface;
import com.Pointwest.Com.Java.UI.LoginUserInterface;
import com.Pointwest.Com.Java.UI.LogoutUserInterface;
import com.Pointwest.Com.Java.UI.SearchPageUserInterface;
import com.Pointwest.Com.Java.UI.SeatMapPageUserInterface;
import com.Pointwest.Com.Java.beans.EmployeeBean;

public class Main {

	public static void main(String[] args) {
		
		List<EmployeeBean> employeeList = new ArrayList<>();
		
		LogoutMenuLoop: while(true) {
			LoginUserInterface loginUI = new LoginUserInterface();
			EmployeeBean user = loginUI.loginPage();
		
				MainMenuLoop: while (true) {
				HomePageUserInterface homePageUI = new HomePageUserInterface();
				int homeChoice = homePageUI.homePage(user);
				
				if (homeChoice == 1) {
					SearchLoop: while(true) {
						SearchPageUserInterface searchPageUI = new SearchPageUserInterface();
						int searchChoice = searchPageUI.searchPage();
						
						employeeList = searchPageUI.searchByPage(searchChoice);
						searchPageUI.printResult(employeeList);
						
						int searchAgain = searchPageUI.searchAgain();
						
						if (searchAgain == 1) {
							continue SearchLoop;
						} else if (searchAgain == 2){
							continue MainMenuLoop;
						}
					}
				} else if (homeChoice == 2) {
					MapLoop : while(true) {
						SeatMapPageUserInterface seatMapPageUI = new SeatMapPageUserInterface();
						int searchChoice = seatMapPageUI.searchPage();
						
						employeeList = seatMapPageUI.requestChoice(searchChoice);
						seatMapPageUI.printResult(employeeList);
						
						int searchAgain = seatMapPageUI.searchAgain();
						
						if (searchAgain == 1) {
							continue MapLoop;
						} else if (searchAgain == 2){
							continue MainMenuLoop;
						}
					}
				} else if (homeChoice == 3) {
					LogoutUserInterface logoutUI = new LogoutUserInterface();
					
					int logoutChoice = logoutUI.logoutMenu();
						if (logoutChoice == 1 ) {
							System.out.println("Program Ended.");
							continue LogoutMenuLoop;
						}
						else if (logoutChoice == 2) {
							continue MainMenuLoop;
						}
				}
			}
		}
	}
}
