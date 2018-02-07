package com.Pointwest.Com.Java.UI;

import java.util.Scanner;

import com.Pointwest.Com.Java.Manager.SearchManager;
import com.Pointwest.Com.Java.beans.EmployeeBean;

public class HomePageUserInterface {
	public int homePage(EmployeeBean user) {
		int homeChoice = 0;
		String userHomeChoice = null;
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String userRole = user.getUserRole();

		System.out.println();
		System.out.println("## HOME ##");
		System.out.println("------------------------------------------");
		System.out.println("Welcome " + firstName + " " + lastName + " [" + userRole + "]!");
		System.out.println("------------------------------------------");
		System.out.println("MENU:");
		System.out.println("[1] Search");
		System.out.println("[2] View Seatplan");
		System.out.println("[3] Logout");

		do {
			Scanner homeMenuScanner = new Scanner(System.in);
			userHomeChoice = homeMenuScanner.nextLine();

			if ((userHomeChoice.equals("1")) || (userHomeChoice.equals("2")) || (userHomeChoice.equals("3"))) {
				homeChoice = Integer.parseInt(userHomeChoice);
				break;
			} else {
				System.out.println("Invalid choice. Please try again.");
				continue;
			}
		} while (true);

		return homeChoice;
	}
}
