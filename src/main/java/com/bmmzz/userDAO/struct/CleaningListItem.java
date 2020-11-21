package com.bmmzz.userDAO.struct;

import java.time.LocalDate;

public class CleaningListItem {
	private String time; 
	private String roomNumber; 
	private int floor; 
	private String roomType; 
	private int hotelID;
	private LocalDate date;
	
	public CleaningListItem(String time, String roomNumber, int floor, String roomType, int hotelID) {
		this.time = time;
		this.roomNumber = roomNumber;
		this.floor = floor;
		this.roomType = roomType;
		this.hotelID =  hotelID;
		this.date = java.time.LocalDate.now();
	}
	
	public Boolean outdated() {
		if(date.compareTo(java.time.LocalDate.now()) == 0)
			return false;
		else
			return true;
	}
	
	public String getTime() {
		return time;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public int getFloor() {
		return floor;
	}

	public String getRoomType() {
		return roomType;
	}

	public int getHotelID() {
		return hotelID;
	}
}
