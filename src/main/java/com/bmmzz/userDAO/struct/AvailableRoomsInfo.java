package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class AvailableRoomsInfo {
	private ArrayList<SingleRoomType> availableRoomsInfo = new ArrayList<SingleRoomType>();
	
	public void add(String typeName, String size, String occupancy,
			  			   String initialPrice, String numberOfAvailableRooms) {
		availableRoomsInfo.add(new SingleRoomType(typeName, size, occupancy, initialPrice, numberOfAvailableRooms));
		return;
	}
	
	private class SingleRoomType {
		private String typeName;
		private String size;
		private String occupancy;
		private String initialPrice;
		private String numberOfAvailableRooms;
		
		public SingleRoomType(String typeName, String size, String occupancy,
							  String initialPrice, String numberOfAvailableRooms) {
			this.typeName = typeName;
			this.size = size;
			this.occupancy = occupancy;
			this.initialPrice = initialPrice;
			this.numberOfAvailableRooms = numberOfAvailableRooms;
		}
	}
}
