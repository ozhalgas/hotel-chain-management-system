package com.bmmzz.userDAO;

import java.util.ArrayList;

public class Hotels {
	ArrayList<String> hotelIDs = new ArrayList<String>();
	ArrayList<String> hotels = new ArrayList<String>();
	
	public void addHotel(String id, String name) {
		hotelIDs.add(id);
		hotels.add(name);
	}
}
