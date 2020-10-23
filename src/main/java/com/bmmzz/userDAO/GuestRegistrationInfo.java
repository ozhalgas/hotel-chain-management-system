package com.bmmzz.userDAO;

public class GuestRegistrationInfo {
	String fullName;
	String identificationType;
	String identificationNumber;
	String categoryName;
	String address;
	String homePhoneNumber;
	String mobilePhoneNumber;
	String login;
	String password;
    
    public void setFullName(String str) {
        fullName = str;
    }
    public void setIdentificationType(String str) {
        identificationType = str;
    } 
    public void setIdentificationNumber(String str) {
        identificationNumber = str;
    } 
    public void setCategory(String str) {
        categoryName = str;
    } 
    public void setAddress(String str) {
        address = str;
    } 
    public void setHomePhoneNumber(String str) {
        homePhoneNumber = str;
    } 
    
    public void setMobilePhoneNumber(String str) {
        mobilePhoneNumber = str;
    } 

    
    public String getFullName() {
        return fullName;
    }
    public String getIdentificationType() {
        return identificationType;
    } 
    public String getIdentificationNumber() {
        return identificationNumber;
    } 
    public String getCategory() {
        return categoryName;
    } 
    public String getAddress() {
        return address;
    } 
    public String HomePhoneNumber() {
        return homePhoneNumber;
    } 
    
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	} 
}

