package com.bmmzz;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
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
	public String registration(@FormParam("username") String username, 
							  @FormParam("password") String password) {
		if(UserDAO.userExists(username)) {return "UserAlreadyExists";}
		if(username.isEmpty() || password.isEmpty()) {return "invalidInput";}
		
		UserDAO.addGuest("", "", "",
				"", "", "", "",
				username, password);
		return "UserWasCreated";
	}
}
