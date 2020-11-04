package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class GuestBookings {
	
	private ArrayList<String> roomTypeNames = new ArrayList<String>();
	private ArrayList<Integer> hotelIDs = new ArrayList<Integer>();
	private ArrayList<String> hotelNames = new ArrayList<String> ();
	private ArrayList<String> hotelCountries = new ArrayList<String> ();
	private ArrayList<String> hotelRegions = new ArrayList<String> ();
	private ArrayList<String> checkInDates = new ArrayList<String>();
	private ArrayList<String> checkOutDates = new ArrayList<String>();
	private ArrayList<Integer> numberOfRooms = new ArrayList<Integer>();
    
    public void addBooking(String roomTypeName, int hotelID, String hotelName, String hotelCountry, String hotelRegion, String checkInDate, String checkOutDate, int numberOfRoom) {
		roomTypeNames.add(roomTypeName);
		hotelIDs.add(hotelID);
		hotelNames.add(hotelName);
		hotelCountries.add(hotelCountry);
		hotelRegions.add(hotelRegion);
		checkInDates.add(checkInDate);
		checkOutDates.add(checkOutDate);
		numberOfRooms.add(numberOfRoom);
	}
    
    public ArrayList<String> getRoomTypeNames() {
		return roomTypeNames;
	}
	
	public ArrayList<Integer> getHotelIDs() {
		return hotelIDs;
	}
	
	public ArrayList<String> getHotelNames() {
		return hotelNames;
	}
	
	public ArrayList<String> getHotelCountries() {
		return hotelCountries;
	}
	
	public ArrayList<String> getHotelRegions() {
		return hotelRegions;
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
}