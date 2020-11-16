package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.bmmzz.userDAO.struct.Hotels;
import com.bmmzz.userDAO.struct.SeasonInfo;
import com.google.gson.Gson;

public class SeasonDAO {

	private SeasonDAO() {}
	
	public static String getAllSeasons(String auth) {
		Gson gson = new Gson();
		String json = "";
		SeasonInfo seasons = new SeasonInfo();
		
		try {
			ResultSet resultSet =  UserDAO.executeQuery("Select * From mydb.time_period t, mydb.initial_price p, mydb.hotel h Where p.hotelid='" + EmployeeDAO.getHotelID(auth) + "' and t.dayoftheweek=p.dayoftheweek and t.seasonname=p.seasonname and h.hotelid='" + EmployeeDAO.getHotelID(auth) + "'");
			while(resultSet.next()) {
				seasons.add(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getDouble(9), EmployeeDAO.getHotelID(auth), resultSet.getString(11));
			}
			json = gson.toJson(seasons, SeasonInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return json;
	}
}
