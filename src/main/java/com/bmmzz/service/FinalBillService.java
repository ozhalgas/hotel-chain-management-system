package com.bmmzz.service;

import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmmzz.userDAO.UserDAO;

@Path("/bill/{guestID}-{roomType}-{roomNumber}-{floor}-{checkInDate}")
public class FinalBillService {

	@Context ServletContext servletContext;
	
	public FinalBillService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream get( @DefaultValue("") @QueryParam("auth") String auth,
			@PathParam("guestID") int guestID,
			@PathParam("roomtype") String roomType,
			@PathParam("roomNumber") String roomNumber,
			@PathParam("floor") int floor,
			@PathParam("checkInDate") String checkInDate ) {
        if(!UserDAO.checkAuth(auth))
            return Helper.getPage(servletContext, "accessDeniedPage.html");
        switch( UserDAO.getRole(auth) ) {
            case "desk-clerk":
                return Helper.getPage(servletContext, "finalBillPage.html");
            default:
                return Helper.getPage(servletContext, "accessDeniedPage.html");
        }
    }
}
