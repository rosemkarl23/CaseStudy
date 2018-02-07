package com.Pointwest.Com.Java.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.Pointwest.Com.Java.Manager.SearchManager;
import com.Pointwest.Com.Java.beans.EmployeeBean;
import com.Pointwest.Com.Java.exceptions.Exceptions;

public class SearchPageUserInterface {
	
	Logger logger = Logger.getLogger(LoginUserInterface.class);
	
	List<EmployeeBean> employeeList = new ArrayList<>();
	SearchManager searchManager = new SearchManager();
	
	public int searchPage() {
		int searchChoice = 0;
		String userSearchChoice = null;
		
		System.out.println("## SEARCH ##");
		System.out.println("MENU:");
		System.out.println("[1] By Employee Id");
		System.out.println("[2] By Name");
		System.out.println("[3] By Project");
		
		while (true) {
			Scanner searchMenuScanner = new Scanner(System.in);
			userSearchChoice = searchMenuScanner.nextLine();
			
			if ((userSearchChoice.equals("1")) || (userSearchChoice.equals("2")) || (userSearchChoice.equals("3"))) {
				searchChoice = Integer.parseInt(userSearchChoice);
				break;
			}
			else {
				System.out.println("Invalid choice. Please try again.");
				continue;
			}
		}
		return searchChoice;
	}
	
	public List<EmployeeBean> searchByPage(int searchChoice) {
		String keyword;
		
		if (searchChoice == 1) {
			System.out.println("## SEARCH – By Employee Id ##");
			System.out.println("Enter Employee ID: ");
		}
		else if (searchChoice == 2) {
			System.out.println("## SEARCH – By Name ##");
			System.out.println("Enter Name: ");
		}
		else if (searchChoice == 3) {
			System.out.println("## SEARCH – By Project ##");
			System.out.println("Enter Project: ");
		}
		Scanner searchKeywordScanner = new Scanner(System.in);
		keyword = searchKeywordScanner.nextLine();
		
		try {
			employeeList = searchManager.searchChoice(keyword, searchChoice);
		} catch (Exceptions plse){
			System.out.println(plse.getCustomMsg());
		} catch (Exception e) {
			logger.error("Error occurred: " + e);
			System.out.println("Error happened. Please try again later...");
		}
		return employeeList;
	}

	public void printResult(List<EmployeeBean> employeeList) {
		String seatID = null;
		
		System.out.println();
		if (employeeList.size() == 0) {
			System.out.println("No Results Found.");
			System.out.println();
		} else {
			System.out.println("## SEARCH RESULT – (" + employeeList.size() + ") ##");
			System.out.println("------------------------------------------------------------------------------------------------");
			System.out.println("EMPLOYEE ID|FIRSTNAME|LASTNAME|SEAT|LOCAL|SHIFT|PROJECT(S)");
			System.out.println("------------------------------------------------------------------------------------------------");
			for (int i = 0, j = 1; i < employeeList.size(); i++, j++) {
				System.out.print("[" + j + "]");
				System.out.print(employeeList.get(i).getEmployeeID().toUpperCase() + "|");
				System.out.print(employeeList.get(i).getFirstName().toUpperCase() + "|");
				System.out.print(employeeList.get(i).getLastName().toUpperCase() + "|");
	
				seatID = (employeeList.get(i).getSeat().getBuilding() + employeeList.get(i).getSeat().getFloor() + "F" +
						employeeList.get(i).getSeat().getQuadrant() + employeeList.get(i).getSeat().getColumn() + "-" +
						employeeList.get(i).getSeat().getRow());
				
				System.out.print(seatID.toUpperCase() + "|");
				
				if (employeeList.get(i).getSeat().getLocalNumber().isEmpty()) {
					System.out.print("NONE|");
				} else {
					System.out.print(employeeList.get(i).getSeat().getLocalNumber().toUpperCase() + "|");
				}
				
				System.out.print(employeeList.get(i).getShift().toUpperCase() + "|");
				
				if (employeeList.get(i).getProject().equalsIgnoreCase("bogusproject")) {
					System.out.print("NONE");
				} else {
					if (employeeList.get(i).getProject().contains(",BogusProject")) {
						System.out.print(employeeList.get(i).getProject().replace(",BogusProject", "").toUpperCase());
					} else if (employeeList.get(i).getProject().contains("BogusProject,")) {
						System.out.print(employeeList.get(i).getProject().replace("BogusProject,", "").toUpperCase());
					} else {
						System.out.print(employeeList.get(i).getProject().toUpperCase());
					}
				}
				System.out.println("|");
			}
			System.out.println("---------------------------------------- end of result --------------------------------------");
		}
	}
	
	public int searchAgain() {
		while (true) {
			String actionChoice = null;
			
			System.out.println("ACTIONS: [1] Search Again [2] Home");
			Scanner actionScanner = new Scanner(System.in);
			actionChoice = actionScanner.nextLine();
			
			if ((!actionChoice.equalsIgnoreCase("1")) && (!actionChoice.equalsIgnoreCase("2"))) {
				System.out.println("Invalid Input");
				continue;
			} else {
				int choice = Integer.parseInt(actionChoice);
				if (choice == 1) {
					return 1;
				} else {
					return 2;
				}
			}
		}
	}
}
