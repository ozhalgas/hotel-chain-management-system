package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class HotelBookings {
	private ArrayList<Integer> guestIDs = new ArrayList<Integer>();
	private ArrayList<Integer> hotelIDs = new ArrayList<Integer>();
	private ArrayList<String> hotelNames = new ArrayList<>();
	private ArrayList<String> guestFullNames = new ArrayList<>();
	private ArrayList<String> guestMobilePhoneNumbers = new ArrayList<>();
	private ArrayList<String> roomTypeNames = new ArrayList<String>();
	private ArrayList<String> checkInDates = new ArrayList<String>();
	private ArrayList<String> checkOutDates = new ArrayList<String>();
	private ArrayList<Integer> numberOfRooms = new ArrayList<Integer>();
    
    public void addBooking(int guestID, int hotelID, String hotelName, String guestFullName, String guestMobilePhoneNumber, String roomTypeName, String checkInDate, String checkOutDate, int numberOfRoom) {
		guestIDs.add(guestID);
    	hotelIDs.add(hotelID);
		hotelNames.add(hotelName);
    	roomTypeNames.add(roomTypeName);
		guestFullNames.add(guestFullName);
		guestMobilePhoneNumbers.add(guestMobilePhoneNumber);
		checkInDates.add(checkInDate);
		checkOutDates.add(checkOutDate);
		numberOfRooms.add(numberOfRoom);
	}
    
    public ArrayList<String> getRoomTypeNames() {
		return roomTypeNames;
	}
    
    public ArrayList<String> getGuestFullNames() {
		return guestFullNames;
	}
    
    public ArrayList<String> getGuestMobilePhoneNumbers() {
		return guestMobilePhoneNumbers;
	}
	
	public ArrayList<String> getCheckInDates() {
		return checkInDates;
	}
	
	public ArrayList<String> getCheckOutDates() {
		return checkOutDates;
	}
	
	public ArrayList<Integer> getNumberOfRooms() {
		return numberOfRooms;
	}
	
	public ArrayList<Integer> getHotelIDs() {
		return hotelIDs;
	}
	
	public ArrayList<Integer> getGuestIDs() {
		return guestIDs;
	}
	
	public ArrayList<String> getHotelNames() {
		return hotelNames;
	}
}