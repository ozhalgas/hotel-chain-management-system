package com.bmmzz.userDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.bmmzz.userDAO.struct.AvailableRoomsInfo;
import com.bmmzz.userDAO.struct.GuestInfo;
import com.bmmzz.userDAO.struct.BillInfo;
import com.google.gson.Gson;

public class RoomDAO {
	public static void reserveRoomType(String roomTypeName, int hotelID, int guestID, String checkInDate, String checkOutDate, int numberOfRooms) {
		ResultSet resultSet = UserDAO.executeQuery("SELECT NumberOfRooms FROM mydb.reserves WHERE "
				+ "RoomTypeName = BINARY '" + roomTypeName + "' AND HotelID = " + hotelID + " AND GuestID = " + guestID + " "
				+ "AND CheckInDate = '" + checkInDate + "' AND CheckOutDate = '" + checkOutDate + "';");
		try {
			if(resultSet.next()) {
				numberOfRooms += resultSet.getInt(1);
				UserDAO.executeUpdate("UPDATE mydb.reserves SET NumberOfRooms=" + numberOfRooms + " WHERE "
						+ "RoomTypeName = BINARY '" + roomTypeName + "' AND HotelID = " + hotelID + " AND GuestID = " + guestID + " "
						+ "AND CheckInDate = '" + checkInDate + "' AND CheckOutDate = '" + checkOutDate + "';");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		UserDAO.executeUpdate("INSERT INTO mydb.reserves VALUES "
				+ "('" + roomTypeName + "', " + hotelID + ", " + guestID + ", "
				+ "'" + checkInDate + "', '" + checkOutDate + "', " + numberOfRooms + ");");
	}
	
	public static String getAvailableRoomsInfo(int hotelID, String startDateStr, String endDateStr) {
		Gson gson = new Gson();
		String json = "";
		
		try {
			Date startDate = new SimpleDateFormat("yyyy:MM:dd").parse(startDateStr);
			Date endDate = new SimpleDateFormat("yyyy:MM:dd").parse(endDateStr);
			
			AvailableRoomsInfo availableRoomsInfo = new AvailableRoomsInfo();
			
			ResultSet resultSet = UserDAO.executeQuery("Select TypeName, Size, Capacity FROM mydb.room_type WHERE HotelID = " + hotelID + ";");
			while(resultSet.next()) {
				int availableRoomNum = getNumberOfAvailableRooms( hotelID, startDate, endDate, resultSet.getString(1) );
				if(availableRoomNum <= 0) {continue;}
				
				double initialPrice = getInitialPrice(hotelID, startDate, endDate, resultSet.getString(1) );
				
				availableRoomsInfo.add(resultSet.getString(1), resultSet.getDouble(2), resultSet.getInt(3), initialPrice, availableRoomNum);
			}
			
			json = gson.toJson(availableRoomsInfo, AvailableRoomsInfo.class);
		} catch (ParseException | SQLException e1) {
			e1.printStackTrace();
		}
		
		return json;
	}
	
	private static double getInitialPrice(int hotelID, Date startDate, Date endDate, String typeName) {
		Double totalPrice = 0.0;
		try {
			Date date = new Date(startDate.getTime());
			
			// Acquiring price for each day separately and summing them
			while( date.compareTo(endDate) <= 0 ) {
				String seasonName = "";
				char dayOfTheWeek = getDayOfTheWeek(date);
				
				ResultSet resultSet = UserDAO.executeQuery("SELECT * FROM mydb.time_period WHERE "
						+ "DayOfTheWeek= '" + dayOfTheWeek + "';");
				
				// Finding the corresponding season of the specific day (startDay)
				while(resultSet.next()) {
					Date seasonStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString(3));
					Date seasonEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString(4));
					if(seasonStartDate.compareTo(date) <= 0 && seasonEndDate.compareTo(date) >= 0) {
						seasonName = resultSet.getString(2);
						break;
					}
				}
				
				resultSet = UserDAO.executeQuery("SELECT Amount FROM mydb.initial_price WHERE "
						+ "RoomTypeName = BINARY '" + typeName + "' AND HotelID = " + hotelID + " AND "
						+ "DayOfTheWeek = BINARY '" + dayOfTheWeek + "' AND SeasonName = BINARY '" + seasonName +"';");
				
				resultSet.next();
				totalPrice += resultSet.getDouble(1);
				date = new Date(date.getTime() + 86400000);
				
			}
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		return totalPrice;
	}
	
	private static char getDayOfTheWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
		switch(dayOfTheWeek) {
			case 1:
				return 'H';
			case 2:
				return 'M';
			case 3:
				return 'T';
			case 4:
				return 'W';
			case 5:
				return 'R';
			case 6:
				return 'F';
			case 7:
				return 'S';
			default:
				return 'E';
		}
	}
	
