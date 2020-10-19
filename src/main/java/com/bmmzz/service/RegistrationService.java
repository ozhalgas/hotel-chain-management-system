package com.bmmzz.service;

import com.bmmzz.userDAO.*;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmmzz.userDAO.UserDAO;
import com.google.gson.Gson;

@Path("/registration")
public class RegistrationService {
	
	@Context ServletContext servletContext;
	
	public RegistrationService() {
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get() {	
		return Helper.getPage(servletContext, "registrationPage.html");
	}
	
	@POST
	@Path("user-data")
	public String registration(@FormParam("guestInJson") String guestInJson) {
		Gson gson = new Gson();
		GuestRegistrationInfo guest = gson.fromJson(guestInJson, GuestRegistrationInfo.class);
		
		if(UserDAO.userExists(guest.getLogin())) 
			{return "UserAlreadyExists";}
		if(guest.getLogin().isEmpty() || guest.getPassword().isEmpty()) 
			{return "invalidInput";}
		
		UserDAO.addGuest(guest);
		return "UserWasCreated";
	}
}
