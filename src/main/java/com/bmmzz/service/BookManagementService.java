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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.bmmzz.userDAO.GuestDAO;
import com.bmmzz.userDAO.HotelDAO;
import com.bmmzz.userDAO.UserDAO;

@Path("/book-management")
public class BookManagementService {
	
	@Context ServletContext servletContext;
	
	public BookManagementService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "guest":
				return Helper.getPage(servletContext, "bookManagement.html");
			case "desk-clerk":
				return Helper.getPage(servletContext, "bookManagement.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@GET
	@Path("/hotel-choosing-info")
	public Response destinationInfo( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth) || !UserDAO.getRole(auth).equals("guest"))
			return null;
		String json = HotelDAO.getHotelChoosingInfo();
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
}
