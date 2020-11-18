package com.bmmzz.service;

import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmmzz.userDAO.SeasonDAO;
import com.bmmzz.userDAO.UserDAO;

@Path("/ads")
public class SeasonAdsService {
	
	@Context ServletContext servletContext;
	
	public SeasonAdsService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream get( @DefaultValue("") @QueryParam("auth") String auth ) {
        if(!UserDAO.checkAuth(auth))
            return Helper.getPage(servletContext, "accessDeniedPage.html");
        switch( UserDAO.getRole(auth) ) {
            case "manager":
                return Helper.getPage(servletContext, "BahaPishiSuda.html");
            default:
                return Helper.getPage(servletContext, "A suda dlya gesta mojesh dat'.html");
        }
    }
	
	@GET
	@Path("/get")
	public Response getAds( @DefaultValue("") @QueryParam("auth") String auth ) {
		if (!UserDAO.getRole(auth).equals("manager") || !UserDAO.getRole(auth).equals("guest")) {
			return null;
		}
		String json = SeasonDAO.getAds(auth);
		return Response.ok(json).build();
	}
}
