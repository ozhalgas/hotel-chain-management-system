package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class SeasonInfo {

	private ArrayList<String> weekDay = new ArrayList<>();
	private ArrayList<String> seasonName = new ArrayList<>();
	private ArrayList<String> start = new ArrayList<>();
	private ArrayList<String> end = new ArrayList<>();
	private ArrayList<String> roomType = new ArrayList<>();
	private ArrayList<Double> price = new ArrayList<>();
	private ArrayList<Integer> hotelID = new ArrayList<>();
	private ArrayList<String> hotelName = new ArrayList<>();
	
	public void add(String weekDay, String seasonName, String start, String end, String roomType, double price, int hotelID, String hotelName) {
		this.weekDay.add(weekDay);
		this.seasonName.add(seasonName);
		this.start.add(start);
		this.end.add(end);
		this.roomType.add(roomType);
		this.price.add(price);
		this.hotelID.add(hotelID);
		this.hotelName.add(hotelName);
	}
}
