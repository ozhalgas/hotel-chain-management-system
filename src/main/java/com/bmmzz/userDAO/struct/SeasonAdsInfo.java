package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class SeasonAdsInfo {

	ArrayList<Integer> hotelID = new ArrayList<>();
	ArrayList<String> hotelName = new ArrayList<>();
	ArrayList<String> text = new ArrayList<>();
	
	public void add(int hotelID, String hotelName, String text) {
		this.hotelID.add(hotelID);
		this.hotelName.add(hotelName);
		this.text.add(text);
	}
}
