package com.bmmzz.userDAO;

public class GuestInfo {
	int guestID;
	String fullName;
	String identificationType;
	String identificationNumber;
	String categoryName;
	String address;
	String homePhoneNumber;
	String mobilePhoneNumber;
    
	public void setGuestID(int str) {
        guestID = str;
    }
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
	
    
    
    public int getGuestID() {
        return guestID;
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
}

