package com.bmmzz.userDAO.struct;

import java.util.ArrayList;
import java.util.List;

public class EmployeesForAdmin{
	private List<Integer> hotelIDs = new ArrayList<>();
	private List<String> hotelNames = new ArrayList<>();
	private List<Integer> empIDs = new ArrayList<>();
	private List<String> empFullNames = new ArrayList<>();
	private List<String> empPositions = new ArrayList<>();
	private List<String> empEmailAddresses = new ArrayList<>();
	private List<String> empMobPhoneNumbers = new ArrayList<>();
	private List<String> empLogins = new ArrayList<>();
	private List<String> empPasswords = new ArrayList<>();
	private List<String> startDates = new ArrayList<>();
	private List<String> endDates = new ArrayList<>();
	public void addEmpFA(int hID, String hName, int eID, String FN, String pos, String EA, String MPN, String login, String password, String startD, String endD) {
		hotelIDs.add(hID);
		hotelNames.add(hName);
		empIDs.add(eID);
		empFullNames.add(FN);
		empPositions.add(pos);
		empEmailAddresses.add(EA);
		empMobPhoneNumbers.add(MPN);
		empLogins.add(login);
		empPasswords.add(password);
		startDates.add(startD);
		endDates.add(endD);
	}
}