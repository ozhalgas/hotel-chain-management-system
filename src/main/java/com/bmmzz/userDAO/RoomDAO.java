package com.bmmzz.userDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import com.bmmzz.service.EmployeeScheduleService;
import com.bmmzz.userDAO.struct.AvailableRoomsInfo;
import com.bmmzz.userDAO.struct.GuestInfo;
import com.bmmzz.userDAO.struct.BillInfo;
import com.bmmzz.userDAO.struct.CleaningListItem;
import com.bmmzz.userDAO.struct.CleaningScheduleInfo;
import com.google.gson.Gson;

public class RoomDAO {
	public static void reserveRoomType(String roomTypeName, int hotelID, int guestID, String checkInDate, String checkOutDate, int numberOfRooms) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("SELECT NumberOfRooms FROM mydb.reserves WHERE "
					+ "RoomTypeName = BINARY '" + roomTypeName + "' AND HotelID = " + hotelID + " AND GuestID = " + guestID + " "
					+ "AND CheckInDate = '" + checkInDate + "' AND CheckOutDate = '" + checkOutDate + "';");
			resultSet = ps.executeQuery(); 
			
			if(resultSet.next()) {
				numberOfRooms += resultSet.getInt(1);
				UserDAO.executeUpdate("UPDATE mydb.reserves SET NumberOfRooms=" + numberOfRooms + " WHERE "
						+ "RoomTypeName = BINARY '" + roomTypeName + "' AND HotelID = " + hotelID + " AND GuestID = " + guestID + " "
						+ "AND CheckInDate = '" + checkInDate + "' AND CheckOutDate = '" + checkOutDate + "';");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		
		UserDAO.executeUpdate("INSERT INTO mydb.reserves VALUES "
				+ "('" + roomTypeName + "', " + hotelID + ", " + guestID + ", "
				+ "'" + checkInDate + "', '" + checkOutDate + "', " + numberOfRooms + ");");
	}
	
