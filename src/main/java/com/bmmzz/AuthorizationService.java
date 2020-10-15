package com.bmmzz;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/authorization")
public class AuthorizationService {
	
	@Context ServletContext servletContext;
	
	public AuthorizationService() {
		UserDAO.connectToUserDAO();
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream get() {	
		return Helper.getPage(servletContext, "authorizationPage.html");
	}
	
	@POST
	public String authorization(@FormParam("username") String username, 
							    @FormParam("password") String password) {
		String auth = UserDAO.getEncodedAuth(username, password);
		if(UserDAO.checkAuth(auth)) {
			return "authorization/" + UserDAO.getRole(auth) + "?auth=" + auth;
		}
		return null;
	}
	
	@GET
	@Path("/admin")
	@Produces({MediaType.TEXT_HTML})
	public InputStream getAdminPage(@DefaultValue("") @QueryParam("auth") String auth) {
		if( !UserDAO.checkRoleAndAuth(auth, "admin") ) {
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
		
		return Helper.getPage(servletContext, "adminPage.html");
	}
	
	@GET
	@Path("/guest")
	@Produces({MediaType.TEXT_HTML})
	public InputStream getGuestPage(@DefaultValue("") @QueryParam("auth") String auth) {
		
		if( !UserDAO.checkRoleAndAuth(auth, "guest") ) {
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
		
		return Helper.getPage(servletContext, "guestPage.html");
	}
	
	@GET
	@Path("/manager")
	@Produces({MediaType.TEXT_HTML})
	public InputStream getManagerPage(@DefaultValue("") @QueryParam("auth") String auth) {
		
		if( !UserDAO.checkRoleAndAuth(auth, "manager") ) {
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
		
		return Helper.getPage(servletContext, "managerPage.html");
	}
	
	@GET
	@Path("/desk-clerk")
	@Produces({MediaType.TEXT_HTML})
	public InputStream getDeskClerkPage(@DefaultValue("") @QueryParam("auth") String auth) {
		
		if( !UserDAO.checkRoleAndAuth(auth, "desk-clerk") ) {
			return Helper.getPage(servletContext, "accessDeniedPage.html");
		}
		
		return Helper.getPage(servletContext, "deskClerkPage.html");
	}
}
