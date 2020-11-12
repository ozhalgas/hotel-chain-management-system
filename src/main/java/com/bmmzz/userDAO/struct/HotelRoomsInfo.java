package com.bmmzz.userDAO.struct;

import java.util.ArrayList;

public class HotelRoomsInfo {
	private ArrayList<String> roomNumbers = new ArrayList<>();
    private ArrayList<Integer> roomFloors = new ArrayList<Integer>();

    private ArrayList<Integer> hotelIDs = new ArrayList<Integer>();
    private ArrayList<String> hotelNames = new ArrayList<String>();
    private ArrayList<String> roomTypeNames = new ArrayList<String>();

    private ArrayList<Integer> occupieds = new ArrayList<>();
    private ArrayList<Integer> cleaneds = new ArrayList<Integer>();
    private ArrayList<Integer> numbersOfOccupants = new ArrayList<Integer>();

    public void addRoom(String roomNumber, int roomFloor, int cleaned, int occupied, int numberOfOccupants, String roomTypeName, int hotelID, String hotelName) {
        roomNumbers.add(roomNumber);
        roomFloors.add(roomFloor);
        hotelIDs.add(hotelID);
        hotelNames.add(hotelName);
        roomTypeNames.add(roomTypeName);
        occupieds.add(occupied);
        cleaneds.add(cleaned);
        numbersOfOccupants.add(numberOfOccupants);
    }

    public ArrayList<String> roomNumbers() {
        return roomNumbers;
    }

    public ArrayList<Integer> roomFloors() {
        return roomFloors;
    }

    public ArrayList<Integer> getHotelIDs() {
        return hotelIDs;
    }

    public ArrayList<String> getHotelNames() {
        return hotelNames;
    }

    public ArrayList<String> getRoomTypeNames() {
        return roomTypeNames;
    }

    public ArrayList<Integer> getOccupieds() {
        return occupieds;
    }

    public ArrayList<Integer> getCleaneds() {
        return cleaneds;
    }

    public ArrayList<Integer> getNumbersOfOccupants() {
        return numbersOfOccupants;
    }
    
    public void setNumOccupants(int roomNumber, int numOccupants) {
    	for (String room : this.roomNumbers) {
        	if ( room.equals( Integer.toString(roomNumber) ) ) {
        		cleaneds.set(roomNumbers.indexOf(room), numOccupants);
        	}
        }
    }

    public void setOccupieds(int roomNumber, int occupied) {
    	for (String room : this.roomNumbers) {
    		if ( room.equals( Integer.toString(roomNumber) ) ) {
        		cleaneds.set(roomNumbers.indexOf(room), occupied);
        	}
        }
    }
    
    public void setCleaneds(int roomNumber, int cleaned) {
        for (String room : this.roomNumbers) {
        	if ( room.equals( Integer.toString(roomNumber) ) ) {
        		cleaneds.set(roomNumbers.indexOf(room), cleaned);
        	}
        }
    }
}
