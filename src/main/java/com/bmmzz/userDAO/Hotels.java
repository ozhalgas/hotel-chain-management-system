package com.bmmzz.userDAO;

import java.util.ArrayList;

public class Hotels {
	ArrayList<Integer> hotelIDs = new ArrayList<Integer>();
	ArrayList<String> hotels = new ArrayList<String>();
	
	public void addHotel(int id, String name) {
		hotelIDs.add(id);
		hotels.add(name);
	}
}
