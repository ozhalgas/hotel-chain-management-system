package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class HotelFeaturesInfo {
	private ArrayList<String> features = new ArrayList<String>();
	private ArrayList<Double> costs = new ArrayList<Double>();
	private ArrayList<String> startTimes = new ArrayList<String>();
	private ArrayList<String> endTimes = new ArrayList<String>();
	
	public void addFeature(String feature, Double cost, String startTime, String endTime) {
		this.features.add(feature);
		this.costs.add(cost);
		this.startTimes.add(startTime);
		this.endTimes.add(endTime);
	}
}
