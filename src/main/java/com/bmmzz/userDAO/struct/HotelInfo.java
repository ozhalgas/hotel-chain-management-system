package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class HotelInfo {
	private int hotelID;
	private String name;
	private String country;
	private String region;
	private String address;
	private ArrayList<String> phoneNumbers = new ArrayList<>();
	
	public void set(int hotelID, String name, String country, String region, String address) {
		this.hotelID = hotelID;
		this.name = name;
		this.country = country;
		this.region = region;
		this.address = address;
	}
	
	public void addPhone(String phone) {
		this.phoneNumbers.add(phone);
	}
}
