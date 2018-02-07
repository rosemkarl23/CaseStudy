package com.Pointwest.Com.Java.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.Pointwest.Com.Java.beans.EmployeeBean;
import com.Pointwest.Com.Java.exceptions.Exceptions;
import com.Pointwest.Com.Java.Manager.SeatMapManager;

public class SeatMapPageUserInterface {
	
	Logger logger = Logger.getLogger(LoginUserInterface.class);

	String choice = null;
	String checkFloor = null;
	String locationKeyword = null;
	String floorKeyword = null;
	String quadrantKeyword = null;
	String seatID = null;
	String currentLocations = null;
	String currentFloors = null;
	boolean checkLocation = true;
	boolean checkQuadrant = true;
	List<EmployeeBean> seatList = new ArrayList<>();

	public int searchPage () {
		String choice;

		System.out.println();
		System.out.println("## VIEW SEATPLAN ##");
		System.out.println("MENU:");
		System.out.println("[1] By Location - Floor Level");
		System.out.println("[2] By Quadrant");
		System.out.println();
		
		while (true) {
			System.out.print("Input choice: ");
			Scanner choiceScanner = new Scanner(System.in);
			choice = choiceScanner.nextLine();

			if (choice.equals("1") || choice.equals("2")) {
				return Integer.parseInt(choice);
			} else {
				System.out.println("Invalid input.");
				continue;
			}
		}
	}

	public List<EmployeeBean> requestChoice (int userChoice) {
		while (true) {
			SeatMapManager seatMapManager = new SeatMapManager();

			try {
				currentLocations = seatMapManager.returnCurrentLocations();
			} catch (Exceptions plse) {
				System.out.println(plse.getCustomMsg());
			} catch (Exception e) {
				logger.error("Error occurred: " + e);
				System.out.println("Error happened. Please try again later...");
			}
			
			System.out.print("Enter Location [" + currentLocations + "]: ");
			Scanner locationScanner = new Scanner(System.in);
			locationKeyword = locationScanner.nextLine();

			try {
				checkLocation = seatMapManager.validateLocation(locationKeyword);
			} catch (Exceptions plse) {
				System.out.println(plse.getCustomMsg());
			} catch (Exception e) {
				logger.error("Error occurred: " + e);
				System.out.println("Error happened. Please try again later...");
			}

			if (checkLocation == true) {
				System.out.println("Location not found. Please try again.");
				continue;
			} else {
				while (true) {
					try {
						currentFloors = seatMapManager.returnCurrentFloors(locationKeyword);
					} catch (Exceptions plse) {
						System.out.println(plse.getCustomMsg());
					} catch (Exception e) {
						logger.error("Error occurred: " + e);
						System.out.println("Error happened. Please try again later...");
					}
					
					System.out.print("Floor [" + currentFloors + "]: ");
					Scanner floorScanner = new Scanner(System.in);
					floorKeyword = floorScanner.nextLine();

					try {
						checkFloor = seatMapManager.validateFloor(locationKeyword, floorKeyword);
					} catch (Exceptions plse) {
						System.out.println(plse.getCustomMsg());
					} catch (Exception e) {
						logger.error("Error occurred: " + e);
						System.out.println("Error happened. Please try again later...");
					}

					if (checkFloor == null) {
						System.out.println("No Such Floor.");
						continue;
					} else {
						if (userChoice == 1) {
							try {
								seatList = seatMapManager.searchSeatMap(locationKeyword, floorKeyword, quadrantKeyword, userChoice);
								break;
							} catch (Exceptions plse) {
								System.out.println(plse.getCustomMsg());
							} catch (Exception e) {
								logger.error("Error occurred: " + e);
								System.out.println("Error happened. Please try again later...");
							}
						} else {
							while(true) {
								System.out.print("Quadrant [A,B,C,D]: ");
								Scanner quadrantScanner = new Scanner(System.in);
								quadrantKeyword = quadrantScanner.nextLine();
								
								try {
									checkQuadrant = seatMapManager.validateQuadrant(locationKeyword, floorKeyword, quadrantKeyword);
								} catch (Exceptions plse){
									System.out.println(plse.getCustomMsg());
								} catch (Exception e) {
									logger.error("Error occurred: " + e);
									System.out.println("Error happened. Please try again later...");
								}
								
								if (checkQuadrant == true) {
									System.out.println("No Such Quadrant.");
									continue;
								} else {
									try {
										seatList = seatMapManager.searchSeatMap(locationKeyword, floorKeyword, quadrantKeyword, userChoice);
										break;
									} catch (Exceptions plse){
										System.out.println(plse.getCustomMsg());
									} catch (Exception e) {
										logger.error("Error occurred: " + e);
										System.out.println("Error happened. Please try again later...");
									}
								}
							}
							break;
						}	
					}
				}
				break;
			}
		}
		return seatList;
	}

