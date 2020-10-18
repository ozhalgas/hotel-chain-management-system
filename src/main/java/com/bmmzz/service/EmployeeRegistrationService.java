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

@Path("/employee-registration")
public class EmployeeRegistrationService {
	
	@Context ServletContext servletContext;
	
	public EmployeeRegistrationService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "admin":
			case "manager":
				return Helper.getPage(servletContext, "employeeRegistrationPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
}
