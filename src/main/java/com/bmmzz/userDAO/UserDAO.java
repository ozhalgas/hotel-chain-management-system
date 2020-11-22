package com.bmmzz.userDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.regex.Pattern;

public class UserDAO {
	
	private UserDAO() {	}
	
	protected static void executeUpdate(String query) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
	}
	
	protected static int executeQueryINT(String query) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement(query);
			resultSet = ps.executeQuery();
			
			resultSet.next();
			int result = resultSet.getInt(1);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		return -1;
	}
	
	public static boolean userExists(String login, String table) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("select Login from mydb." + table + " where Login= binary '" + login + "'");
			resultSet = ps.executeQuery(); 
			boolean userExist = resultSet.next();
			return userExist;
		} catch (SQLException e) {
			e.printStackTrace();	
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
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
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		String username = getDecodedAuth(auth)[0];
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("SELECT GuestID FROM mydb.guest WHERE Login= BINARY '" + username + "';");
			resultSet = ps.executeQuery();
			
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
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
			Database db = null;
			Connection connection = null;
			PreparedStatement ps = null;
			ResultSet resultSet = null;
			PreparedStatement ps2 = null;
			ResultSet resultSet2 = null;
			
			try {
				db = new Database();
				connection = db.getConnection();

				ps = connection.prepareStatement("SELECT EmployeeID FROM mydb.employee WHERE Login= BINARY '" + login + "'");
				resultSet = ps.executeQuery();

				int employeeID = 0;
				try {
					resultSet.next();
					employeeID = resultSet.getInt(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				ps2 = connection.prepareStatement("SELECT Position FROM mydb.schedule WHERE EmployeeID= BINARY " + employeeID);
				resultSet2 = ps2.executeQuery();

				String position = "";
				try {
					resultSet2.next();
					position = resultSet2.getString(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				if(position.equalsIgnoreCase("Manager") || position.equalsIgnoreCase("Desk-clerk"))
					return position.toLowerCase();
				else
					return null;
			} catch (SQLException e) {
				e.printStackTrace();	
			} finally {
				try { resultSet.close(); } catch (Exception e) {}
				try { resultSet2.close(); } catch (Exception e) {}
				try { ps.close(); } catch (Exception e) {}
				try { ps2.close(); } catch (Exception e) {}
				try { connection.close(); } catch (Exception e) {}
			}
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