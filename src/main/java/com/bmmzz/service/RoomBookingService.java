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

@Path("/room-booking")
public class RoomBookingService {
	
	@Context ServletContext servletContext;
	
	public RoomBookingService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "guest":
				return Helper.getPage(servletContext, "roomBookingPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
}
