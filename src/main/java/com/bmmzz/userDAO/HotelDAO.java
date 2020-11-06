package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.regex.Pattern;

import com.bmmzz.userDAO.struct.GuestBookings;
import com.bmmzz.userDAO.struct.HotelBookings;
import com.bmmzz.userDAO.struct.HotelChoosingInfo;
import com.bmmzz.userDAO.struct.HotelInfo;
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
	
	public static String getHotelChoosingInfo() {
		Gson gson = new Gson();
		String json = "";
		HotelChoosingInfo hotelChoosingInfo = new HotelChoosingInfo();
		
		try {
			ResultSet resultSet =  UserDAO.executeQuery("SELECT HotelID, Name, Country, Region FROM mydb.hotel;" );
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
		
		byte[] decodedBytes = Base64.getDecoder().decode(auth);
		String decodedAuth = new String(decodedBytes);
		
		if( !Pattern.compile(".+:.+").matcher(decodedAuth).matches() )
			return null;
		
		String username = decodedAuth.split(":", 2)[0];
		
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.reserves, mydb.employee, mydb.schedule, mydb.guest WHERE mydb.Employee.Login= BINARY '" + username + "' and mydb.employee.EmployeeID = mydb.schedule.EmployeeID and mydb.schedule.HotelID = mydb.reserves.HotelID" );
			while(resultSet.next()) {
				hotelBookings.addBooking( resultSet.getString(32), resultSet.getString(37), resultSet.getString(1), 
						resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6));
			}
			json = gson.toJson(hotelBookings, HotelBookings.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
}