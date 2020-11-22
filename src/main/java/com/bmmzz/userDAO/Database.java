package com.bmmzz.userDAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private Connection connection = null;
	private static final String url = "jdbc:mysql://localhost:3306/?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
	
	private static final String username = "admin";
	private static final String password = "admin";
	
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
			connection = DriverManager.getConnection(url, username, password);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
			| NoSuchMethodException | SecurityException | ClassNotFoundException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
}
