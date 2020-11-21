package com.bmmzz.userDAO.struct;

public class CleaningScheduleInfo {
	String[] startTimes = new String[20];
	String[] endTimes = new String[20];
	String[][] roomNumbers = new String[20][2];
	Integer[][] floors = new Integer[20][2];
	String[][] roomTypes = new String[20][2];
	Boolean[] slotOccupied = new Boolean[20]; // False - if not slot is empty (no room to clean in the given slot - 30 min period)
	Boolean[] slotFull = new Boolean[20]; // True - if slot is full (max number of rooms are planned to clean in 30 min time period)
	
	public int startTimeToIndex(String startTime) {
		for(int i = 0; i < 20; i++) {
			if(startTimes[i].equals(startTime))
				return i;
		}
		return -1;
	}
	
	public CleaningScheduleInfo() {
		for(int i = 0; i < 20; i++) {
			String str_HH, str_mm;
			Integer HH = 9 + (i/2);
			
			if(HH/10 == 0)
				str_HH = "0" + HH.toString();
			else
				str_HH = HH.toString();
			
			if(i%2 == 0)
				str_mm = "00";
			else
				str_mm = "30";
			
			startTimes[i] = str_HH + ":" + str_mm;
			
			if(str_mm == "00")
				str_mm = "30";
			else {
				str_mm = "00";
				HH++;
				if(HH/10 == 0)
					str_HH = "0" + HH.toString();
				else
					str_HH = HH.toString();
			}
			
			endTimes[i] = str_HH + ":" + str_mm;
			
			slotOccupied[i] = false;
			slotFull[i] = false;
		}
	}
	
	public void addCleaningListItem(CleaningListItem item) {
		String time;
		Integer HH = Integer.parseInt(item.getTime().split(":")[0]);
		
		if(HH < 9) {
			time = "9:00";
		} else if(HH < 19) {
			time = item.getTime();
		} else {
			return;
		}
		
		int index = startTimeToIndex( time );
		
		for(int i = index; i < 20; i++) {
			if(slotFull[i] == false) {
				if(slotOccupied[i] == false) {
					roomNumbers[i][0] = item.getRoomNumber();
					floors[i][0] = item.getFloor();
					roomTypes[i][0] = item.getRoomType();
					
					slotOccupied[i] = true;
				} else {
					roomNumbers[i][1] = item.getRoomNumber();
					floors[i][1] = item.getFloor();
					roomTypes[i][1] = item.getRoomType();
					
					slotFull[i] = true;
				}
				break;
			}
		}
	}
	
	public int getSlotIndexOfRoom(String roomNumber, int floor, String roomType) {
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 2; j++) {
				if(this.roomNumbers[i][j] == roomNumber &&
						this.floors[i][j] == floor &&
						this.roomTypes[i][j] == roomType) {
					return i;
				}
			}
		}
		
		return -1;
	}
}
