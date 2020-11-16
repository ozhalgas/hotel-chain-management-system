package com.bmmzz.service;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.bmmzz.userDAO.EmployeeDAO;
import com.bmmzz.userDAO.HotelDAO;
import com.bmmzz.userDAO.RoomDAO;
import com.bmmzz.userDAO.UserDAO;

@Path("/schedule")
public class EmployeeScheduleService {
	@Context ServletContext servletContext;
	
	public EmployeeScheduleService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	public Response employeeSchedulesInfo( @DefaultValue("") @QueryParam("auth") String auth ) {
		if(!UserDAO.checkRoleAndAuth(auth, "manager"))
			return null;
		String json = EmployeeDAO.getSchedules(auth);
		return Response.ok(json).build();
	}
	
	@POST
	@Path("/edit/{employeeID}-{newStartTime}-{newEndTime}")
	public Response checkOut(@DefaultValue("") @QueryParam("auth") String auth,
			@PathParam("employeeID") int employeeID,
			@PathParam("newStartTime") String startTime,
			@PathParam("newEndTime") String endTime) {
		if (!UserDAO.checkRoleAndAuth(auth, "manager"))
			return null;
		EmployeeDAO.editEmployeeSchedule(auth, employeeID, startTime, endTime);
		return Response.ok().build();
	}
}
