package com.bmmzz.userDAO.struct;

public class CleaningListItem {
	private String time; 
	private String roomNumber; 
	private int floor; 
	private String roomType; 
	private int hotelID;
	
	public CleaningListItem(String time, String roomNumber, int floor, String roomType, int hotelID) {
		this.time = time;
		this.roomNumber = roomNumber;
		this.floor = floor;
		this.roomType = roomType;
		this.hotelID =  hotelID;
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
