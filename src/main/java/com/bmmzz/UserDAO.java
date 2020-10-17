package com.bmmzz;

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
	private static String databaseName = "";
	private static String url = "jdbc:mysql://localhost:3306/" + databaseName;
	
	private static String username = "Admin";
	private static String password = "1qaz2wsx3edc";
	
	private UserDAO() {	}
	
	public static void connectToUserDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
			connection = DriverManager.getConnection(url, username, password);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			int i = 0;
			while(connection == null && i < 10000000) {
				System.out.println(e);
				i++;
			}
		}
	}
	
	private static void executeUpdate(String query) {
		try {
			connection.prepareStatement(query).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean execute(String query) {
		try {
			return connection.prepareStatement(query).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static int executeQueryINT(String query) {
		try {
			ResultSet resultSet = connection.prepareStatement(query).executeQuery();
			resultSet.next();
			return resultSet.getInt(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	private static ResultSet executeQuery(String query) {
		try {
			return connection.prepareStatement(query).executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void addEmployee(String login, String password, String role) {
	}
	
	public static void addGuest(String fullName, String identificationType, String identificationNumber,
			String category, String address, String homePhoneNumber, String mobilePhoneNumber,
			String login, String password) {
			if(userExists(login, "guest"))
					return;

				String guestID = Integer.toString(executeQueryINT("SELECT COUNT(*) FROM mydb.guest"));

					executeUpdate("insert into mydb.guest "
								+ "(GuestID, FullName, IdentificationType, IdentificationNumber, Category, Address, HomePhoneNumber, MobilePhoneNumber, Login, Password) "
								+ "values "
								+ "('" + guestID + "', '" + fullName +"', '" + identificationType + "', '" + identificationNumber + "', "
								+ "'" + category + "', '" + address +"', '" + homePhoneNumber + "', '" + mobilePhoneNumber + "', '" + login +"', '" + password +"')");
	}
	
	public static boolean userExists(String login, String table) {
		return execute("SELECT EXISTS(SELECT * from mydb." + table +" WHERE Login='" + login + "')");
	}
	
	public static boolean userExists(String login) {
		return execute("SELECT EXISTS(SELECT * from mydb.guest WHERE Login='" + login + "')") ||
			   execute("SELECT EXISTS(SELECT * from mydb.employee WHERE Login='" + login + "')");
	}
	
	private static boolean checkAuth(String login, String password) {
		return execute("SELECT EXISTS(SELECT * from mydb.guest WHERE Login='" + login + "' AND Password='" + password + "')") ||
			   execute("SELECT EXISTS(SELECT * from mydb.employee WHERE Login='" + login + "' AND Password='" + password + "')") ||
			   (login.equals("admin") && password.equals("password"));
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
		
		if(userExists(login, "guest") )
			return "guest";
		
		if(userExists(login, "employee")) {
			ResultSet resultSet = executeQuery("SELECT EmployeeID FROM mydb.employee WHERE Login='" + login + "'");
			String employeeID = "";
			try {
				resultSet.next();
				employeeID = resultSet.getString(0);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			resultSet = executeQuery("SELECT Position FROM mydb.employee_at_hotel WHERE Employee_EmployeeID='" + employeeID + "'" );
			String position = "";
			try {
				resultSet.next();
				position = resultSet.getString(0);
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
		
		ResultSet resultSet = executeQuery("SELECT * FROM mydb.guest WHERE Login='" + username + "'" );
		
		try {
			resultSet.next();
			guestInfo.setGuestID( resultSet.getString(0) );
			
			resultSet.next();
			guestInfo.setFullName( resultSet.getString(0) );
			
			resultSet.next();
			guestInfo.setIdentificationType( resultSet.getString(0) );
			
			resultSet.next();
			guestInfo.setIdentificationNumber( resultSet.getString(0) );
			
			resultSet.next();
			guestInfo.setCategory( resultSet.getString(0) );
			
			resultSet.next();
			guestInfo.setAdress( resultSet.getString(0) );
			
			resultSet.next();
			guestInfo.setHomePhoneNumber( resultSet.getString(0) );
			
			resultSet.next();
			guestInfo.setMobilePhoneNumber( resultSet.getString(0) );
			
			json = gson.toJson(guestInfo, GuestInfo.class);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
}