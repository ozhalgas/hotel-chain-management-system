package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bmmzz.userDAO.struct.EmployeeInfo;
import com.bmmzz.userDAO.struct.EmployeeRegistrationInfo;
import com.google.gson.Gson;

public class EmployeeDAO {
	
	private EmployeeDAO() {}

	public static void addEmployee(EmployeeRegistrationInfo employee) {
		if(UserDAO.userExists(employee.getLogin(), "employee"))
			return;

		int employeeID = 1; 
		if( UserDAO.executeQueryINT("SELECT COUNT(*) FROM mydb.employee") > 0 ) {
			try {
				ResultSet resultSet = UserDAO.executeQuery("SELECT EmployeeID FROM mydb.employee ORDER BY EmployeeID DESC LIMIT 1;");
				resultSet.next();
				employeeID = resultSet.getInt(1) + 1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
		UserDAO.executeUpdate("INSERT INTO mydb.employee VALUES (" + employeeID +", '" + employee.getFullName() + "', '" + employee.getGender() + "', "
				+ "'" + employee.getDateOfBirth() +"', '" + employee.getIdentificationType() + "', '" + employee.getIdentificationNumber() + "', "
				+ "'" + employee.getCitizenship() + "', '" + employee.getVisa() + "', '" + employee.getAddress() + "', '" + employee.getBankCardNumber() + "', "
				+ "'" + employee.getEmailAddress() + "', '" + employee.getHomePhoneNumber() + "', '" + employee.getMobilePhoneNumber() + "', '" + employee.getLogin() + "', "
				+ "'" + employee.getPassword() + "')");
		UserDAO.executeUpdate("INSERT INTO mydb.schedule VALUES (" + employeeID + ", " + employee.getHotelID() + ", '" + employee.getPosition() + "', "
				+ "'" + employee.getStatus() + "', '" + employee.getPayRate() + "', '" + employee.getStartDate() + "', " + employee.getEndDate() + ", '" + employee.getStartTime() + "', '" + employee.getEndTime() + "')");
	}
	
	public static String getEmployeeInfo(String auth) {
		Gson gson = new Gson();
		EmployeeInfo employeeInfo = new EmployeeInfo();
		String json = "";
		
		String username = UserDAO.getDecodedAuth(auth)[0];
		
		try {
			ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.employee WHERE Login= BINARY '" + username + "'" );
			resultSet.next();
			employeeInfo.setEmployeeID( resultSet.getInt(1) );
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
			
			resultSet = UserDAO.executeQuery("SELECT * FROM mydb.schedule WHERE EmployeeID= BINARY " + employeeInfo.getEmployeeID());
			resultSet.next();
			employeeInfo.setHotelID( resultSet.getInt(2) );
			employeeInfo.setPosition( resultSet.getString(3) );
			employeeInfo.setStatus( resultSet.getString(4) );
			employeeInfo.setPayrate( resultSet.getString(5) );
			employeeInfo.setStartDate( resultSet.getDate(6).toString() );
			if(resultSet.getDate(7) == null)
				employeeInfo.setEndDate("NULL");
			else
				employeeInfo.setEndDate( resultSet.getDate(7).toString() );
			employeeInfo.setStartTime( resultSet.getString(8) );
			employeeInfo.setEndTime( resultSet.getString(9) );
			
			resultSet = UserDAO.executeQuery("SELECT Name FROM mydb.hotel WHERE HotelID= BINARY " + employeeInfo.getHotelID() );
			resultSet.next();
			employeeInfo.setHotelName( resultSet.getString(1) );
			
			json = gson.toJson(employeeInfo, EmployeeInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static int getHotelID(String auth) {
		String username = UserDAO.getDecodedAuth(auth)[0];
		try {
			ResultSet result = UserDAO.executeQuery("Select mydb.schedule.hotelID From mydb.schedule, mydb.employee Where Login= BINARY '" + username + "' and mydb.schedule.employeeid = mydb.employee.EmployeeID");
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void editEmployeeSchedule(String auth, int employeeID, String startTime, String endTime) {
		UserDAO.executeUpdate("Update mydb.schedule " + 
							"Set starttime='" + startTime + "', endtime='" + endTime + "' " +
							"Where employeeID='" + employeeID + "' and hotelid='" + getHotelID(auth) + "'");
	}
	
}


