package com.bmmzz.service;

import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmmzz.userDAO.EmployeeDAO;
import com.bmmzz.userDAO.GuestDAO;
import com.bmmzz.userDAO.HotelDAO;
import com.bmmzz.userDAO.RoomDAO;
import com.bmmzz.userDAO.UserDAO;

@Path("/book-management")
public class BookManagementService {
	
	@Context ServletContext servletContext;
	
	public BookManagementService() {}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "guest":
				return Helper.getPage(servletContext, "bookManagementForGuest.html");
			case "desk-clerk":
				return Helper.getPage(servletContext, "bookManagementForDeskClerk.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@GET
	@Path("/rooms")
	@Produces({MediaType.TEXT_HTML})
	public InputStream get_rooms( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "desk-clerk":
				return Helper.getPage(servletContext, "roomInfoPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@GET
	@Path("/hotel-choosing-info")
	public Response destinationInfo( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkRoleAndAuth(auth, "guest", "desk-clerk", "admin"))
			return null;
		String json = HotelDAO.getHotelChoosingInfo(auth);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/hotel-info")
	public Response destinationInfo2( @DefaultValue("") @QueryParam("auth") String auth) {
		if(!UserDAO.checkRoleAndAuth(auth, "guest", "desk-clerk", "admin"))
			return null;
		int hotelID = EmployeeDAO.getHotelID(auth);
		String json = HotelDAO.getHotelInfo(hotelID);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/bookings")
	public Response getPastBookings( @DefaultValue("") @QueryParam("auth") String auth) {
		if(UserDAO.getRole(auth).equals("guest")) {
			String json = GuestDAO.getGuestBookings(auth);
			return Response.ok(json).build();
		}
		if (UserDAO.getRole(auth).equals("desk-clerk")) {
			String json = HotelDAO.getHotelBookings(auth);
			return Response.ok(json).build();
		}
		return null;
	}
	
	@DELETE
	@Path("/{hotelID}-{startDate}-{endDate}-{roomTypeName}")
	public Response removeBooking(@DefaultValue("") @QueryParam("auth") String auth,
			@PathParam("hotelID") int hotelID,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate,
			@PathParam("roomTypeName") String typeName){
	   GuestDAO.removeBooking(auth, hotelID, startDate, endDate, typeName);
	   return Response.ok().build();
	}

	@DELETE
	@Path("/{guestID}-{hotelID}-{startDate}-{endDate}-{roomTypeName}")
	public Response removeBooking(@DefaultValue("") @QueryParam("auth") String auth,
			@PathParam("guestID") int guestID,
			@PathParam("hotelID") int hotelID,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate,
			@PathParam("roomTypeName") String typeName){
	   GuestDAO.removeBooking(guestID, hotelID, startDate, endDate, typeName);
	   return Response.ok().build();
	}
	
	@GET
	@Path("/{roomTypeName}-{hotelID}-{guestID}-{checkInDate}-{checkOutDate}-{numOfRooms}")
	public InputStream bookEditing( @DefaultValue("") @QueryParam("auth") String auth,
								  @PathParam("roomTypeName") String roomTypeName,
								  @PathParam("hotelID") int hotelID,
								  @PathParam("guestID") int guestID,
								  @PathParam("checkInDate") String checkInDate,
								  @PathParam("checkOutDate") String checkOutDate,
								  @PathParam("numOfRooms") int numOfRooms) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "desk-clerk":
				return Helper.getPage(servletContext, "hotelChoosingPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@GET
	@Path("/{roomTypeName}-{hotelID}-{guestID}-{checkInDate}-{checkOutDate}-{numOfRooms}/{hotelID}-{checkInDate2}-{checkOutDate2}")
	public InputStream roomTypeEditing( @DefaultValue("") @QueryParam("auth") String auth,
								  @PathParam("roomTypeName") String roomTypeName,
								  @PathParam("hotelID") int hotelID,
								  @PathParam("guestID") int guestID,
								  @PathParam("checkInDate") String checkInDate,
								  @PathParam("checkOutDate") String checkOutDate,
								  @PathParam("numOfRooms") int numOfRooms,
								  @PathParam("checkInDate2") String checkInDate2,
								  @PathParam("checkOutDate2") String checkOutDate2) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "desk-clerk":
				return Helper.getPage(servletContext, "bookEditingPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@POST
	@Path("/{roomTypeName1}-{hotelID}-{guestID1}-{checkInDate1}-{checkOutDate1}-{numOfRooms1}-{roomTypeName2}-{guestID2}-{checkInDate2}-{checkOutDate2}-{numOfRooms2}")
	public Response bookEditing(@DefaultValue("") @QueryParam("auth") String auth,
			  					@PathParam("roomTypeName1") String roomTypeName1,
								@PathParam("hotelID") int hotelID,
								@PathParam("guestID1") int guestID1,
								@PathParam("checkInDate1") String checkInDate1,
								@PathParam("checkOutDate1") String checkOutDate1,
								@PathParam("numOfRooms1") int numOfRooms1,
								@PathParam("roomTypeName2") String roomTypeName2,
								@PathParam("guestID2") int guestID2,
								@PathParam("checkInDate2") String checkInDate2,
								@PathParam("checkOutDate2") String checkOutDate2,
								@PathParam("numOfRooms2") int numOfRooms2) {
		if (!UserDAO.checkRoleAndAuth(auth, "desk-clerk"))
			return null;
		checkInDate1 = checkInDate1.replace(':', '-');
		checkInDate2 = checkInDate2.replace(':', '-');
		checkOutDate1 = checkOutDate1.replace(':', '-');
		checkOutDate2 = checkOutDate2.replace(':', '-');
		RoomDAO.bookEditing(roomTypeName1, hotelID, guestID1, checkInDate1, checkOutDate1, numOfRooms1, roomTypeName2, guestID2, checkInDate2, checkOutDate2, numOfRooms2);
		return Response.ok().build();
	}
	
}
