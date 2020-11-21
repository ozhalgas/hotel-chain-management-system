package com.bmmzz.service;

import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
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
import com.bmmzz.userDAO.struct.CleaningListItem;

@Path("/schedule")
public class EmployeeScheduleService {
	@Context ServletContext servletContext;
	public static LinkedList<CleaningListItem> cleaningList = HotelDAO.initialCleaningListInsert();
	
	public EmployeeScheduleService() {
		UserDAO.connectToUserDAO();
		cleaningList = HotelDAO.initialCleaningListInsert();
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
	public Response adjustHours(@DefaultValue("") @QueryParam("auth") String auth,
			@PathParam("employeeID") int employeeID,
			@PathParam("newStartTime") String startTime,
			@PathParam("newEndTime") String endTime) {
		if (!UserDAO.checkRoleAndAuth(auth, "manager"))
			return null;
		EmployeeDAO.editEmployeeSchedule(auth, employeeID, startTime, endTime);
		return Response.ok().build();
	}
	
	@POST
	@Path("/edit/{employeeID}-{newPayRate}")
	public Response editPayRate(@DefaultValue("") @QueryParam("auth") String auth,
			@PathParam("employeeID") int employeeID,
			@PathParam("newPayRate") String payRate) {
		if (!UserDAO.checkRoleAndAuth(auth, "manager"))
			return null;
		EmployeeDAO.editEmployeePayRate(auth, employeeID, payRate);
		return Response.ok().build();
	}
	
	@GET
	@Path("/cleaning")
	public Response editPayRate(@DefaultValue("") @QueryParam("auth") String auth) {
		if (!UserDAO.checkRoleAndAuth(auth, "desk-clerk", "manager", "admin"))
			return null;
		String json = HotelDAO.getCleaningSchedule(auth);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/delete/{employeeID}")
	public Response deleteEmployee(@DefaultValue("") @QueryParam("auth") String auth,
			@PathParam("employeeID") int employeeID) {
		if (!UserDAO.checkRoleAndAuth(auth, "admin"))
			return null;
		EmployeeDAO.deleteEmployee(employeeID);
		return Response.ok().build();
	}
	
	@Path("/admin")
	public Response getEmployees(@DefaultValue("") @QueryParam("auth") String auth) {
		if (!UserDAO.checkRoleAndAuth(auth, "admin"))
			return null;
		String json = EmployeeDAO.getEmployees();
		return Response.ok(json).build();
	}
}
