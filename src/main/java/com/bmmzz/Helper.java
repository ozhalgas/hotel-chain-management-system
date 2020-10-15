package com.bmmzz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.ServletContext;

public class Helper{
	
	public Helper() {
		
	}
	
	public static InputStream getPage(ServletContext servletContext, String pageName) {
		File f = new File(servletContext.getRealPath("/WEB-INF") + "/" + pageName);
		System.out.println("Opening file " + pageName);
		
		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find " + pageName);
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}