package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class SeasonPriceInfo {
	
	private ArrayList<String> weekDay = new ArrayList<>();
	private ArrayList<String> seasonName = new ArrayList<>();
	private ArrayList<String> roomType = new ArrayList<>();
	private ArrayList<Double> price = new ArrayList<>();
	
	public void add(String weekDay, String seasonName, String roomType, double price) {
		this.weekDay.add(weekDay);
		this.seasonName.add(seasonName);
		this.roomType.add(roomType);
		this.price.add(price);
	}
}
