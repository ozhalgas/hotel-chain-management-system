package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}