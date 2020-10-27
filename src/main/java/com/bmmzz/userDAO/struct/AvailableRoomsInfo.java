package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class AvailableRoomsInfo {
	private ArrayList<SingleRoomType> availableRoomsInfo = new ArrayList<SingleRoomType>();
	
	public void add(String typeName, double size, int occupancy,
			  		double initialPrice, int numberOfAvailableRooms) {
		availableRoomsInfo.add(new SingleRoomType(typeName, size, occupancy, initialPrice, numberOfAvailableRooms));
		return;
	}
	
	private class SingleRoomType {
		private String typeName;
		private double size;
		private int occupancy;
		private double initialPrice;
		private int numberOfAvailableRooms;
		
		public SingleRoomType(String typeName, double size, int occupancy,
							  double initialPrice, int numberOfAvailableRooms) {
			this.typeName = typeName;
			this.size = size;
			this.occupancy = occupancy;
			this.initialPrice = initialPrice;
			this.numberOfAvailableRooms = numberOfAvailableRooms;
		}
	}
}