	private static int getNumberOfAvailableRooms(int hotelID, Date startDate, Date endDate, String typeName) {
		try {
			// Getting the number of rooms in a given room type at given hotel.
			ResultSet resultSet = UserDAO.executeQuery("SELECT NumberOfRooms "
					+ "FROM  mydb.room_type WHERE TypeName = BINARY '" + typeName + "' AND HotelID = "+ hotelID + ";");
			resultSet.next();
			int maxNumOfRooms = resultSet.getInt(1);
			
			// Creating an array to keep track of the number of available rooms in a specific day.
			int numOfDays = getDifferenceInDays(startDate, endDate);
			int availableRoomsNumOnDay[] = new int[numOfDays];
			for(int i = 0; i < numOfDays; i++) {
				availableRoomsNumOnDay[i] =  maxNumOfRooms;
			}
			
			// Counting number of available rooms for each day in the given time interval.
			resultSet = UserDAO.executeQuery("SELECT CheckInDate, CheckOutDate, NumberOfRooms "
				+ "FROM mydb.reserves WHERE RoomTypeName = BINARY '" + typeName + "' AND HotelID = " + hotelID + ";");
			while(resultSet.next()) {		
				Date reservedStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString(1));
				Date reservedEndDate = new SimpleDateFormat("yyyy-MM-dd").parse( resultSet.getString(2));
				int numOfReservedRooms = resultSet.getInt(3);
				
				if(startDate.after(reservedEndDate) || endDate.before(reservedStartDate)) {continue;}
				
				Date fromDate = reservedStartDate.after(startDate) ? reservedStartDate : startDate;
				Date toDate = reservedEndDate.before(endDate) ? reservedEndDate : endDate;
				
				for(int i = getDifferenceInDays(startDate, fromDate)-1 ; i < getDifferenceInDays(startDate, toDate); i++ ) {
					availableRoomsNumOnDay[i] -= numOfReservedRooms;
					if(availableRoomsNumOnDay[i] <= 0) {return 0;}
				}
			}
			
			// Counting maximum number of rooms that can be reserved in the given time interval.
			int min = maxNumOfRooms;
			for(int i = 0; i < numOfDays; i++) {
				if(availableRoomsNumOnDay[i] < min)
					min = availableRoomsNumOnDay[i];
			}
			return min;
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// get difference between to dates (inclusive)
	private static int getDifferenceInDays(Date startDate, Date endDate) {
		return Math.round((endDate.getTime() - startDate.getTime()) / 86400000) + 1;
	}
	
	public static void bookEditing(String roomTypeName1, int hotelID, int guestID1, String checkInDate1, String checkOutDate1, int numberOfRooms1,
								   String roomTypeName2, int guestID2, String checkInDate2, String checkOutDate2, int numberOfRooms2) {
		UserDAO.executeUpdate("Update mydb.reserves " + 
			"Set roomtypename='" + roomTypeName2 + "', GuestID='" + guestID2 + "', checkindate='" + checkInDate2 + "', checkoutdate='" + checkOutDate2 + "', numberofrooms='" + numberOfRooms2 + "'" +
			"Where roomtypename= BINARY '" + roomTypeName1 + "' and hotelID='" + hotelID + "' and GuestID='" + guestID1 + "' and checkindate='" + checkInDate1 + "' and checkoutdate='" + checkOutDate1 + "' and numberofrooms='" + numberOfRooms1 + "'");
	}
	
	public static void checkInRoom(String checkInDate, String checkOutDate, String roomNumber, int roomFloor, int guestID, String roomTypeName, int hotelID) {
		UserDAO.executeUpdate("INSERT INTO mydb.single_stay VALUES "
                + "('" + checkInDate + "', '" + checkOutDate + "', " + "null" + ", '" 
                + roomNumber + "', '" + roomFloor + "', '" + guestID +  "', '" + roomTypeName + "', '" +  hotelID +"');");
	}
	
	public static void occupy(String roomNumber, int roomFloor, int occupID, String checkInDate, String checkOutDate, int hotelID, String roomTypeName) {
		UserDAO.executeUpdate("INSERT INTO mydb.occupies VALUES "
                + "('" + roomNumber + "', '" + roomFloor + "', '" + occupID + "', '" + checkInDate + "', '" + checkOutDate + "', '" +  hotelID +  "', '" + roomTypeName +"');");
	}
	
	public static void checkOut(String auth, int guestID, String roomType, String roomNumber, int floor, String checkInDate) {
		String checkOutDate = java.time.LocalDate.now().toString();
		double finalBill = 0;
		try {
			Date outDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkOutDate);
			Date inDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkInDate);
			finalBill = getInitialPrice(EmployeeDAO.getHotelID(auth), inDate, outDate, roomType);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			ResultSet result = UserDAO.executeQuery("Select numberofrooms, checkoutdate From mydb.reserves Where roomtypename='" + roomType + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "' and checkindate='" + checkInDate + "'");
			result.next();
			if ( !checkOutDate.equals(result.getString(2)) ) {
				if (result.getInt(1) > 1) {
					UserDAO.executeUpdate("Update mydb.reserves " + 
							"Set numberofrooms = '" + (result.getInt(1) - 1) + "' " + 
							"Where roomtypename='" + roomType + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "' and checkindate='" + checkInDate + "'");
					reserveRoomType(roomType, EmployeeDAO.getHotelID(auth), guestID, checkInDate, checkOutDate, 1);				
				} else {
					UserDAO.executeUpdate("Update mydb.reserves " + 
							"Set checkoutdate = '" + checkOutDate + "' " + 
							"Where roomtypename='" + roomType + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "' and checkindate='" + checkInDate + "'");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		UserDAO.executeUpdate("Update mydb.single_stay " + 
			"Set checkoutdate = '"  + checkOutDate + "', finalbill = '" + finalBill + "' " + 
			"Where checkindate='" + checkInDate + "' and roomNumber='" + roomNumber + "' and roomfloor='" + floor + "' and guestid='" + guestID + "' and hotelID='" + EmployeeDAO.getHotelID(auth) + "'");
		UserDAO.executeUpdate("Update mydb.occupies " + 
			"Set checkoutdate = '" + checkOutDate + "' " +
			"Where roomNumber='" + roomNumber + "' and floor='" + floor + "' and checkindate='" + checkInDate + "' and hotelID='" + EmployeeDAO.getHotelID(auth) + "'");
		UserDAO.executeUpdate("Update mydb.room " + 
				"Set occupied = '0', numberofoccupants = '0' " +
				"Where roomNumber='" + roomNumber + "' and floor='" + floor + "'");	
	}
	
	public static String finalBill(String auth, int guestID, String roomType, String roomNumber, int floor, String checkInDate) {
		String checkOutDate = java.time.LocalDate.now().toString();
		Gson gson = new Gson();
		BillInfo bill = new BillInfo();
		String json = "";
		try {
			ResultSet result = UserDAO.executeQuery("Select * From mydb.single_stay, mydb.hotel, mydb.guest Where mydb.hotel.hotelid='" + EmployeeDAO.getHotelID(auth) + "' and mydb.single_stay.checkindate='" + checkInDate + "' and mydb.single_stay.roomnumber='" + roomNumber + "' and mydb.single_stay.roomfloor='" + floor + "' and mydb.single_stay.guestid='" + guestID + "' and mydb.guest.guestID='" + guestID + "'");
			if (result.next()) {
				bill.add(result.getString(1), result.getString(2), result.getDouble(3), result.getString(4), result.getInt(5), result.getInt(6), result.getString(15), result.getString(16), result.getString(17), result.getInt(9), result.getString(10), result.getString(11), result.getString(12), result.getString(13));
				json = gson.toJson(bill, BillInfo.class);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static boolean checkOutEdit(String auth, int guestID, String roomType, String roomNumber, int floor, String checkInDate, String oldCheckOutDate, String newCheckOutDate) {
		try {
			Date outDate = new SimpleDateFormat("yyyy-MM-dd").parse(newCheckOutDate);
			Date inDate = new SimpleDateFormat("yyyy-MM-dd").parse(oldCheckOutDate);
			inDate = new Date(inDate.getTime() + 86400000);
			if (getNumberOfAvailableRooms(EmployeeDAO.getHotelID(auth), inDate, outDate, roomType) > 0) {
				ResultSet resultSet = UserDAO.executeQuery("Select NumberOfRooms from mydb.reserves " + 
						"Where roomtypename='" + roomType + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "' and checkindate='" + checkInDate + "'");
				resultSet.next();
				
				if(resultSet.getInt(1) > 1) {
					UserDAO.executeUpdate("Update mydb.reserves " + 
							"Set NumberOfRooms = " + (resultSet.getInt(1) - 1 ) +
							" Where roomtypename='" + roomType + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "' and checkindate='" + checkInDate + "'");
					reserveRoomType(roomType, EmployeeDAO.getHotelID(auth), guestID, checkInDate, newCheckOutDate, 1);
				} else {
					UserDAO.executeUpdate("Update mydb.reserves " + 
							"Set checkoutdate = '" + newCheckOutDate + "' " + 
							"Where roomtypename='" + roomType + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "' and checkindate='" + checkInDate + "'");
				}
				
				UserDAO.executeUpdate("Update mydb.single_stay " + 
						"Set checkoutdate = '"  + newCheckOutDate + "' " +
						"Where checkindate='" + checkInDate + "' and roomNumber='" + roomNumber + "' and roomfloor='" + floor + "' and guestid='" + guestID + "' and hotelID='" + EmployeeDAO.getHotelID(auth) + "'");
				UserDAO.executeUpdate("Update mydb.occupies " + 
						"Set checkoutdate = '" + newCheckOutDate + "' " +
						"Where roomNumber='" + roomNumber + "' and floor='" + floor + "' and checkindate='" + checkInDate + "' and hotelID='" + EmployeeDAO.getHotelID(auth) + "'");
				return true;
			}
		} catch (ParseException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
