package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class SeasonInfo {

	private ArrayList<String> seasonName = new ArrayList<>();
	private ArrayList<String> start = new ArrayList<>();
	private ArrayList<String> end = new ArrayList<>();
	private ArrayList<Integer> hotelID = new ArrayList<>();
	private ArrayList<String> hotelName = new ArrayList<>();
	
	public void add(String seasonName, String start, String end, int hotelID, String hotelName) {
		this.seasonName.add(seasonName);
		this.start.add(start);
		this.end.add(end);
		this.hotelID.add(hotelID);
		this.hotelName.add(hotelName);
	}
}
