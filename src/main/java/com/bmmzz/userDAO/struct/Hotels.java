package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class Hotels {
	private ArrayList<Integer> hotelIDs = new ArrayList<Integer>();
	private ArrayList<String> hotels = new ArrayList<String>();
	
	public void addHotel(int id, String name) {
		hotelIDs.add(id);
		hotels.add(name);
	}
	
	public ArrayList<Integer> getHotelIDs() {
		return hotelIDs;
	}
	
	public ArrayList<String> getHotels() {
		return hotels;
	}
}
