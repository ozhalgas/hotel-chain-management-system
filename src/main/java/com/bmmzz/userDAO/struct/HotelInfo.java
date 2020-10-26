package com.bmmzz.userDAO.struct;

public class HotelInfo {
	private int hotelID;
	private String name;
	private String country;
	private String region;
	private String address;
	
	public void set(int hotelID, String name, String country, String region, String address) {
		this.hotelID = hotelID;
		this.name = name;
		this.country = country;
		this.region = region;
		this.address = address;
	}
}
