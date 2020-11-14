package com.bmmzz;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.bmmzz.service.AuthorizationService;
import com.bmmzz.service.BookManagementService;
import com.bmmzz.service.CheckInService;
import com.bmmzz.service.CheckOutService;
import com.bmmzz.service.EmployeeRegistrationService;
import com.bmmzz.service.EmployeeScheduleService;
import com.bmmzz.service.FinalBillService;
import com.bmmzz.service.ProfileService;
import com.bmmzz.service.RegistrationService;
import com.bmmzz.service.RoomBookingService;

@ApplicationPath("/services")
public class HotelChainManagementSystem extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	
	public HotelChainManagementSystem() {
		singletons.add(new AuthorizationService());
		singletons.add(new RegistrationService());
		singletons.add(new ProfileService());
		singletons.add(new EmployeeRegistrationService());
		singletons.add(new BookManagementService());
		singletons.add(new RoomBookingService());
		singletons.add(new BookManagementService());
		singletons.add(new CheckInService());
		singletons.add(new CheckOutService());
		singletons.add(new FinalBillService());
		singletons.add(new EmployeeScheduleService());
	}
	
	public Set<Class<?>> getClasses() {
		return empty;
	}
	
	public Set<Object> getSingletons() {
		return singletons;
	}
}