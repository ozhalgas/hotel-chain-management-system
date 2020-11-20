package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class AvailableRoomsInfo {
	private ArrayList<SingleRoomType> availableRoomsInfo = new ArrayList<SingleRoomType>();
	
	public void add(String typeName, double size, int occupancy,
			  		double initialPrice, int numberOfAvailableRooms, ArrayList<String> features) {
		availableRoomsInfo.add(new SingleRoomType(typeName, size, occupancy, initialPrice, numberOfAvailableRooms, features));
		return;
	}
	
	private class SingleRoomType {
		private String typeName;
		private double size;
		private int occupancy;
		private double initialPrice;
		private int numberOfAvailableRooms;
		private ArrayList<String> features;
		
		public SingleRoomType(String typeName, double size, int occupancy,
							  double initialPrice, int numberOfAvailableRooms, ArrayList<String> features) {
			this.typeName = typeName;
			this.size = size;
			this.occupancy = occupancy;
			this.initialPrice = initialPrice;
			this.numberOfAvailableRooms = numberOfAvailableRooms;
			this.features = features;
		}
	}
}
