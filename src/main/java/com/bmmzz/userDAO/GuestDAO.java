package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.regex.Pattern;

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
		
		byte[] decodedBytes = Base64.getDecoder().decode(auth);
		String decodedAuth = new String(decodedBytes);
		
		if( !Pattern.compile(".+:.+").matcher(decodedAuth).matches() )
			return null;
		
		String username = decodedAuth.split(":", 2)[0];
		
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.guest WHERE Login= BINARY '" + username + "'" );
			resultSet.next();
			guestInfo.setGuestID( resultSet.getInt(1) );
			guestInfo.setFullName( resultSet.getString(2) );
			guestInfo.setIdentificationType( resultSet.getString(3) );
			guestInfo.setIdentificationNumber( resultSet.getString(4) );
			guestInfo.setAddress( resultSet.getString(6) );
			guestInfo.setHomePhoneNumber( resultSet.getString(7) );
			guestInfo.setMobilePhoneNumber( resultSet.getString(8) );
			
			resultSet = UserDAO.executeQuery("SELECT * FROM mydb.guest_belongs_category WHERE GuestID= BINARY '" + guestInfo.getGuestID() + "'" );
			resultSet.next();
			guestInfo.setCategoryName(resultSet.getString(1));
			
			json = gson.toJson(guestInfo, GuestInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
}