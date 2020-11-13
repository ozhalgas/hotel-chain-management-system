package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.RowSet;

import com.bmmzz.userDAO.struct.HotelBookings;
import com.bmmzz.userDAO.struct.HotelChoosingInfo;
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
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.reserves, mydb.employee, mydb.schedule, mydb.guest, mydb.hotel WHERE mydb.Employee.Login= BINARY '" + username + "' and mydb.employee.EmployeeID = mydb.schedule.EmployeeID and mydb.schedule.HotelID = mydb.reserves.HotelID and mydb.hotel.HotelID = mydb.reserves.HotelID" );
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
	
	public static void setNumOccupants(int roomNumber, int numOccupants) {
		HotelRoomsInfo hotelRooms = new HotelRoomsInfo();
		hotelRooms.setNumOccupants(roomNumber, numOccupants);
	}
	
	public static void setRoomOccupied(int roomNumber, int occupied) {
		HotelRoomsInfo hotelRooms = new HotelRoomsInfo();
		hotelRooms.setOccupied(roomNumber, occupied);
	}
	
	public static void setGuestInRoom(int roomNumber, int guestID) {
		HotelRoomsInfo hotelRooms = new HotelRoomsInfo();
		hotelRooms.setGuestInRoom(roomNumber, guestID);
	}
	
	public static void setCID(int roomNumber, String CID) {
		HotelRoomsInfo hotelRooms = new HotelRoomsInfo();
		hotelRooms.setCID(roomNumber, CID);
	}
	
	public static void setCOD(int roomNumber, String COD) {
		HotelRoomsInfo hotelRooms = new HotelRoomsInfo();
		hotelRooms.setCOD(roomNumber, COD);
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
				
		//guestIDs that will be full-filled into hotelRooms
		List<Integer> roomGuestID = new ArrayList<>();
		List<String> roomCID = new ArrayList<>();
		List<String> roomCOD = new ArrayList<>();
		
		String todayStr = java.time.LocalDate.now().toString();
		Date todayDate = new Date();
		try {
			todayDate = new SimpleDateFormat("yyyy-mm-dd").parse(todayStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			ResultSet resSS = UserDAO.executeQuery("SELECT * FROM mydb.single_stay");
			while(resSS.next()) {
				Date tempCheckOutDate = new Date();
				try {
					tempCheckOutDate = new SimpleDateFormat("yyyy-mm-dd").parse(resSS.getString(2));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(todayDate.before(tempCheckOutDate)) {
					checkInD.add(resSS.getString(1));
					checkOutD.add(resSS.getString(2));
					roomN.add(resSS.getString(4));
					roomF.add(resSS.getInt(5));
					gID.add(resSS.getInt(6));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {	
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.employee, mydb.schedule, mydb.room, mydb.hotel WHERE mydb.Employee.Login= BINARY '" + username+ "' and mydb.employee.EmployeeID = mydb.schedule.EmployeeID and mydb.schedule.HotelID = mydb.room.HotelID and mydb.schedule.HotelID = mydb.hotel.HotelID" );
			int ind = 0;
			while(resultSet.next()) {
				boolean exists = false;
				for(int i = 0; i < roomN.size(); ++i) {
					if(resultSet.getString(25).equals(roomN.get(i)) && resultSet.getInt(26) == roomF.get(i)) {
						roomGuestID.add(gID.get(i));
						roomCID.add(checkInD.get(i));
						roomCOD.add(checkOutD.get(i));
						exists = true;
						break;
					}
				}
				if(!exists) { 
					roomGuestID.add(-1);
					roomCID.add(null);
					roomCOD.add(null);
				}
				hotelRooms.addRoom( resultSet.getString(25), resultSet.getInt(26), resultSet.getInt(27), resultSet.getInt(28), resultSet.getInt(29), resultSet.getString(30), resultSet.getInt(31), resultSet.getNString(33), roomGuestID.get(ind), roomCID.get(ind), roomCOD.get(ind));
				++ind;
			}
			json = gson.toJson(hotelRooms, HotelRoomsInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
}