	public static String getAvailableRoomsInfo(int hotelID, String startDateStr, String endDateStr) {
		Gson gson = new Gson();
		String json = "";
		
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null, ps2 = null;
		ResultSet resultSet = null, resultSetOfFeatures = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			Date startDate = new SimpleDateFormat("yyyy:MM:dd").parse(startDateStr);
			Date endDate = new SimpleDateFormat("yyyy:MM:dd").parse(endDateStr);
			
			AvailableRoomsInfo availableRoomsInfo = new AvailableRoomsInfo();
			
			ps = connection.prepareStatement("Select TypeName, Size, Capacity FROM mydb.room_type WHERE HotelID = " + hotelID + ";");
			resultSet = ps.executeQuery(); 
			while(resultSet.next()) {
				int availableRoomNum = getNumberOfAvailableRooms( hotelID, startDate, endDate, resultSet.getString(1) );
				if(availableRoomNum <= 0) {continue;}
				
				double initialPrice = getInitialPrice(hotelID, startDate, endDate, resultSet.getString(1) );
				
				ArrayList<String> features = new ArrayList<String>();
				ps2 = connection.prepareStatement("SELECT FeatureName FROM mydb.feature WHERE RoomTypeName = '" + resultSet.getString(1) + "' AND HotelID = " + hotelID + ";");
				resultSetOfFeatures = ps2.executeQuery(); 
				
				while(resultSetOfFeatures.next()) {
					features.add(resultSetOfFeatures.getString(1));
				}
				
				availableRoomsInfo.add(resultSet.getString(1), resultSet.getDouble(2), resultSet.getInt(3), initialPrice, availableRoomNum, features);
			}
			
			json = gson.toJson(availableRoomsInfo, AvailableRoomsInfo.class);
		} catch (ParseException | SQLException e1) {
			e1.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { resultSetOfFeatures.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { ps2.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		
		return json;
	}
	
	private static double getInitialPrice(int hotelID, Date startDate, Date endDate, String typeName) {
		Double totalPrice = 0.0;
		
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null, ps2 = null;
		ResultSet resultSet = null, resultSet2 = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			Date date = new Date(startDate.getTime());
			
			// Acquiring price for each day separately and summing them
			while( date.compareTo(endDate) <= 0 ) {
				String seasonName = "";
				char dayOfTheWeek = getDayOfTheWeek(date);
				
				ps = connection.prepareStatement("SELECT * FROM mydb.time_period WHERE "
						+ "DayOfTheWeek= '" + dayOfTheWeek + "' and SeasonName <> 'regular';");
				resultSet = ps.executeQuery(); 
				
				// Finding the corresponding season of the specific day (startDay)
				while(resultSet.next()) {
					Date seasonStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString(3));
					Date seasonEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString(4));
					if(seasonStartDate.compareTo(date) <= 0 && seasonEndDate.compareTo(date) >= 0) {
						seasonName = resultSet.getString(2);
						break;
					}
				}
				if(seasonName.equals("")) seasonName = "regular";
				
				ps2 = connection.prepareStatement("SELECT Amount FROM mydb.initial_price WHERE "
						+ "RoomTypeName = BINARY '" + typeName + "' AND HotelID = " + hotelID + " AND "
						+ "DayOfTheWeek = BINARY '" + dayOfTheWeek + "' AND SeasonName = BINARY '" + seasonName +"';");
				resultSet2 = ps2.executeQuery(); 
				
				resultSet2.next();
				totalPrice += resultSet2.getDouble(1);
				date = new Date(date.getTime() + 86400000);
				
			}
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { resultSet2.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { ps2.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
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
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null, ps2 = null;
		ResultSet resultSet = null, resultSet2 = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			// Getting the number of rooms in a given room type at given hotel.
			ps = connection.prepareStatement("SELECT NumberOfRooms "
					+ "FROM  mydb.room_type WHERE TypeName = BINARY '" + typeName + "' AND HotelID = "+ hotelID + ";");
			resultSet = ps.executeQuery(); 
			resultSet.next();
			int maxNumOfRooms = resultSet.getInt(1);
			
			// Creating an array to keep track of the number of available rooms in a specific day.
			int numOfDays = getDifferenceInDays(startDate, endDate);
			int availableRoomsNumOnDay[] = new int[numOfDays];
			for(int i = 0; i < numOfDays; i++) {
				availableRoomsNumOnDay[i] =  maxNumOfRooms;
			}
			
			// Counting number of available rooms for each day in the given time interval.
			ps2 = connection.prepareStatement("SELECT CheckInDate, CheckOutDate, NumberOfRooms "
					+ "FROM mydb.reserves WHERE RoomTypeName = BINARY '" + typeName + "' AND HotelID = " + hotelID + ";");
			resultSet2 = ps2.executeQuery();
			while(resultSet2.next()) {		
				Date reservedStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(resultSet2.getString(1));
				Date reservedEndDate = new SimpleDateFormat("yyyy-MM-dd").parse( resultSet2.getString(2));
				int numOfReservedRooms = resultSet2.getInt(3);
				
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
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { resultSet2.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { ps2.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
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
		
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null, ps2 = null, ps3 = null;
		ResultSet resultFeatures = null, resultDiscount = null, result = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("SELECT w.numberofusage, f.cost FROM mydb.single_stay_with_feature w, mydb.additional_feature f Where w.checkindate='" 
					+ checkInDate + "' and w.roomnumber='" + roomNumber + "' and w.floor='" + floor + "' and w.guestid='" + guestID + "' and w.featurename=f.featurename and f.hotelid='" + EmployeeDAO.getHotelID(auth) + "'");
			resultFeatures = ps.executeQuery(); 
			
			while(resultFeatures.next()) {
				finalBill = finalBill + (resultFeatures.getInt(1) * resultFeatures.getDouble(2));
			}
			ps2 = connection.prepareStatement("SELECT discount FROM mydb.guest_belongs_category b, mydb.category c Where b.CategoryName=c.TypeName and c.HotelID='" 
					+ EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "'");
			resultDiscount = ps2.executeQuery();
			resultDiscount.next();
			finalBill = finalBill * (1 - resultDiscount.getDouble(1));
			ps3 = connection.prepareStatement("Select numberofrooms, checkoutdate From mydb.reserves Where roomtypename='" + roomType + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "' and checkindate='" + checkInDate + "'");
			result = ps3.executeQuery(); 
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
		} finally {
			try { resultFeatures.close(); } catch (Exception e) {}
			try { resultDiscount.close(); } catch (Exception e) {}
			try { result.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { ps2.close(); } catch (Exception e) {}
			try { ps3.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
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
		
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null, ps2 = null;
		ResultSet result = null, resultFeatures = null;
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("Select * From mydb.single_stay, mydb.hotel, mydb.guest, mydb.guest_belongs_category, mydb.category Where mydb.hotel.hotelid='" + EmployeeDAO.getHotelID(auth) + 
					"' and mydb.single_stay.checkindate='" + checkInDate + "' and mydb.single_stay.roomnumber='" + roomNumber + "' and mydb.single_stay.roomfloor='" + floor + 
					"' and mydb.single_stay.guestid='" + guestID + "' and mydb.guest.guestID='" + guestID + "' and mydb.guest_belongs_category.guestid='" + guestID + 
					"' and mydb.guest_belongs_category.categoryname=mydb.category.typename and mydb.category.hotelid='" + EmployeeDAO.getHotelID(auth) + "'");
			result = ps.executeQuery(); 
			if (result.next()) {
				bill.add(result.getString(1), result.getString(2), result.getDouble(3), result.getString(4), result.getInt(5), 
						result.getInt(6), result.getString(15), result.getString(16), result.getString(17), result.getDouble(26) * 100, result.getString(23),
						result.getInt(9), result.getString(10), result.getString(11), result.getString(12), result.getString(13));
			}
			
			ps2 = connection.prepareStatement("SELECT w.featurename, w.numberofusage, f.cost FROM mydb.single_stay_with_feature w, mydb.additional_feature f Where w.checkindate='" 
					+ checkInDate + "' and w.roomnumber='" + roomNumber + "' and w.floor='" + floor + "' and w.guestid='" + guestID + "' and w.featurename=f.featurename and f.hotelid='" + EmployeeDAO.getHotelID(auth) + "'");
			resultFeatures = ps2.executeQuery();
			while(resultFeatures.next()) {
				bill.addFeatures(resultFeatures.getString(1), resultFeatures.getInt(2), resultFeatures.getDouble(3));
			}
			json = gson.toJson(bill, BillInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { result.close(); } catch (Exception e) {}
			try { resultFeatures.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { ps2.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		return json;
	}
	
	public static boolean checkOutEdit(String auth, int guestID, String roomType, String roomNumber, int floor, String checkInDate, String oldCheckOutDate, String newCheckOutDate) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		try {
			Date outDate = new SimpleDateFormat("yyyy-MM-dd").parse(newCheckOutDate);
			Date inDate = new SimpleDateFormat("yyyy-MM-dd").parse(oldCheckOutDate);
			inDate = new Date(inDate.getTime() + 86400000);
			if (getNumberOfAvailableRooms(EmployeeDAO.getHotelID(auth), inDate, outDate, roomType) > 0) {
				db = new Database();
				connection = db.getConnection();
				
				ps = connection.prepareStatement("Select NumberOfRooms from mydb.reserves " + 
						"Where roomtypename='" + roomType + "' and hotelid='" + EmployeeDAO.getHotelID(auth) + "' and guestid='" + guestID + "' and checkindate='" + checkInDate + "'");
				resultSet = ps.executeQuery(); 
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
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		return false;
	}

	public static void addFeature(int hotelID, int guestID, String featureName, String roomNumber) {
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null, ps2 = null;
		ResultSet rs = null, check = null;
		
		
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("SELECT * FROM mydb.single_stay WHERE GuestID='" + guestID + "' and RoomNumber='" + roomNumber + "' and HotelID='" + hotelID + "';");
			rs = ps.executeQuery(); 
			while(rs.next()) {
				String todayStr = java.time.LocalDate.now().toString();
				Date todayDate = new Date();
				Date tempCheckOutDate = new Date();
				try {
					todayDate = new SimpleDateFormat("yyyy-mm-dd").parse(todayStr);
					tempCheckOutDate = new SimpleDateFormat("yyyy-mm-dd").parse(rs.getString(2));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(todayDate.before(tempCheckOutDate) || todayDate.equals(tempCheckOutDate)) {
					ps2 = connection.prepareStatement("SELECT * FROM mydb.single_stay_with_feature "+
							   "WHERE FeatureName='"+featureName+"' and CheckInDate='"+rs.getString(1)+"' "+
							   "and RoomNumber='"+roomNumber+"' and Floor='"+rs.getInt(5)+"' and GuestID='"+guestID+"';");
					check = ps2.executeQuery(); 
					
					if(check.next()) {
						int newNum = check.getInt(6) + 1;
						UserDAO.executeUpdate("UPDATE mydb.single_stay_with_feature SET NumberOfUsage='"+newNum+"' "+
											  "WHERE FeatureName='"+featureName+"' and CheckInDate='"+rs.getString(1)+"' "+
											  "and RoomNumber='"+roomNumber+"' and Floor='"+rs.getInt(5)+"' and GuestID='"+guestID+"';");
					}else {
						UserDAO.executeUpdate("INSERT INTO mydb.single_stay_with_feature VALUES ('"+featureName+"', '"+rs.getString(1)+"', '"+roomNumber+"', '"+rs.getInt(5)+"', '"+guestID+"', '1');");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { rs.close(); } catch (Exception e) {}
			try { check.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { ps2.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
	}
	
	private static void ensureNoOutDatedItems() {
		for(int i = 0; i < EmployeeScheduleService.cleaningList.size(); i++) {
			if( EmployeeScheduleService.cleaningList.get(i).outdated() ) {
				EmployeeScheduleService.cleaningList.clear();
				EmployeeScheduleService.cleaningList = HotelDAO.initialCleaningListInsert();
				break;
			}
		}
	}
	
	public static LinkedList<CleaningListItem> changeCleanState(String auth, String roomNumber, int floor, String roomType, LinkedList<CleaningListItem> cleaningList) {	
		ensureNoOutDatedItems();
		
		int hotelID = EmployeeDAO.getHotelID(auth);
		UserDAO.executeUpdate("UPDATE mydb.room SET Cleaned=IF(Cleaned = 0, 1, 0) WHERE HotelID = " + hotelID + " AND RoomNumber = '" + roomNumber + "' AND Floor = " + floor + " AND RoomTypeName = '" + roomType + "';");
	
		Database db = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		// updating cleaning list accordingly
		try {
			db = new Database();
			connection = db.getConnection();
			
			ps = connection.prepareStatement("SELECT Cleaned FROM mydb.room WHERE RoomNumber = " + roomNumber + " AND Floor = " + floor + " AND RoomTypeName = '" + roomType + "' AND HotelID = " + hotelID + ";");
			resultSet = ps.executeQuery(); 
			resultSet.next();
			
			if(resultSet.getInt(1) == 1) {
				for(int i = 0; i < cleaningList.size(); i++) {
					CleaningListItem item = cleaningList.get(i);
					
					if(item.getRoomNumber().equals(roomNumber) &&
							item.getFloor() == floor &&
							item.getRoomType().equals(roomType) &&
							item.getHotelID() == hotelID) {
						
						CleaningScheduleInfo cleaningSchedule = new CleaningScheduleInfo();
						for(int j = 0; j < cleaningList.size(); j++) {
							if(cleaningList.get(j).getHotelID() == hotelID)
								cleaningSchedule.addCleaningListItem(EmployeeScheduleService.cleaningList.get(j));
						}
						
						int timeIndexFromSchedule = cleaningSchedule.getSlotIndexOfRoom(roomNumber, floor, roomType);
						String now = HotelDAO.adjustTimeByStep( LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) );
						int timeIndexOfNow = cleaningSchedule.startTimeToIndex(now);
						
						// remove only if the cleaning is scheduled for the future
						if(timeIndexOfNow < timeIndexFromSchedule)
							cleaningList.remove(i);
					}
				}
			} else {
				String now = HotelDAO.adjustTimeByStep( LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) );
				
				cleaningList.add(new CleaningListItem(now, roomNumber, floor, roomType, hotelID));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { ps.close(); } catch (Exception e) {}
			try { connection.close(); } catch (Exception e) {}
		}
		
		return cleaningList;
	}
}
