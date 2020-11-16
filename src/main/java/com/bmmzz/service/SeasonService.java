package com.bmmzz.service;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import com.bmmzz.userDAO.HotelDAO;
import com.bmmzz.userDAO.SeasonDAO;
import com.bmmzz.userDAO.UserDAO;

@Path("/season")
public class SeasonService {

	@Context ServletContext servletContext;
	
	public SeasonService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Path("/all")
	public Response getSeasons( @DefaultValue("") @QueryParam("auth") String auth ) {
		if (!UserDAO.getRole(auth).equals("manager")) {
			return null;
		}
		String json = SeasonDAO.getAllSeasons(auth);
		return Response.ok(json).build();
	}
}
