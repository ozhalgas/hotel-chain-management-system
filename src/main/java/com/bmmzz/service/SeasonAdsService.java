package com.bmmzz.service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmmzz.userDAO.EmployeeDAO;
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
                return Helper.getPage(servletContext, "A suda dlya vseh drugih mojesh dat'.html");
        }
    }
	
	@GET
	@Path("/get")
	public Response getAds( @DefaultValue("") @QueryParam("auth") String auth ) {
		String json = SeasonDAO.getAds(auth);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/delete")
	public Response deleteAd(@DefaultValue("") @QueryParam("auth") String auth) {
		if (!UserDAO.checkRoleAndAuth(auth, "manager"))
			return null;
		int hotelID = EmployeeDAO.getHotelID(auth);
		SeasonDAO.deleteAd(hotelID);
		return Response.ok().build();
	}
	
	@GET
	@Path("/update/{adTxt}")
	public Response updateAd(@DefaultValue("") @QueryParam("auth") String auth,
								     @PathParam("adTxt") String adTxt) {
		if (!UserDAO.checkRoleAndAuth(auth, "manager"))
			return null;
		int hotelID = EmployeeDAO.getHotelID(auth);
		SeasonDAO.updateAd(hotelID, adTxt);
		return Response.ok().build();
	}
}
