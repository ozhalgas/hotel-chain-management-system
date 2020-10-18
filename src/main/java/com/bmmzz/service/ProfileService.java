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
import javax.ws.rs.core.Response;

import com.bmmzz.userDAO.UserDAO;

@Path("/profile")
public class ProfileService {
	
	@Context ServletContext servletContext;
	
	public ProfileService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get( @DefaultValue("") @QueryParam("auth") String auth ) {	
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "admin":
				return Helper.getPage(servletContext, "adminPage.html");
			case "guest":
				return Helper.getPage(servletContext, "guestPage.html");
			case "manager":
				return Helper.getPage(servletContext, "managerPage.html");
			case "desk-clerk":
				return Helper.getPage(servletContext, "deskClerkPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@GET
	@Path("/info")
	public Response getProfileInfo(@DefaultValue("") @QueryParam("auth") String auth) {
		if(UserDAO.getRole(auth).equals("guest")) {
			String json = UserDAO.getGuestInfo(auth);
			return Response.ok(json).build();
		}
		if(UserDAO.getRole(auth).equals("desk-clerk") || UserDAO.getRole(auth).equals("manager")) {
			String json = UserDAO.getEmployeeInfo(auth);
			return Response.ok(json).build();
		}
		System.out.println("What?");
		return null;
	}
}
