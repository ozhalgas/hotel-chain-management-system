package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bmmzz.userDAO.struct.EmployeeInfo;
import com.bmmzz.userDAO.struct.EmployeeRegistrationInfo;
import com.bmmzz.userDAO.struct.EmployeeSchedulesInfo;
import com.bmmzz.userDAO.struct.HotelRoomsInfo;
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
							"Where employeeID='" + employeeID + "' and hotelid='" + getHotelID(auth) + "' and position<>'Manager'");
	}
	
	public static String getSchedules(String auth) {
		Gson gson = new Gson();
		EmployeeSchedulesInfo es = new EmployeeSchedulesInfo();
		String json = "";
		int hID = EmployeeDAO.getHotelID(auth);
		
		//get es for this hotel
		ResultSet rES = UserDAO.executeQuery("Select * From mydb.employee E, mydb.schedule S " +
											"Where S.HotelID='" + hID + "' and " +
											"E.EmployeeID=S.EmployeeID and S.Position<>'Manager';");
		//System.out.println("Select * From mydb.employee E, mydb.schedule S " +
		//		"Where S.HotelID='" + hID + "' and " +
		//		"E.EmployeeID=S.EmployeeID and S.Position<>'Manager';");
		try {
			while(rES.next()) {
				Date startTime = new Date();
				Date endTime = new Date();
				try {
					startTime = new SimpleDateFormat("HH:mm:ss").parse(rES.getString(23));
					endTime = new SimpleDateFormat("HH:mm:ss").parse(rES.getString(24));				
				} catch (ParseException e) {
					e.printStackTrace();
				} 
				long minLong = (endTime.getTime() - startTime.getTime()) / (60 * 1000);
				double hoursDb = (double) minLong/60;
				double hours = Math.round(hoursDb * 100.0) / 100.0;
				
				//checking for right hours
				int startH = Integer.parseInt(rES.getString(23).substring(0, 2));
				int endH = Integer.parseInt(rES.getString(24).substring(0, 2));
				//System.out.println(startH + " " + endH);
				if(endH < startH) hours += 24;
				//System.out.println(hours);
				
				double hourlyWage = Double.parseDouble(rES.getString(20).substring(1, rES.getString(20).length()));
				double dailySalDb = hours * hourlyWage;

				//get numWorkingDays/Week
				ResultSet numDays = UserDAO.executeQuery("SELECT count(*) " +
													   "FROM mydb.day_of_the_week D " +
													   "Where D.EmployeeID='" + rES.getInt(1)+ "' and D.HotelID='" + rES.getInt(17) + "';");
				
				//System.out.println("SELECT count(*) " +
				//		   "FROM mydb.day_of_the_week D " +
				//		   "Where D.EmployeeID='" + rES.getInt(1)+ "' and D.HotelID='" + rES.getInt(17) + "';");
				
				double weeklySalDb = 0;
				if(numDays.next()) {
					weeklySalDb = dailySalDb * numDays.getInt(1);
					System.out.println("Daily: " + dailySalDb + " Weekly: " + weeklySalDb);
				}
				String cur = String.valueOf(rES.getString(20).charAt(0));
				String dailySal = cur + String.valueOf(dailySalDb);
				String weeklySal = cur + String.valueOf(weeklySalDb);
				
				//System.out.println("Daily: " + dailySal + " Weekly: " + weeklySal);
				
				es.addES(rES.getInt(1), rES.getString(2), rES.getString(11), rES.getString(13), rES.getInt(17), rES.getString(18), rES.getString(20), rES.getString(21), rES.getString(23), rES.getString(24), dailySal, weeklySal);
			}
			json = gson.toJson(es, EmployeeSchedulesInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return json;
	}
	
}


