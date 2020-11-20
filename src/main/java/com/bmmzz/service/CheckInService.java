package com.bmmzz.service;

import java.io.InputStream;

import javax.servlet.ServletContext;
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
import com.bmmzz.userDAO.HotelDAO;
import com.bmmzz.userDAO.RoomDAO;
import com.bmmzz.userDAO.UserDAO;

@Path("/checkin")
public class CheckInService {

	@Context ServletContext servletContext;
	
	public CheckInService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream get( @DefaultValue("") @QueryParam("auth") String auth ) {
            return Helper.getPage(servletContext, "accessDeniedPage.html");
    }
	
	@GET
	@Path("/{guestID}-{roomTypeName}-{checkInDate}-{checkOutDate}-{numberOfRooms}")
    @Produces({MediaType.TEXT_HTML})
    public InputStream getRooms( @DefaultValue("") @QueryParam("auth") String auth ) {
        if(!UserDAO.checkAuth(auth))
            return Helper.getPage(servletContext, "accessDeniedPage.html");
        switch( UserDAO.getRole(auth) ) {
            case "desk-clerk":
                return Helper.getPage(servletContext, "checkInPage.html");
            default:
                return Helper.getPage(servletContext, "accessDeniedPage.html");
        }
    }
	
	@GET
	@Path("/rooms")
	public Response hotelRoomsInfo( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkRoleAndAuth(auth, "desk-clerk", "admin"))
			return null;
		String json = HotelDAO.getHotelRooms(auth);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/clean/{roomNumber}-{floor}-{roomType}")
	public Response clean( @DefaultValue("") @QueryParam("auth") String auth,
						   @PathParam("roomNumber") String roomNumber,
						   @PathParam("floor") int floor,
						   @PathParam("roomType") String roomType) {
		if(!UserDAO.checkRoleAndAuth(auth, "desk-clerk", "admin"))
			return null;
		RoomDAO.changeCleanState(auth, roomNumber, floor, roomType);
		return Response.ok().build();
	}
	
	@GET
	@Path("/hotel-features")
	public Response getHotelFeatures( @DefaultValue("") @QueryParam("auth") String auth) {
		if(!UserDAO.checkRoleAndAuth(auth, "desk-clerk", "admin"))
			return null;
		String json = HotelDAO.getHotelFeaturesInfo(auth);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/{guestID}-{roomTypeName}-{roomNumber}-{roomFloor}-{checkInDate}-{checkOutDate}-{occupants}")
	//@Path("/test")
	public Response checkIn( @DefaultValue("") 	@QueryParam("auth") String auth,
												@PathParam("checkInDate") String checkInDate,
												@PathParam("checkOutDate") String checkOutDate,
												@PathParam("roomTypeName") String roomTypeName,
												@PathParam("roomNumber") String roomNumber,
												@PathParam("roomFloor") int roomFloor,
												@PathParam("guestID") int guestID,
												@PathParam("occupants") String occupantsIDs,
												@FormParam("hotelID") int hotelID) {
		
		if (!UserDAO.checkRoleAndAuth(auth, "desk-clerk"))
			return null;	
		checkInDate = checkInDate.replace(':', '-');
		checkOutDate = checkOutDate.replace(':', '-');
		hotelID = EmployeeDAO.getHotelID(auth);
		
		//hotelId and roomTypeName should be added after DB updating
		//insert into single_stay
		RoomDAO.checkInRoom(checkInDate, checkOutDate, roomNumber, roomFloor, guestID, roomTypeName, hotelID);
		String[] occupantsArr = occupantsIDs.split(":");
		int n = occupantsArr.length;
		
		//insert into Occupies
		for(String occupID : occupantsArr) {
			RoomDAO.occupy(roomNumber, roomFloor, Integer.parseInt(occupID), checkInDate, checkOutDate, hotelID, roomTypeName);
		}
		
		HotelDAO.setRoomOccupied(hotelID, roomNumber, 1);
		HotelDAO.setNumOccupants(hotelID, roomNumber, n);
		
		return Response.ok().build();
	}
}
