package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.bmmzz.service.EmployeeScheduleService;
import com.bmmzz.userDAO.struct.CleaningListItem;
import com.bmmzz.userDAO.struct.CleaningScheduleInfo;
import com.bmmzz.userDAO.struct.HotelBookings;
import com.bmmzz.userDAO.struct.HotelChoosingInfo;
import com.bmmzz.userDAO.struct.HotelFeaturesInfo;
import com.bmmzz.userDAO.struct.HotelInfo;
import com.bmmzz.userDAO.struct.HotelOccupied;
import com.bmmzz.userDAO.struct.HotelRoomsInfo;
import com.bmmzz.userDAO.struct.Hotels;
import com.google.gson.Gson;

public class HotelDAO {
	
	private HotelDAO() {}
	
	public static String getAllHotels() {
		Gson gson = new Gson();
		String json = "";
		Hotels hotels = new Hotels();
		
		try {
			ResultSet resultSet =  UserDAO.executeQuery("SELECT HotelID, Name FROM mydb.hotel;" );
			while(resultSet.next()) {
				hotels.addHotel(resultSet.getInt(1), resultSet.getString(2));
			}
			json = gson.toJson(hotels, Hotels.class);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String getHotelChoosingInfo(String auth) {
		Gson gson = new Gson();
		String json = "";
		HotelChoosingInfo hotelChoosingInfo = new HotelChoosingInfo();
		
		String role = UserDAO.getRole(auth);
		try {
			ResultSet resultSet;
			if(role.equals("desk-clerk"))
				resultSet = UserDAO.executeQuery("SELECT HotelID, Name, Country, Region FROM mydb.hotel "
						+ "JOIN mydb.schedule S USING(HotelID) "
						+ "JOIN mydb.employee E USING(EmployeeID) "
						+ "WHERE E.Login = '" + UserDAO.getDecodedAuth(auth)[0] + "'");
			else
				resultSet =  UserDAO.executeQuery("SELECT HotelID, Name, Country, Region FROM mydb.hotel;" );
			while(resultSet.next()) {
				hotelChoosingInfo.addHotel(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
			}
			json = gson.toJson(hotelChoosingInfo, HotelChoosingInfo.class);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String getHotelInfo(int hotelID) {
		Gson gson = new Gson();
		String json = "";
		HotelInfo hotelInfo = new HotelInfo();
		
		try {
			ResultSet resultSet =  UserDAO.executeQuery("SELECT * FROM mydb.hotel WHERE HotelID = " + hotelID + ";" );
			while(resultSet.next()) {
				hotelInfo.set(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
			}
			ResultSet resultSet2 =  UserDAO.executeQuery("SELECT phonenumber FROM mydb.phone_number WHERE HotelID = " + hotelID + ";" );
			while(resultSet2.next()) {
				hotelInfo.addPhone(resultSet2.getString(1));
			}
			json = gson.toJson(hotelInfo, HotelInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String getHotelBookings(String auth) {
		Gson gson = new Gson();
		HotelBookings hotelBookings = new HotelBookings();
		String json = "";
		
		String username = UserDAO.getDecodedAuth(auth)[0];
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.reserves, mydb.employee, mydb.schedule, mydb.guest, mydb.hotel WHERE mydb.Employee.Login= BINARY '" + username + "' and mydb.employee.EmployeeID = mydb.schedule.EmployeeID and mydb.schedule.HotelID = mydb.reserves.HotelID and mydb.hotel.HotelID = mydb.reserves.HotelID and mydb.reserves.GuestID=mydb.guest.GuestID;" );
			while(resultSet.next()) {
				hotelBookings.addBooking( resultSet.getInt(3), resultSet.getInt(40), resultSet.getString(41), resultSet.getString(32), resultSet.getString(37), resultSet.getString(1), 
						resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6));
			}
			json = gson.toJson(hotelBookings, HotelBookings.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	//NEED UPDATE FROM DB SIDE
	public static String getHotelOccupied(String auth) {
		Gson gson = new Gson();
		HotelOccupied hotelOccupied = new HotelOccupied();
		String json = "";
		
		String username = UserDAO.getDecodedAuth(auth)[0];
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.occupies" );
			while(resultSet.next()) {
				hotelOccupied.addOccupied( resultSet.getInt(3), 1, "Exclusive", "test test", 
						"12111", "1 bed Double", resultSet.getString(4), resultSet.getString(5), resultSet.getString(1), resultSet.getInt(2));
			}
			json = gson.toJson(hotelOccupied, HotelOccupied.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static void setNumOccupants(int hotelID, String roomNumber, int numOccupants) {
		UserDAO.executeUpdate("Update mydb.room " + 
				"Set numberOfOccupants = '" + numOccupants + "' " +
				"Where RoomNumber='" + roomNumber + "' and HotelID='" + hotelID + "';");
	}
	
	public static void setRoomOccupied(int hotelID, String roomNumber, int occupied) {
		UserDAO.executeUpdate("Update mydb.room " + 
				"Set Occupied = '" + occupied + "' " +
				"Where RoomNumber='" + roomNumber + "' and HotelID='" + hotelID + "';");
	}
	
	//hotelID and roomTypeNames should be considered
	public static String getHotelRooms(String auth) {
		Gson gson = new Gson();
		HotelRoomsInfo hotelRooms = new HotelRoomsInfo();
		String json = "";
		
		String username = UserDAO.getDecodedAuth(auth)[0];
		
		//SingleStays for today
		List<String> checkInD = new ArrayList<>();
		List<String> checkOutD = new ArrayList<>();
		List<String> roomN = new ArrayList<>();
		List<Integer> roomF = new ArrayList<>();
		List<Integer> gID = new ArrayList<>();
		List<String> gFN = new ArrayList<>();
		List<String> gMPN = new ArrayList<>();
				
		//guestIDs that will be full-filled into hotelRooms
		List<Integer> roomGuestID = new ArrayList<>();
		List<String> roomCID = new ArrayList<>();
		List<String> roomCOD = new ArrayList<>();
		List<String> roomGuestFN = new ArrayList<>();
		List<String> roomGuestMPN = new ArrayList<>();
		
		String todayStr = java.time.LocalDate.now().toString();
		Date todayDate = new Date();
		try {
			todayDate = new SimpleDateFormat("yyyy-mm-dd").parse(todayStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int hID = EmployeeDAO.getHotelID(auth);
		try {
			//select single stays only in hotel, where clerk works
			ResultSet resSS = UserDAO.executeQuery("SELECT * FROM mydb.single_stay, mydb.guest where mydb.single_stay.HotelID='" + hID + "' and mydb.single_stay.GuestID=mydb.guest.GuestID;");
			while(resSS.next()) {
				Date tempCheckOutDate = new Date();
				try {
					tempCheckOutDate = new SimpleDateFormat("yyyy-mm-dd").parse(resSS.getString(2));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(todayDate.before(tempCheckOutDate) || todayDate.equals(tempCheckOutDate)) {
					checkInD.add(resSS.getString(1));
					checkOutD.add(resSS.getString(2));
					roomN.add(resSS.getString(4));
					roomF.add(resSS.getInt(5));
					gID.add(resSS.getInt(6));
					gFN.add(resSS.getString(10));
					gMPN.add(resSS.getString(15));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {	
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.employee, mydb.schedule, mydb.room, mydb.hotel, mydb.room_type " +
													   "WHERE mydb.Employee.Login= BINARY '" + username+ "' and mydb.employee.EmployeeID = mydb.schedule.EmployeeID and mydb.schedule.HotelID = mydb.room.HotelID and mydb.schedule.HotelID = mydb.hotel.HotelID " +
													   "and mydb.room.RoomTypeName=mydb.room_type.TypeName and mydb.room_type.HotelID=mydb.hotel.HotelID order by mydb.room.RoomNumber;");
			int ind = 0;
			while(resultSet.next()) {
				boolean exists = false;
				for(int i = 0; i < roomN.size(); ++i) {
					if(resultSet.getString(25).equals(roomN.get(i)) && resultSet.getInt(26) == roomF.get(i)) {
						roomGuestID.add(gID.get(i));
						roomCID.add(checkInD.get(i));
						roomCOD.add(checkOutD.get(i));
						roomGuestFN.add(gFN.get(i));
						roomGuestMPN.add(gMPN.get(i));
						exists = true;
						break;
					}
				}
				if(!exists) { 
					roomGuestID.add(-1);
					roomCID.add(null);
					roomCOD.add(null);
					roomGuestFN.add(null);
					roomGuestMPN.add(null);
				}
				hotelRooms.addRoom( resultSet.getString(25), resultSet.getInt(26), resultSet.getInt(27), resultSet.getInt(28), resultSet.getInt(29), resultSet.getString(30), resultSet.getInt(31), resultSet.getNString(33), roomGuestID.get(ind), roomCID.get(ind), roomCOD.get(ind), resultSet.getInt(39), roomGuestFN.get(ind), roomGuestMPN.get(ind));
				++ind;
			}
			json = gson.toJson(hotelRooms, HotelRoomsInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String getHotelFeaturesInfo(String auth) {
		int hotelID = EmployeeDAO.getHotelID(auth);
		
		Gson gson = new Gson();
		HotelFeaturesInfo hotelFeaturesInfo = new HotelFeaturesInfo();
		String json = "";
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.additional_feature WHERE HotelID = " + hotelID + ";");
			while(resultSet.next()) {
				String[] startTime = resultSet.getString(3).split(":");
				String[] endTime = resultSet.getString(4).split(":");
				String[] now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).split(":");
				
				boolean insideTimePeriod = false;
				if(Integer.parseInt(startTime[0]) <= Integer.parseInt(now[0]) && Integer.parseInt(endTime[0]) >= Integer.parseInt(now[0])) {
					insideTimePeriod = true;
				} else if(Integer.parseInt(startTime[1]) <= Integer.parseInt(now[1]) && Integer.parseInt(endTime[1]) >= Integer.parseInt(now[1])) {
					insideTimePeriod = true;
				}
				
				if(insideTimePeriod) 
					hotelFeaturesInfo.addFeature(resultSet.getString(1), resultSet.getDouble(2), resultSet.getString(3), resultSet.getString(4));
			}
			json = gson.toJson(hotelFeaturesInfo, HotelFeaturesInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	// Example: time = 18:42 => time = 19:00 (Time step 30 min)
	public static String adjustTimeByStep(String time) {
		String str_HH = time.split(":")[0];
		String str_mm = time.split(":")[1];
		
		Integer HH = Integer.parseInt(str_HH);
		Integer mm = Integer.parseInt(str_mm);
		
		if(mm == 0) {
			return time;
		}
		
		if(mm <= 30) {
			mm = 30;
			str_mm = "30";
		} else {
			mm = 0;
			str_mm = "00";
			HH++;
			
			if(HH/10 == 0) {
				str_HH = "0" + HH.toString();
			} else {
				str_HH = HH.toString();
			}
			
			if(HH == 24) {
				HH = 0;
				str_HH = "00";
			}
		}
		
		return str_HH + ":" + str_mm;
	}
	
	// Initial insert of unclean rooms into cleaning list
	public static LinkedList<CleaningListItem> initialCleaningListInsert() {
		LinkedList<CleaningListItem> cleaningList = new LinkedList<CleaningListItem>();
		
		String now = adjustTimeByStep( LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) );
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("Select * FROM mydb.room WHERE Cleaned = 0;");
			while(resultSet.next()) {
				cleaningList.add(new CleaningListItem(now, resultSet.getString(1), resultSet.getInt(1), resultSet.getString(6), resultSet.getInt(7)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cleaningList;
	}
	
	public static String getCleaningSchedule(String auth) {
		int hotelID = EmployeeDAO.getHotelID(auth);
		
		Gson gson = new Gson();
		CleaningScheduleInfo cleaningSchedule = new CleaningScheduleInfo();
		String json = "";
		
		for(int i = 0; i < EmployeeScheduleService.cleaningList.size(); i++) {
			if(EmployeeScheduleService.cleaningList.get(i).getHotelID() == hotelID)
				cleaningSchedule.addCleaningListItem(EmployeeScheduleService.cleaningList.get(i));
		}
		
		json = gson.toJson(cleaningSchedule, CleaningScheduleInfo.class);
		
		return json;
	}
}