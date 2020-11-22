package com.bmmzz.userDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.bmmzz.userDAO.struct.Hotels;
import com.bmmzz.userDAO.struct.SeasonAdsInfo;
import com.bmmzz.userDAO.struct.SeasonInfo;
import com.bmmzz.userDAO.struct.SeasonPriceInfo;
import com.google.gson.Gson;

public class SeasonDAO {

	private SeasonDAO() {}
	
	public static String getAllSeasons(String auth) {
		Gson gson = new Gson();
		String json = "";
		SeasonInfo seasons = new SeasonInfo();
		
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("Select * From mydb.time_period, mydb.hotel h, mydb.operates_during Where h.hotelid='" + EmployeeDAO.getHotelID(auth) + "' and " +
					"mydb.operates_during.hotelid='" + EmployeeDAO.getHotelID(auth) + "' and " + 
					"mydb.operates_during.seasonname=mydb.time_period.seasonname" + " group by mydb.time_period.SeasonName");
			resultSet = ps.executeQuery(); 
		
			while(resultSet.next()) {
				seasons.add(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), EmployeeDAO.getHotelID(auth), resultSet.getString(6));
			}
			json = gson.toJson(seasons, SeasonInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		return json;
	}
	
	public static String getSeasonPrice(String auth, String seasonName) {
		Gson gson = new Gson();
		String json = "";
		SeasonPriceInfo seasonPrice = new SeasonPriceInfo();
		
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("Select * From mydb.initial_price Where hotelid='" + EmployeeDAO.getHotelID(auth) + "' and seasonname='" + seasonName + "'");
			resultSet = ps.executeQuery(); 
			
			while(resultSet.next()) {
				seasonPrice.add(resultSet.getString(3), resultSet.getString(4), resultSet.getString(1), resultSet.getInt(5));
			}
			json = gson.toJson(seasonPrice, SeasonPriceInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		return json;
	}
	
	public static void deleteSeason(String auth, String seasonName, String start, String end) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			UserDAO.executeUpdate("Delete from mydb.operates_during Where seasonname='" + seasonName + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "'");
			UserDAO.executeUpdate("Delete from mydb.initial_price Where seasonname='" + seasonName + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "'");
			ps = connection.prepareStatement("Select * From mydb.operates_during Where seasonname='" + seasonName + "' and startdate='" + start + "' and enddate='" + end + "'");
			result = ps.executeQuery();
			if (!result.next()) {
				UserDAO.executeUpdate("Delete from mydb.time_period Where seasonname='" + seasonName + "' and startdate='" + start + "' and enddate='" + end + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { result.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
	}
	
	public static void createSeason(String seasonName, String startDate, String endDate, String roomTypes, String prices, List<String> days, int hotelID) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs1 = null;
		
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("Select count(*) From mydb.time_period T Where T.SeasonName='" + seasonName + "' and T.StartDate='" + startDate + "' and T.EndDate = '" + endDate + "';");
			rs1 = ps.executeQuery(); 
			
			rs1.next();
			if(rs1.getInt(1) == 0) {
				for(String day : days) {
					UserDAO.executeUpdate("INSERT INTO mydb.time_period VALUES ('"+day+"', '"+seasonName+"', '"+startDate+"', '"+endDate+"');");
				}
			}
			
			String[] roomTypesArr = roomTypes.split(":");
			String[] rtPrices = prices.split("X");
			for(int i = 0; i < roomTypesArr.length; ++i) {
				String[] pricesArr = rtPrices[i].split(":");
				int cntr = 0;
				for(String pr : pricesArr) {
					UserDAO.executeUpdate("INSERT INTO mydb.initial_price VALUES ('"+roomTypesArr[i]+"', '"+hotelID+"', '"+days.get(cntr)+"', '"+seasonName+"', '"+Double.parseDouble(pr)+"', '"+startDate+"', '"+endDate+"');");
					++cntr;
				}
			}
			
			for(String day : days) {
				UserDAO.executeUpdate("INSERT INTO mydb.operates_during VALUES ('"+hotelID+"', '"+day+"', '"+seasonName+"', '"+startDate+"', '"+endDate+"');");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { rs1.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
	}
	
	public static String getAds(String auth) {
		Gson gson = new Gson();
		String json = "";
		SeasonAdsInfo seasonAds = new SeasonAdsInfo();
		
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			if (UserDAO.getRole(auth).equals("manager")) {
				ps = connection.prepareStatement("Select * From mydb.advertisement a, mydb.hotel h Where a.hotelid='" + EmployeeDAO.getHotelID(auth) + "' and h.hotelid='" + EmployeeDAO.getHotelID(auth) + "'");
				resultSet = ps.executeQuery(); 
				while(resultSet.next()) {
					seasonAds.add(resultSet.getInt(2), resultSet.getString(4), resultSet.getString(1));
				}
			} else if (UserDAO.getRole(auth).equals("guest") || UserDAO.getRole(auth).equals("desk-clerk") || UserDAO.getRole(auth).equals("admin")) {
				ps = connection.prepareStatement("Select * From mydb.advertisement a, mydb.hotel h Where a.hotelid=h.hotelid");
				resultSet = ps.executeQuery(); 
				while(resultSet.next()) {
					seasonAds.add(resultSet.getInt(2), resultSet.getString(4), resultSet.getString(1));
				}
			}
			json = gson.toJson(seasonAds, SeasonAdsInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		return json;
	}

	public static void deleteAd(int hotelID) {
		UserDAO.executeUpdate("Delete from mydb.advertisement Where HotelID='" + hotelID + "';"); 
	}
	
	public static void updateAd(int hotelID, String adTxt) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("Select count(*) From mydb.advertisement Where HotelID='" + hotelID + "';");
			rs = ps.executeQuery(); 
			
			rs.next();
			if(rs.getInt(1) == 1) {
				UserDAO.executeUpdate("UPDATE mydb.advertisement SET Text='" + adTxt + "' WHERE HotelID='" + hotelID + "';");
			}else {
				UserDAO.executeUpdate("INSERT INTO mydb.advertisement VALUES ('" + adTxt + "', '" + hotelID + "');");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { rs.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
	}
}
