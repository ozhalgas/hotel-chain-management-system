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
import javax.ws.rs.core.Response;

import com.bmmzz.userDAO.RoomDAO;
import com.bmmzz.userDAO.UserDAO;

@Path("/bill")
public class FinalBillService {

	@Context ServletContext servletContext;
	
	public FinalBillService() {}
	
	@GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream get( @DefaultValue("") @QueryParam("auth") String auth) {
        if(!UserDAO.checkAuth(auth))
            return Helper.getPage(servletContext, "accessDeniedPage.html");
        switch( UserDAO.getRole(auth) ) {
            case "desk-clerk":
                return Helper.getPage(servletContext, "finalBillPage.html");
            default:
                return Helper.getPage(servletContext, "accessDeniedPage.html");
        }
    }
	
	@GET
	@Path("/{guestID}-{roomType}-{roomNumber}-{floor}-{checkInDate}")
	public Response finalBill(@DefaultValue("") @QueryParam("auth") String auth,
			@PathParam("guestID") int guestID,
			@PathParam("roomType") String roomType,
			@PathParam("roomNumber") String roomNumber,
			@PathParam("floor") int floor,
			@PathParam("checkInDate") String checkInDate) {
		if (!UserDAO.checkRoleAndAuth(auth, "desk-clerk"))
			return null;
		checkInDate = checkInDate.replace(':', '-');
		roomType = roomType.replace(':', '-');
		String json = RoomDAO.finalBill(auth, guestID, roomType, roomNumber, floor, checkInDate);
		return Response.ok(json).build();
	}
}
