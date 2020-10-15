package com.bmmzz;

import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Pattern;

public class UserDAO {
	private static ArrayList<String> usernameArr = new ArrayList<String>();
	private static ArrayList<String> passwordArr = new ArrayList<String>();
	private static ArrayList<String> roleArr = new ArrayList<String>();
	
	private UserDAO() {	}
	
	public static void addUser(String username, String password, String role) {
		if(usernameArr.contains(username))
			return;
		
		usernameArr.add(username);
		passwordArr.add(password);
		roleArr.add(role);
	}
	
	public static void connectToUserDAO() {
		addUser("admin", "password", "admin");
		addUser("madik", "passwordOfMadik", "guest");
		addUser("paul", "passwordOfPaul", "manager");
		addUser("alice", "passwordOfAlice", "desk-clerk");
	}
	
	public static boolean userExists(String username) {
		return usernameArr.contains(username);
	}
	
	private static boolean checkAuth(String username, String password) {
		if(!usernameArr.contains(username))
			return false;
		
		int id = usernameArr.indexOf(username);
		return passwordArr.get(id).equals(password);
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
		byte[] decodedBytes = Base64.getDecoder().decode(auth);
		String decodedAuth = new String(decodedBytes);
		
		if( !Pattern.compile(".+:.+").matcher(decodedAuth).matches() )
			return false;
			
		String username = decodedAuth.split(":", 2)[0];
		String password = decodedAuth.split(":", 2)[1];
		
		if(!checkAuth(username, password))
			return false;
		
		int id = usernameArr.indexOf(username);
		return roleArr.get(id).equals(role);
	}
	
	public static String getRole(String auth) {
		byte[] decodedBytes = Base64.getDecoder().decode(auth);
		String decodedAuth = new String(decodedBytes);
		
		if( !Pattern.compile(".+:.+").matcher(decodedAuth).matches() )
			return null;
		
		String username = decodedAuth.split(":", 2)[0];
		
		int id = usernameArr.indexOf(username);
		return roleArr.get(id);
	}
	
	public static String getEncodedAuth(String username, String password) {
		String auth = username + ":" + password;
		return Base64.getEncoder().encodeToString(auth.getBytes());
	}
}