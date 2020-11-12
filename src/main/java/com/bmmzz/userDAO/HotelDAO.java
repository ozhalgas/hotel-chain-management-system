package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.RowSet;

import com.bmmzz.userDAO.struct.HotelBookings;
import com.bmmzz.userDAO.struct.HotelChoosingInfo;
import com.bmmzz.userDAO.struct.HotelInfo;
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
	
	public static String getHotelRooms(String auth) {
		Gson gson = new Gson();
		HotelRoomsInfo hotelRooms = new HotelRoomsInfo();
		String json = "";
		
		String username = UserDAO.getDecodedAuth(auth)[0];
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.employee, mydb.schedule, mydb.room, mydb.hotel WHERE mydb.Employee.Login= BINARY 'gal' and mydb.employee.EmployeeID = mydb.schedule.EmployeeID and mydb.schedule.HotelID = mydb.room.HotelID and mydb.schedule.HotelID = mydb.hotel.HotelID" );
			while(resultSet.next()) {
				hotelRooms.addRoom( resultSet.getString(25), resultSet.getInt(26), resultSet.getInt(27), resultSet.getInt(28), resultSet.getInt(29), resultSet.getString(30), resultSet.getInt(31), resultSet.getNString(33));
			}
			json = gson.toJson(hotelRooms, HotelRoomsInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
}