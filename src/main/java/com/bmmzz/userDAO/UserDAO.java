package com.bmmzz.userDAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class UserDAO {
	private static Connection connection = null;
	private static String url = "jdbc:mysql://localhost:3306/?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
	
	private static String username = "admin";
	private static String password = "admin";
	
	private UserDAO() {	}
	
	public static void connectToUserDAO() {
		try {
				Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
				connection = DriverManager.getConnection(url, username, password);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void executeUpdate(String query) {
		try {
			connection.prepareStatement(query).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static int executeQueryINT(String query) {
		try {
			ResultSet resultSet = connection.prepareStatement(query).executeQuery();
			resultSet.next();
			int result = resultSet.getInt(1);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private static ResultSet executeQuery(String query) {
		try {
			ResultSet resultSet = connection.prepareStatement(query).executeQuery();
			return resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void addEmployee(EmployeeRegistrationInfo employee) {
		if(userExists(employee.login, "employee"))
			return;

		String employeeID = "1"; 
		if( executeQueryINT("SELECT COUNT(*) FROM mydb.guest") > 0 ) {
			try {
				ResultSet resultSet = executeQuery("SELECT guestID FROM mydb.employee ORDER BY guestID DESC LIMIT 1;");
				resultSet.next();
				employeeID = Integer.toString(Integer.parseInt(resultSet.getString(1)) + 1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	

		executeUpdate("INSERT INTO mydb.employee VALUES ('" + employeeID +"', '" + employee.fullName + "', '" + employee.gender + "', "
				+ "'" + employee.dateOfBirth +"', '" + employee.identificationType + "', '" + employee.identificationNumber + "', "
				+ "'" + employee.citizenship + "', '" + employee.visa + "', '" + employee.address + "', '" + employee.bankCardNumber + "', "
				+ "'" + employee.emailAdress + "', '" + employee.homePhoneNumber + "', '" + employee.mobilePhoneNumber + "', '" + employee.login + "', "
				+ "'" + employee.password + "')");
		executeUpdate("INSERT INTO mydb.employee_at_hotel VALUES ('" + employeeID + "', '" + employee.hotelID + "', '" + employee.position + "', "
				+ "'" + employee.status + "', '" + employee.payRate + "', '" + employee.startDate + "', '" + employee.endDate + "')");
	}
	
	public static void addGuest(GuestRegistrationInfo guest) {
		if(userExists(guest.login, "guest"))
					return;
		
		String guestID = "1"; 
		if( executeQueryINT("SELECT COUNT(*) FROM mydb.guest") > 0 ) {
			try {
				ResultSet resultSet = executeQuery("SELECT guestID FROM mydb.guest ORDER BY guestID DESC LIMIT 1;");
				resultSet.next();
				guestID = Integer.toString(Integer.parseInt(resultSet.getString(1)) + 1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		executeUpdate("insert into mydb.guest "
		+ "(GuestID, FullName, IdentificationType, IdentificationNumber, Category, Address, HomePhoneNumber, MobilePhoneNumber, Login, Password) "
		+ "values "
		+ "('" + guestID + "', '" + guest.fullName +"', '" + guest.identificationType + "', '" + guest.identificationNumber + "', "
		+ "'" + guest.category + "', '" + guest.address +"', '" + guest.homePhoneNumber + "', '" + guest.mobilePhoneNumber + "', '" + guest.login +"', '" + guest.password +"')");
	}
	
	public static boolean userExists(String login, String table) {
		try {
			ResultSet resultSet = connection.prepareStatement("select Login from mydb." + table + " where Login= binary '" + login + "'").executeQuery(); 
			resultSet.next();
			boolean userExist = !resultSet.getString(1).isEmpty();
			return userExist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean userExists(String login) {
		return userExists(login, "guest") || userExists(login, "employee");
	}
	
	
	private static boolean checkAuth(String login, String password) {
		if(login.equals("admin") && password.equals("password") )
			return true;
			
		if(executeQueryINT("SELECT EXISTS(SELECT * from mydb.guest WHERE Login= BINARY '" + login + "' AND Password= BINARY '" + password + "')") != 0) 
			return true;
		
		boolean result = executeQueryINT("SELECT EXISTS(SELECT * from mydb.employee WHERE Login= BINARY '" + login + "' AND Password= BINARY '" + password + "')") != 0;
		return result;
	}
	
	public static boolean checkAuth(String auth) {
		byte[] decodedBytes = Base64.getDecoder().decode(auth);
		String decodedAuth = new String(decodedBytes);
		
		if( !Pattern.compile(".+:.+").matcher(decodedAuth).matches() )
			return false;
		
		String username = decodedAuth.split(":", 2)[0];
		String password = decodedAuth.split(":", 2)[1];
		
		return checkAuth(username, password);
	}
	
	public static boolean checkRoleAndAuth(String auth, String role) {
		return checkAuth(auth) && getRole(auth).equals(role);
	}
	
	public static String getRole(String auth) {
		byte[] decodedBytes = Base64.getDecoder().decode(auth);
		String decodedAuth = new String(decodedBytes);
		
		if( !Pattern.compile(".+:.+").matcher(decodedAuth).matches() )
			return null;
		
		String login = decodedAuth.split(":", 2)[0];
		
		if(login.equals("admin"))
			return "admin";
		
		if(userExists(login, "guest") ) {
			return "guest";
		}
		
		if(userExists(login, "employee")) {
			ResultSet resultSet = executeQuery("SELECT EmployeeID FROM mydb.employee WHERE Login= BINARY '" + login + "'");
			String employeeID = "";
			try {
				resultSet.next();
				employeeID = resultSet.getString(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			resultSet = executeQuery("SELECT Position FROM mydb.employee_at_hotel WHERE Employee_EmployeeID= BINARY '" + employeeID + "'" );
			String position = "";
			try {
				resultSet.next();
				position = resultSet.getString(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(position.equalsIgnoreCase("Manager") || position.equalsIgnoreCase("Desk-clerk"))
				return position.toLowerCase();
			else
				return null;
		}
		return null;
		
	}
	
	public static String getEncodedAuth(String username, String password) {
		String auth = username + ":" + password;
		return Base64.getEncoder().encodeToString(auth.getBytes());
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
			ResultSet resultSet = executeQuery("SELECT * FROM mydb.guest WHERE Login= BINARY '" + username + "'" );
			resultSet.next();
			guestInfo.setGuestID( resultSet.getString(1) );
			guestInfo.setFullName( resultSet.getString(2) );
			guestInfo.setIdentificationType( resultSet.getString(3) );
			guestInfo.setIdentificationNumber( resultSet.getString(4) );
			guestInfo.setCategory( resultSet.getString(5) );
			guestInfo.setAddress( resultSet.getString(6) );
			guestInfo.setHomePhoneNumber( resultSet.getString(7) );
			guestInfo.setMobilePhoneNumber( resultSet.getString(8) );
			
			json = gson.toJson(guestInfo, GuestInfo.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String getEmployeeInfo(String auth) {
		Gson gson = new Gson();
		EmployeeInfo employeeInfo = new EmployeeInfo();
		String json = "";
		
		byte[] decodedBytes = Base64.getDecoder().decode(auth);
		String decodedAuth = new String(decodedBytes);
		
		if( !Pattern.compile(".+:.+").matcher(decodedAuth).matches() )
			return null;
		
		String username = decodedAuth.split(":", 2)[0];
		
		try {
			ResultSet resultSet = executeQuery("SELECT * FROM mydb.employee WHERE Login= BINARY '" + username + "'" );
			resultSet.next();
			employeeInfo.setEmployeeID( resultSet.getString(1) );
			employeeInfo.setFullName( resultSet.getString(2) );
			employeeInfo.setGender( resultSet.getString(3) );
			employeeInfo.setDateOfBirth( resultSet.getDate(4).toString() );
			employeeInfo.setIdentificationType( resultSet.getString(5) );
			employeeInfo.setIdentificationNumber( resultSet.getString(6) );
			employeeInfo.setCitizenShip( resultSet.getString(7) );
			employeeInfo.setVise( resultSet.getString(8) );
			employeeInfo.setAddress( resultSet.getString(9) );
			employeeInfo.setBankCardNumber( resultSet.getString(10) );
			employeeInfo.setEmailAdress( resultSet.getString(11) );
			employeeInfo.setHomePhoneNumber( resultSet.getString(12) );
			employeeInfo.setMobilePhoneNumber( resultSet.getString(13) );
			
			resultSet = executeQuery("SELECT * FROM mydb.employee_at_hotel WHERE Employee_EmployeeID= BINARY '" + employeeInfo.getEmployeeID() + "'" );
			resultSet.next();
			employeeInfo.setHotelID( resultSet.getString(2) );
			employeeInfo.setPosition( resultSet.getString(3) );
			employeeInfo.setStatus( resultSet.getString(4) );
			employeeInfo.setPayrate( resultSet.getString(5) );
			employeeInfo.setStartDate( resultSet.getDate(6).toString() );
			employeeInfo.setEndDate( resultSet.getDate(7).toString() );
			
			resultSet = executeQuery("SELECT Name FROM mydb.hotel WHERE HotelID= BINARY '" + employeeInfo.getHotelID() + "'" );
			resultSet.next();
			employeeInfo.setHotelName( resultSet.getString(1) );
			
			json = gson.toJson(employeeInfo, EmployeeInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String getAllHotels() {
		Gson gson = new Gson();
		String json = "";
		Hotels hotels = new Hotels();
		
		try {
			ResultSet resultSet =  executeQuery("SELECT HotelID, Name FROM mydb.hotel;" );
			while(resultSet.next()) {
				hotels.addHotel(resultSet.getString(1), resultSet.getString(2));
			}
			json = gson.toJson(hotels, Hotels.class);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
}