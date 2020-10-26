package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class HotelChoosingInfo {
	private ArrayList<Integer> hotelID = new ArrayList<Integer>();
	private ArrayList<String> name = new ArrayList<String>();
	private ArrayList<String> country = new ArrayList<String>();
	private ArrayList<String> region = new ArrayList<String>();
	
	public void addHotel(Integer id, String name, String country, String region) {
		hotelID.add(id);
		this.name.add(name);
		this.country.add(country);
		this.region.add(region);
	}
}
