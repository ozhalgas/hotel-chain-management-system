package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bmmzz.userDAO.struct.GuestBookings;
import com.bmmzz.userDAO.struct.GuestInfo;
import com.bmmzz.userDAO.struct.GuestRegistrationInfo;
import com.google.gson.Gson;

public class GuestDAO {
	
	private GuestDAO() {}
	
	public static void addGuest(GuestRegistrationInfo guest) {
		if(UserDAO.userExists(guest.getLogin(), "guest"))
					return;
		
		int guestID = 1; 
		if( UserDAO.executeQueryINT("SELECT COUNT(*) FROM mydb.guest") > 0 ) {
			try {
				ResultSet resultSet = UserDAO.executeQuery("SELECT guestID FROM mydb.guest ORDER BY guestID DESC LIMIT 1;");
				resultSet.next();
				guestID = resultSet.getInt(1) + 1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		UserDAO.executeUpdate("insert into mydb.guest "
		+ "values "
		+ "(" + guestID + ", '" + guest.getFullName() +"', '" + guest.getIdentificationType() + "', '" + guest.getIdentificationNumber() + "', "
		+ "'" + guest.getAddress() +"', '" + guest.getHomePhoneNumber() + "', '" + guest.getMobilePhoneNumber() + "', '" + guest.getLogin() +"', '" + guest.getPassword() +"')");
		
		UserDAO.executeUpdate("insert into mydb.guest_belongs_category values ('" + guest.getCategoryName() + "', " + guestID + ")");
	}
	
	public static String getGuestInfo(String auth) {
		Gson gson = new Gson();
		GuestInfo guestInfo = new GuestInfo();
		String json = "";
		
		String username = UserDAO.getDecodedAuth(auth)[0];
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.guest WHERE Login= BINARY '" + username + "'" );
			if(!resultSet.next()) {return null;}
			guestInfo.setGuestID( resultSet.getInt(1) );
			guestInfo.setFullName( resultSet.getString(2) );
			guestInfo.setIdentificationType( resultSet.getString(3) );
			guestInfo.setIdentificationNumber( resultSet.getString(4) );
			guestInfo.setAddress( resultSet.getString(5) );
			guestInfo.setHomePhoneNumber( resultSet.getString(6) );
			guestInfo.setMobilePhoneNumber( resultSet.getString(7) );
			
			resultSet = UserDAO.executeQuery("SELECT * FROM mydb.guest_belongs_category WHERE GuestID= BINARY '" + guestInfo.getGuestID() + "'" );
			resultSet.next();
			guestInfo.setCategoryName(resultSet.getString(1));
			
			json = gson.toJson(guestInfo, GuestInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String getGuestInfo(int guestID) {
		try {
			ResultSet resultSet = UserDAO.executeQuery("Select Login, Password FROM mydb.guest WHERE GuestID = " + guestID);
			if(!resultSet.next()) {return null;}
			String auth = UserDAO.getEncodedAuth(resultSet.getString(1), resultSet.getString(2));
			return getGuestInfo(auth);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getGuestBookings(String auth) {
		Gson gson = new Gson();
		GuestBookings guestBookings = new GuestBookings();
		String json = "";
		
		String username = UserDAO.getDecodedAuth(auth)[0];
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.reserves, mydb.guest, mydb.hotel WHERE Login= BINARY '" + username + "' and mydb.reserves.GuestID = mydb.guest.GuestID and mydb.reserves.HotelID = mydb.hotel.HotelID" );
			while(resultSet.next()) {
				guestBookings.addBooking( resultSet.getString(1), resultSet.getInt(2), resultSet.getString(17), resultSet.getString(18), resultSet.getString(19), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6) );
			}
			
			json = gson.toJson(guestBookings, GuestBookings.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static void removeBooking(String auth, int hotelID, String startDate, String endDate, String typeName) {		
		
		try {
			String username = UserDAO.getDecodedAuth(auth)[0];
			
			startDate = startDate.replace(':', '-');
			endDate = endDate.replace(':', '-');
			typeName = typeName.replace(':', ' ');
			
			String sql = "DELETE mydb.reserves FROM mydb.reserves, mydb.guest WHERE Login= BINARY '" + username + "' and mydb.reserves.GuestID = mydb.guest.GuestID"
					+ " and mydb.reserves.HotelID= BINARY '" + Integer.toString(hotelID) + "'"
					+ " and mydb.reserves.CheckInDate= BINARY '" + startDate + "'"
					+ " and mydb.reserves.CheckOutDate= BINARY '" + endDate + "'"
					+ " and mydb.reserves.RoomTypeName= BINARY '" + typeName + "'";
			
			System.out.println(sql);

			UserDAO.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public static void removeBooking(int guestID, int hotelID, String startDate, String endDate, String typeName) {		
		
		try {			
			startDate = startDate.replace(':', '-');
			endDate = endDate.replace(':', '-');
			typeName = typeName.replace(':', ' ');
			
			String sql = "DELETE mydb.reserves FROM mydb.reserves, mydb.guest WHERE mydb.guest.GuestID= BINARY '" + guestID + "' and mydb.reserves.GuestID = mydb.guest.GuestID"
					+ " and mydb.reserves.HotelID= BINARY '" + Integer.toString(hotelID) + "'"
					+ " and mydb.reserves.CheckInDate= BINARY '" + startDate + "'"
					+ " and mydb.reserves.CheckOutDate= BINARY '" + endDate + "'"
					+ " and mydb.reserves.RoomTypeName= BINARY '" + typeName + "'";
			
			System.out.println(sql);

			UserDAO.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}