	public void printResult (List<EmployeeBean> seatList) {
		System.out.println();
		if (seatList.size() == 0) {
			System.out.println("No Results Found.");
			System.out.println();
		} else if (quadrantKeyword == null) {
			System.out.println("## VIEW SEATPLAN ##");
			System.out.println(
					"LOCATION: " + locationKeyword.toUpperCase() + ", FLOOR: " + floorKeyword);
			System.out.println(
					"--------------------------------------------------------------------------------------------------------");
			for (int i = 14; i > 11; i--) {
				seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
						+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
						+ seatList.get(i).getSeat().getRow());

				System.out.print(seatID.toUpperCase() + "\t");
			}
			for (int i = 15; i < 18; i++) {
				seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
						+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
						+ seatList.get(i).getSeat().getRow());

				System.out.print(seatID.toUpperCase() + "\t");
			}
			System.out.println();
			for (int i = 14; i > 11; i--) {
				if (seatList.get(i).getLastName() == null) {
					System.out.print("Vacant");
				} else {
					System.out.print(seatList.get(i).getLastName().substring(0,
							Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
				}
				if (seatList.get(i).getFirstName() == null) {
					System.out.print("\t\t");
				} else {
					System.out.print(seatList.get(i).getFirstName().substring(0,
							Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
				}
			}
			for (int i = 15; i < 18; i++) {
				if (seatList.get(i).getLastName() == null) {
					System.out.print("Vacant");
				} else {
					System.out.print(seatList.get(i).getLastName().substring(0,
							Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
				}
				if (seatList.get(i).getFirstName() == null) {
					System.out.print("\t\t");
				} else {
					System.out.print(seatList.get(i).getFirstName().substring(0,
							Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
				}
			}
			System.out.println();
			for (int i = 14; i > 11; i--) {
				if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
					System.out.print("No loc. no.\t");
				} else {
					System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
				}
			}
			for (int i = 15; i < 18; i++) {
				if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
					System.out.print("No loc. no.\t");
				} else {
					System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
				}
			}
			System.out.println();
			System.out.println();
			for (int i = 2; i > -1; i--) {
				seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
						+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
						+ seatList.get(i).getSeat().getRow());

				System.out.print(seatID.toUpperCase() + "\t");
			}
			for (int i = 3; i < 6; i++) {
				seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
						+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
						+ seatList.get(i).getSeat().getRow());

				System.out.print(seatID.toUpperCase() + "\t");
			}
			System.out.println();
			for (int i = 2; i > -1; i--) {
				if (seatList.get(i).getLastName() == null) {
					System.out.print("Vacant");
				} else {
					System.out.print(seatList.get(i).getLastName().substring(0,
							Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
				}
				if (seatList.get(i).getFirstName() == null) {
					System.out.print("\t\t");
				} else {
					System.out.print(seatList.get(i).getFirstName().substring(0,
							Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
				}
			}
			for (int i = 3; i < 6; i++) {
				if (seatList.get(i).getLastName() == null) {
					System.out.print("Vacant");
				} else {
					System.out.print(seatList.get(i).getLastName().substring(0,
							Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
				}
				if (seatList.get(i).getFirstName() == null) {
					System.out.print("\t\t");
				} else {
					System.out.print(seatList.get(i).getFirstName().substring(0,
							Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
				}
			}
			System.out.println();
			for (int i = 2; i > -1; i--) {
				if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
					System.out.print("No loc. no.\t");
				} else {
					System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
				}
			}
			for (int i = 3; i < 6; i++) {
				if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
					System.out.print("No loc. no.\t");
				} else {
					System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
				}
			}
			System.out.println();
			System.out.println();
			for (int i = 8; i > 5; i--) {
				seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
						+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
						+ seatList.get(i).getSeat().getRow());

				System.out.print(seatID.toUpperCase() + "\t");
			}
			for (int i = 9; i < 12; i++) {
				seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
						+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
						+ seatList.get(i).getSeat().getRow());

				System.out.print(seatID.toUpperCase() + "\t");
			}
			System.out.println();
			for (int i = 8; i > 5; i--) {
				if (seatList.get(i).getLastName() == null) {
					System.out.print("Vacant");
				} else {
					System.out.print(seatList.get(i).getLastName().substring(0,
							Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
				}
				if (seatList.get(i).getFirstName() == null) {
					System.out.print("\t\t");
				} else {
					System.out.print(seatList.get(i).getFirstName().substring(0,
							Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
				}
			}
			for (int i = 9; i < 12; i++) {
				if (seatList.get(i).getLastName() == null) {
					System.out.print("Vacant");
				} else {
					System.out.print(seatList.get(i).getLastName().substring(0,
							Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
				}
				if (seatList.get(i).getFirstName() == null) {
					System.out.print("\t\t");
				} else {
					System.out.print(seatList.get(i).getFirstName().substring(0,
							Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
				}
			}
			System.out.println();
			for (int i = 6; i < 12; i++) {
				if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
					System.out.print("No loc. no.\t");
				} else {
					System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
				}
			}
			System.out.println();
			System.out.println();
			for (int i = 20; i > 17; i--) {
				seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
						+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
						+ seatList.get(i).getSeat().getRow());

				System.out.print(seatID.toUpperCase() + "\t");
			}
			for (int i = 21; i < 24; i++) {
				seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
						+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
						+ seatList.get(i).getSeat().getRow());

				System.out.print(seatID.toUpperCase() + "\t");
			}
			System.out.println();
			for (int i = 20; i > 17; i--) {
				if (seatList.get(i).getLastName() == null) {
					System.out.print("Vacant");
				} else {
					System.out.print(seatList.get(i).getLastName().substring(0,
							Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
				}
				if (seatList.get(i).getFirstName() == null) {
					System.out.print("\t\t");
				} else {
					System.out.print(seatList.get(i).getFirstName().substring(0,
							Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
				}
			}
			for (int i = 21; i < 24; i++) {
				if (seatList.get(i).getLastName() == null) {
					System.out.print("Vacant");
				} else {
					System.out.print(seatList.get(i).getLastName().substring(0,
							Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
				}
				if (seatList.get(i).getFirstName() == null) {
					System.out.print("\t\t");
				} else {
					System.out.print(seatList.get(i).getFirstName().substring(0,
							Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
				}
			}
			System.out.println();
			for (int i = 20; i > 17; i--) {
				if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
					System.out.print("No loc. no.\t");
				} else {
					System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
				}
			}
			for (int i = 21; i < 24; i++) {
				if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
					System.out.print("No loc. no.\t");
				} else {
					System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
				}
			}
			System.out.println();
			System.out.println(
					"--------------------------------------- end of seatplan ---------------------------------------------");
			System.out.println("**Note: Names are truncated for the seat map to be displayed properly");
			System.out.println();
		} else {
			System.out.println("## VIEW SEATPLAN ##");
			System.out.println("LOCATION: " + locationKeyword.toUpperCase() + " [" + checkFloor + "], FLOOR: " + floorKeyword + ", QUADRANT: " + quadrantKeyword.toUpperCase());
			System.out.println("--------------------------------------------------------------------------------------------------------");
			if (quadrantKeyword.equalsIgnoreCase("a")) {
				for (int i = 5; i > 2; i--) {
					seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
							+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
							+ seatList.get(i).getSeat().getRow());

					System.out.print(seatID.toUpperCase() + "\t");
				}
				System.out.println();
				for (int i = 5; i > 2; i--) {
					if (seatList.get(i).getLastName() == null) {
						System.out.print("Vacant");
					} else {
						System.out.print(seatList.get(i).getLastName().substring(0,
								Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
					}
					if (seatList.get(i).getFirstName() == null) {
						System.out.print("\t\t");
					} else {
						System.out.print(seatList.get(i).getFirstName().substring(0,
								Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
					}
				}
				System.out.println();
				for (int i = 5; i > 2; i--) {
					if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
						System.out.print("No loc. no.\t");
					} else {
						System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
					}
				}
				System.out.println();
				System.out.println();
				for (int i = 2; i > -1; i--) {
					seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
							+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
							+ seatList.get(i).getSeat().getRow());

					System.out.print(seatID.toUpperCase() + "\t");
				}
				System.out.println();
				for (int i = 2; i > -1; i--) {
					if (seatList.get(i).getLastName() == null) {
						System.out.print("Vacant");
					} else {
						System.out.print(seatList.get(i).getLastName().substring(0,
								Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
					}
					if (seatList.get(i).getFirstName() == null) {
						System.out.print("\t\t");
					} else {
						System.out.print(seatList.get(i).getFirstName().substring(0,
								Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
					}
				}
				System.out.println();
				for (int i = 2; i > -1; i--) {
					if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
						System.out.print("No loc. no.\t");
					} else {
						System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
					}
				}
			} else if (quadrantKeyword.equalsIgnoreCase("b")) {
				for (int i = 3; i < 6; i++) {
					seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
							+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
							+ seatList.get(i).getSeat().getRow());

					System.out.print(seatID.toUpperCase() + "\t");
				}
				System.out.println();
				for (int i = 3; i < 6; i++) {
					if (seatList.get(i).getLastName() == null) {
						System.out.print("Vacant");
					} else {
						System.out.print(seatList.get(i).getLastName().substring(0,
								Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
					}
					if (seatList.get(i).getFirstName() == null) {
						System.out.print("\t\t");
					} else {
						System.out.print(seatList.get(i).getFirstName().substring(0,
								Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
					}
				}
				System.out.println();
				for (int i = 3; i < 6; i++) {
					if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
						System.out.print("No loc. no.\t");
					} else {
						System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
					}
				}
				System.out.println();
				System.out.println();
				for (int i = 0; i < 3; i++) {
					seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
							+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
							+ seatList.get(i).getSeat().getRow());

					System.out.print(seatID.toUpperCase() + "\t");
				}
				System.out.println();
				for (int i = 0; i < 3; i++) {
					if (seatList.get(i).getLastName() == null) {
						System.out.print("Vacant");
					} else {
						System.out.print(seatList.get(i).getLastName().substring(0,
								Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
					}
					if (seatList.get(i).getFirstName() == null) {
						System.out.print("\t\t");
					} else {
						System.out.print(seatList.get(i).getFirstName().substring(0,
								Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
					}
				}
				System.out.println();
				for (int i = 0; i < 3; i++) {
					if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
						System.out.print("No loc. no.\t");
					} else {
						System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
					}
				}
			} else if (quadrantKeyword.equalsIgnoreCase("c")) {
				for (int i = 2; i > -1; i--) {
					seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
							+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
							+ seatList.get(i).getSeat().getRow());

					System.out.print(seatID.toUpperCase() + "\t");
				}
				System.out.println();
				for (int i = 2; i > -1; i--) {
					if (seatList.get(i).getLastName() == null) {
						System.out.print("Vacant");
					} else {
						System.out.print(seatList.get(i).getLastName().substring(0,
								Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
					}
					if (seatList.get(i).getFirstName() == null) {
						System.out.print("\t\t");
					} else {
						System.out.print(seatList.get(i).getFirstName().substring(0,
								Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
					}
				}
				System.out.println();
				for (int i = 2; i > -1; i--) {
					if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
						System.out.print("No loc. no.\t");
					} else {
						System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
					}
				}
				System.out.println();
				System.out.println();
				for (int i = 5; i > 2; i--) {
					seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
							+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
							+ seatList.get(i).getSeat().getRow());

					System.out.print(seatID.toUpperCase() + "\t");
				}
				System.out.println();
				for (int i = 5; i > 2; i--) {
					if (seatList.get(i).getLastName() == null) {
						System.out.print("Vacant");
					} else {
						System.out.print(seatList.get(i).getLastName().substring(0,
								Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
					}
					if (seatList.get(i).getFirstName() == null) {
						System.out.print("\t\t");
					} else {
						System.out.print(seatList.get(i).getFirstName().substring(0,
								Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
					}
				}
				System.out.println();
				for (int i = 5; i > 2; i--) {
					if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
						System.out.print("No loc. no.\t");
					} else {
						System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
					}
				}
			} else if (quadrantKeyword.equalsIgnoreCase("d")) {
				for (int i = 0; i < 3; i++) {
					seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
							+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
							+ seatList.get(i).getSeat().getRow());

					System.out.print(seatID.toUpperCase() + "\t");
				}
				System.out.println();
				for (int i = 0; i < 3; i++) {
					if (seatList.get(i).getLastName() == null) {
						System.out.print("Vacant");
					} else {
						System.out.print(seatList.get(i).getLastName().substring(0,
								Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
					}
					if (seatList.get(i).getFirstName() == null) {
						System.out.print("\t\t");
					} else {
						System.out.print(seatList.get(i).getFirstName().substring(0,
								Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
					}
				}
				System.out.println();
				for (int i = 0; i < 3; i++) {
					if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
						System.out.print("No loc. no.\t");
					} else {
						System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
					}
				}
				System.out.println();
				System.out.println();
				for (int i = 3; i < 6; i++) {
					seatID = (seatList.get(i).getSeat().getBuilding() + seatList.get(i).getSeat().getFloor() + "F"
							+ seatList.get(i).getSeat().getQuadrant() + seatList.get(i).getSeat().getColumn() + "-"
							+ seatList.get(i).getSeat().getRow());

					System.out.print(seatID.toUpperCase() + "\t");
				}
				System.out.println();
				for (int i = 3; i < 6; i++) {
					if (seatList.get(i).getLastName() == null) {
						System.out.print("Vacant");
					} else {
						System.out.print(seatList.get(i).getLastName().substring(0,
								Math.min(seatList.get(i).getLastName().length(), 5)) + ",");
					}
					if (seatList.get(i).getFirstName() == null) {
						System.out.print("\t\t");
					} else {
						System.out.print(seatList.get(i).getFirstName().substring(0,
								Math.min(seatList.get(i).getFirstName().length(), 1)) + "\t\t");
					}
				}
				System.out.println();
				for (int i = 3; i < 6; i++) {
					if (seatList.get(i).getSeat().getLocalNumber().isEmpty()) {
						System.out.print("No loc. no.\t");
					} else {
						System.out.print("loc." + seatList.get(i).getSeat().getLocalNumber() + "\t");
					}
				}
			}
			System.out.println();
			System.out.println("--------------------------------------- end of seatplan ---------------------------------------------");
			System.out.println("**Note: Names are truncated for the seat map to be displayed properly");
			System.out.println();
		}
	}

	public int searchAgain() {
		while (true) {
			String actionChoice = null;

			System.out.println("ACTIONS: [1] View seatplan again [2] Home");
			Scanner actionScanner = new Scanner(System.in);
			actionChoice = actionScanner.nextLine();

			if (!actionChoice.equalsIgnoreCase("1") && !actionChoice.equalsIgnoreCase("2")) {
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
