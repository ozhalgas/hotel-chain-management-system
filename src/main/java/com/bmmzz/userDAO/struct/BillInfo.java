package com.bmmzz.userDAO.struct;

public class BillInfo {
	private String checkInDate;
	private String checkOutDate;
	private double finalBill;
	private String roomNumber;
	private int floor;
	private int guestID;
	private String guestFullName;
	private String guestIdType;
	private String guestIdNumber;
	private int hotelID;
	private String hotelName;
	private String hotelCountry;
	private String hotelRegion;
	private String hotelAddress;
	
	
	public void add(String checkInDate, String checkOutDate, double finalBill, String roomNumber, int floor,
					int guestID, String guestFullName, String guestIdType, String guestIdNumber,
					int hotelID, String hotelName, String hotelCountry, String hotelRegion, String hotelAddress) {
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.finalBill = finalBill;
		this.floor = floor;
		this.guestID = guestID;
		this.roomNumber = roomNumber;
		this.guestID = guestID;
		this.guestFullName = guestFullName;
		this.guestIdType = guestIdType;
		this.guestIdNumber = guestIdNumber;
		this.hotelID = hotelID;
		this.hotelName = hotelName;
		this.hotelRegion = hotelRegion;
		this.hotelAddress = hotelAddress;
	}
}
