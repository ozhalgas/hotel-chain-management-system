package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

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
	private double categoryDiscount;
	private String categoryName;
	private int hotelID;
	private String hotelName;
	private String hotelCountry;
	private String hotelRegion;
	private String hotelAddress;
	private ArrayList<String> usedFeatures = new ArrayList<>();
	private ArrayList<Integer> usedFeaturesTimes = new ArrayList<>();
	private ArrayList<Double> usedFeaturesCost = new ArrayList<>();
	
	public void add(String checkInDate, String checkOutDate, double finalBill, String roomNumber, int floor,
					int guestID, String guestFullName, String guestIdType, String guestIdNumber, double categoryDiscount, String categoryName,
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
		this.categoryDiscount = categoryDiscount;
		this.categoryName = categoryName;
		this.hotelID = hotelID;
		this.hotelName = hotelName;
		this.hotelRegion = hotelRegion;
		this.hotelAddress = hotelAddress;
	}
	
	public void addFeatures(String featureName, int timesUsed, double cost) {
		this.usedFeatures.add(featureName);
		this.usedFeaturesTimes.add(timesUsed);
		this.usedFeaturesCost.add(cost);
	}
}
