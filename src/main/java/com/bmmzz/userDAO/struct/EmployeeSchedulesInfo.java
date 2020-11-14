package com.bmmzz.userDAO.struct;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSchedulesInfo {
	//Select * From mydb.employee E, mydb.schedule S
	//S.HotelID='0' and
	//Where E.EmployeeID=S.EmployeeID and S.Position<>'Manager';
	private List<Integer> empIDs = new ArrayList<>();//1
	private List<String> empFullNames = new ArrayList<>();//2
	private List<String> empEmailAddresses = new ArrayList<>();//11
	private List<String> empMobPhoneNumbers = new ArrayList<>();//13
	
	private List<Integer> hotelIDs = new ArrayList<>();//17
	private List<String> empPositions = new ArrayList<>();//18
	private List<String> empPayRates = new ArrayList<>();//20
	private List<String> empStartDates = new ArrayList<>();//21
	private List<String> empStartTimes = new ArrayList<>();//23
	private List<String> empEndTimes = new ArrayList<>();//24
	
	//Query to get numWorkingDays/Week
	//SELECT count(*) 
	//FROM mydb.day_of_the_week D
	//Where D.EmployeeID='0' and D.HotelID='0';
	private List<String> empDailySalaries = new ArrayList<>();//no column
	private List<String> empWeeklySalaries = new ArrayList<>();//no column
	
	public void addES(int ID, String FN, String EA, String MPN, int hID, String pos, String PR, String SD, String ST, String ET, String DS, String WS) {
		empIDs.add(ID);
		empFullNames.add(FN);
		empEmailAddresses.add(EA);
		empMobPhoneNumbers.add(MPN);
		hotelIDs.add(hID);
		empPositions.add(pos);
		empPayRates.add(PR);
		empStartDates.add(SD);
		empStartTimes.add(ST);
		empEndTimes.add(ET);
		empDailySalaries.add(DS);
		empWeeklySalaries.add(WS);
	}
}