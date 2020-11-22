package com.bmmzz.service;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmmzz.userDAO.EmployeeDAO;
import com.bmmzz.userDAO.HotelDAO;
import com.bmmzz.userDAO.UserDAO;
import com.bmmzz.userDAO.struct.EmployeeRegistrationInfo;
import com.google.gson.Gson;

@Path("/employee-registration")
public class EmployeeRegistrationService {
	
	@Context ServletContext servletContext;
	
	public EmployeeRegistrationService() {}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkAuth(auth))
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		switch( UserDAO.getRole(auth) ) {
			case "admin":
				return Helper.getPage(servletContext, "employeeRegistrationPage.html");
			case "manager":
				return Helper.getPage(servletContext, "clerkRegistrationPage.html");
			default:
				return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
	}
	
	@GET
	@Path("/hotels")
	public Response getAllHotels() {
		String json = HotelDAO.getAllHotels();
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/user-data")
	public String employeeRegistration(@FormParam("employeeInJson") String employeeInJson) {
		Gson gson = new Gson();
		EmployeeRegistrationInfo employee = gson.fromJson(employeeInJson, EmployeeRegistrationInfo.class);
		
		if(UserDAO.userExists(employee.getLogin())) 
			{return "UserAlreadyExists";}
		if(employee.getLogin().isEmpty() || employee.getPassword().isEmpty()) 
			{return "invalidInput";}
		
		EmployeeDAO.addEmployee(employee);
		return "UserWasCreated";
	}
	
}
