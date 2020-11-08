package com.bmmzz.userDAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.regex.Pattern;

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
	
	protected static void executeUpdate(String query) {
		try {
			connection.prepareStatement(query).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected static int executeQueryINT(String query) {
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
	
	protected static ResultSet executeQuery(String query) {
		try {
			ResultSet resultSet = connection.prepareStatement(query).executeQuery();
			return resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean userExists(String login, String table) {
		try {
			ResultSet resultSet = connection.prepareStatement("select Login from mydb." + table + " where Login= binary '" + login + "'").executeQuery(); 
			boolean userExist = resultSet.next();
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
		String[] loginAndPassword = getDecodedAuth(auth);
		return checkAuth(loginAndPassword[0], loginAndPassword[1]);
	}
	
	public static int getGuestID(String auth) {
		String username = getDecodedAuth(auth)[0];
		
		ResultSet resultSet = executeQuery("SELECT GuestID FROM mydb.guest WHERE Login= BINARY '" + username + "';");
		try {
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static boolean checkRoleAndAuth(String auth, String... roles) {
		if(!checkAuth(auth))
			return false;
		
		for(String role : roles) {
			if(getRole(auth).equals(role))
				return true;
		}
		return false;
	}
	
	public static String getRole(String auth) {
		String login = getDecodedAuth(auth)[0];
		
		if(login.equals("admin"))
			return "admin";
		
		if(userExists(login, "guest") ) {
			return "guest";
		}
		
		if(userExists(login, "employee")) {
			ResultSet resultSet = executeQuery("SELECT EmployeeID FROM mydb.employee WHERE Login= BINARY '" + login + "'");
			int employeeID = 0;
			try {
				resultSet.next();
				employeeID = resultSet.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			resultSet = executeQuery("SELECT Position FROM mydb.schedule WHERE EmployeeID= BINARY " + employeeID);
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
	
	public static String[] getDecodedAuth(String auth) {
		byte[] decodedBytes = Base64.getDecoder().decode(auth);
		String decodedAuth = new String(decodedBytes);
		
		if( !Pattern.compile(".+:.+").matcher(decodedAuth).matches() )
			return null;
		
		String[] loginAndPassword = new String[2]; 
		loginAndPassword[0] = decodedAuth.split(":", 2)[0];
		loginAndPassword[1] = decodedAuth.split(":",2)[1];
		return loginAndPassword;
	}
	
	public static String getEncodedAuth(String username, String password) {
		String auth = username + ":" + password;
		return Base64.getEncoder().encodeToString(auth.getBytes());
	}
}