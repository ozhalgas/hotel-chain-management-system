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
    private ArrayList<Integer> guestIDs = new ArrayList<Integer>();
    private ArrayList<String> CIDs = new ArrayList<>();
    private ArrayList<String> CODs = new ArrayList<>();

    public void addRoom(String roomNumber, int roomFloor, int cleaned, int occupied, int numberOfOccupants, String roomTypeName, int hotelID, String hotelName, int guestID, String CID, String COD) {
        roomNumbers.add(roomNumber);
        roomFloors.add(roomFloor);
        hotelIDs.add(hotelID);
        hotelNames.add(hotelName);
        roomTypeNames.add(roomTypeName);
        occupieds.add(occupied);
        cleaneds.add(cleaned);
        numbersOfOccupants.add(numberOfOccupants);
        guestIDs.add(guestID);
        CIDs.add(CID);
        CODs.add(COD);
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
    
    public ArrayList<Integer> getGuestIDs() {
        return guestIDs;
    }
    
    public ArrayList<String> getCIDs() {
        return CIDs;
    }
    
    public ArrayList<String> getCODs() {
        return CODs;
    }
    
    public void setNumOccupants(int roomNumber, int numOccupants) {
    	for (String room : this.roomNumbers) {
        	if ( room.equals( Integer.toString(roomNumber) ) ) {
        		numbersOfOccupants.set(roomNumbers.indexOf(room), numOccupants);
        	}
        }
    }

    public void setOccupied(int roomNumber, int occupied) {
    	for (String room : this.roomNumbers) {
    		if ( room.equals( Integer.toString(roomNumber) ) ) {
        		occupieds.set(roomNumbers.indexOf(room), occupied);
        	}
        }
    }
    
    public void setCleaned(int roomNumber, int cleaned) {
        for (String room : this.roomNumbers) {
        	if ( room.equals( Integer.toString(roomNumber) ) ) {
        		cleaneds.set(roomNumbers.indexOf(room), cleaned);
        	}
        }
    }
    
    public void setGuestInRoom(int roomNumber, int guestID) {
        for (String room : this.roomNumbers) {
        	if ( room.equals( Integer.toString(roomNumber) ) ) {
        		guestIDs.set(roomNumbers.indexOf(room), guestID);
        	}
        }
    }
    
    public void setCID(int roomNumber, String CID) {
        for (String room : this.roomNumbers) {
        	if ( room.equals( Integer.toString(roomNumber) ) ) {
        		CIDs.set(roomNumbers.indexOf(room), CID);
        	}
        }
    }
    
    public void setCOD(int roomNumber, String COD) {
        for (String room : this.roomNumbers) {
        	if ( room.equals( Integer.toString(roomNumber) ) ) {
        		CODs.set(roomNumbers.indexOf(room), COD);
        	}
        }
    }
}
