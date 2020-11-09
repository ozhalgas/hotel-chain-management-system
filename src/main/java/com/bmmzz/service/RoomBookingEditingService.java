package com.bmmzz.service;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.bmmzz.userDAO.UserDAO;

@Path("/room-booking-editing")
public class RoomBookingEditingService {
	
	@Context ServletContext servletContext;
	
	public RoomBookingEditingService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream hotelChoosing( @DefaultValue("") @QueryParam("auth") String auth, 
			@DefaultValue("-1") @QueryParam("guestID") int guestID,
			@DefaultValue("-1") @QueryParam("hotelID") int hotelID,
			@DefaultValue("") @QueryParam("checkInDate") String checkInDate,
			@DefaultValue("") @QueryParam("checkOutDate") String checkOutDate,
			@DefaultValue("") @QueryParam("roomType") String roomType) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "guest":
			case "desk-clerk":
			case "admin":
				return Helper.getPage(servletContext, "roomBookingEditingPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	/*
	 * @GET
	 * 
	 * @Path("hotel-choosing-info") public Response
	 * destinationInfo( @DefaultValue("") @QueryParam("auth") String auth ) { if
	 * (!UserDAO.checkRoleAndAuth(auth, "guest", "desk-clerk", "admin")) return
	 * null; String json = ""; json = HotelDAO.getHotelChoosingInfo(auth); return
	 * Response.ok(json).build(); }
	 * 
	 * @GET
	 * 
	 * @Path("role") public Response
	 * getRoleOfUser( @DefaultValue("") @QueryParam("auth") String auth ) { return
	 * Response.ok("{\"role\" : \"" + UserDAO.getRole(auth) + "\"}").build(); }
	 * 
	 * @GET
	 * 
	 * @Path("{guestID}") public Response
	 * getGuestInfoForBooking( @DefaultValue("") @QueryParam("auth") String auth,
	 * 
	 * @PathParam("guestID") int guestID) { if(!UserDAO.checkRoleAndAuth(auth,
	 * "desk-clerk", "admin")) return null; String json =
	 * GuestDAO.getGuestInfo(guestID); return Response.ok(json).build(); }
	 * 
	 * @GET
	 * 
	 * @Path("{hotelID}-{startDate}-{endDate}") public InputStream
	 * roomBooking( @DefaultValue("") @QueryParam("auth") String auth,
	 * 
	 * @PathParam("hotelID") int hotelID,
	 * 
	 * @PathParam("startDate") String startDate,
	 * 
	 * @PathParam("endDate") String endDate) { if(!UserDAO.checkAuth(auth)) return
	 * Helper.getPage(servletContext, "accessDeniedPage.html"); switch(
	 * UserDAO.getRole(auth) ) { case "guest": case "desk-clerk": case "admin":
	 * return Helper.getPage(servletContext, "roomBookingPage.html"); default:
	 * return Helper.getPage(servletContext, "accessDeniedPage.html"); } }
	 * 
	 * @GET
	 * 
	 * @Path("{hotelID}-{startDate}-{endDate}/hotel-information") public Response
	 * hotelInformation( @DefaultValue("") @QueryParam("auth") String auth,
	 * 
	 * @PathParam("hotelID") int hotelID,
	 * 
	 * @PathParam("startDate") String startDate,
	 * 
	 * @PathParam("endDate") String endDate) { if ( !UserDAO.checkRoleAndAuth(auth,
	 * "guest", "desk-clerk", "admin") ) return null; String json =
	 * HotelDAO.getHotelInfo(hotelID); return Response.ok(json).build(); }
	 * 
	 * @GET
	 * 
	 * @Path("{hotelID}-{startDate}-{endDate}/available-rooms") public Response
	 * availableRooms( @DefaultValue("") @QueryParam("auth") String auth,
	 * 
	 * @PathParam("hotelID") int hotelID,
	 * 
	 * @PathParam("startDate") String startDate,
	 * 
	 * @PathParam("endDate") String endDate) { if (!UserDAO.checkRoleAndAuth(auth,
	 * "guest", "desk-clerk", "admin")) return null; String json =
	 * RoomDAO.getAvailableRoomsInfo(hotelID, startDate, endDate); return
	 * Response.ok(json).build(); }
	 * 
	 * @POST
	 * 
	 * @Path("{hotelID}-{startDate}-{endDate}/to-book") public Response
	 * toBook( @DefaultValue("") @QueryParam("auth") String auth,
	 * 
	 * @PathParam("hotelID") int hotelID,
	 * 
	 * @PathParam("startDate") String startDate,
	 * 
	 * @PathParam("endDate") String endDate,
	 * 
	 * @FormParam("roomTypeName") String typeName,
	 * 
	 * @FormParam("numberOfRooms") int numberOfRooms,
	 * 
	 * @DefaultValue("-1") @FormParam("guestID") int guestID) { if(numberOfRooms >=
	 * 1) { startDate = startDate.replace(':', '-'); endDate = endDate.replace(':',
	 * '-'); if(UserDAO.getRole(auth).equals("guest")) guestID =
	 * UserDAO.getGuestID(auth); RoomDAO.reserveRoomType(typeName, hotelID, guestID,
	 * startDate, endDate, numberOfRooms); } return Response.ok().build(); }
	 */
}
