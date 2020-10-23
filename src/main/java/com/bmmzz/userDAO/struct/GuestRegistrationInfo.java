package com.bmmzz.userDAO.struct;

public class GuestRegistrationInfo {
	private String fullName;
	private String identificationType;
	private String identificationNumber;
	private String categoryName;
	private String address;
	private String homePhoneNumber;
	private String mobilePhoneNumber;
	private String login;
	private String password;
    
    public void setFullName(String str) {
        fullName = str;
    }
    public void setIdentificationType(String str) {
        identificationType = str;
    } 
    public void setIdentificationNumber(String str) {
        identificationNumber = str;
    } 
    public void setCategoryName(String str) {
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
    public String getCategoryName() {
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

