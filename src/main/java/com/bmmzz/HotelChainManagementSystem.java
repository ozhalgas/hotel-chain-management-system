package com.bmmzz;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

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
	}
	
	public Set<Class<?>> getClasses() {
		return empty;
	}
	
	public Set<Object> getSingletons() {
		return singletons;
	}
}