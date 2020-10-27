package com.bmmzz.service;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmmzz.userDAO.HotelDAO;
import com.bmmzz.userDAO.RoomDAO;
import com.bmmzz.userDAO.UserDAO;

@Path("/room-booking")
public class RoomBookingService {
	
	@Context ServletContext servletContext;
	
	public RoomBookingService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream hotelChoosing( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "guest":
				return Helper.getPage(servletContext, "hotelChoosingPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@GET
	@Path("hotel-choosing-info")
	public Response destinationInfo( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth) || !UserDAO.getRole(auth).equals("guest"))
			return null;
		String json = HotelDAO.getHotelChoosingInfo();
		return Response.ok(json).build();
	}
	
	@GET
	@Path("{hotelID}-{startDate}-{endDate}")
	public InputStream roomBooking( @DefaultValue("") @QueryParam("auth") String auth,
								  @PathParam("hotelID") int hotelID,
								  @PathParam("startDate") String startDate,
								  @PathParam("endDate") String endDate) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "guest":
				return Helper.getPage(servletContext, "roomBookingPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@GET
	@Path("{hotelID}-{startDate}-{endDate}/hotel-information")
	public Response hotelInformation( @DefaultValue("") @QueryParam("auth") String auth,
								  	@PathParam("hotelID") int hotelID,
								  	@PathParam("startDate") String startDate,
								  	@PathParam("endDate") String endDate) {
		if(!UserDAO.checkAuth(auth) || !UserDAO.getRole(auth).equals("guest"))
			return null;
		String json = HotelDAO.getHotelInfo(hotelID);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("{hotelID}-{startDate}-{endDate}/available-rooms")
	public Response availableRooms( @DefaultValue("") @QueryParam("auth") String auth,
								  	@PathParam("hotelID") int hotelID,
								  	@PathParam("startDate") String startDate,
								  	@PathParam("endDate") String endDate) {
		if(!UserDAO.checkAuth(auth) || !UserDAO.getRole(auth).equals("guest"))
			return null;
		
		String json = RoomDAO.getAvailableRoomsInfo(hotelID, startDate, endDate);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("{hotelID}-{startDate}-{endDate}/to-book")
	public Response toBook( @DefaultValue("") @QueryParam("auth") String auth,
									@PathParam("hotelID") int hotelID,
									@PathParam("startDate") String startDate,
									@PathParam("endDate") String endDate,
									@FormParam("roomTypeName") String typeName,
									@FormParam("numberOfRooms") int numberOfRooms) {
		startDate = startDate.replace(':', '-');
		endDate = endDate.replace(':', '-');
		RoomDAO.reserveRoomType(typeName, hotelID, auth, startDate, endDate, numberOfRooms);
		return Response.ok().build();
	}
}
