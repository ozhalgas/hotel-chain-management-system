package com.bmmzz;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/registration")
public class RegistrationService {
	
	@Context ServletContext servletContext;
	
	public RegistrationService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get() {	
		return Helper.getPage(servletContext, "registrationPage.html");
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String registration(GuestRegistrationInfo guestRegistrationInfo) {
		if(UserDAO.userExists(guestRegistrationInfo.getLogin())) 
			{return "UserAlreadyExists";}
		if(guestRegistrationInfo.getLogin().isEmpty() || guestRegistrationInfo.getPassword().isEmpty()) 
			{return "invalidInput";}
		
		UserDAO.addGuest(guestRegistrationInfo);
		return "UserWasCreated";
	}
}
