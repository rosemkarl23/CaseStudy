package com.Pointwest.Com.Java.UI;

import java.util.Scanner;
import com.Pointwest.Com.Java.Manager.LogoutManager;

public class LogoutUserInterface {
	public int logoutMenu() {
		int logoutChoice = 0;
		String userLogoutChoice = null;
		
		System.out.println("## LOGOUT ##");
		System.out.println("Are you sure you want to logout of the system?");
		System.out.println("[1] Yes");
		System.out.println("[2] No");
		
		while (true) {
			Scanner logoutScanner = new Scanner(System.in);
			userLogoutChoice = logoutScanner.nextLine();
			if ((userLogoutChoice.equals("1")) || (userLogoutChoice != "2")) {
				logoutChoice = Integer.parseInt(userLogoutChoice);
				break;
			}
			else {
				System.out.println("Invalid choice. Please try again.");
				continue;
			}
		}
		
		return logoutChoice;
	}
}
