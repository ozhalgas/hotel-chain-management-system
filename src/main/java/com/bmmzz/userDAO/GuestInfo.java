package com.bmmzz.userDAO;

public class GuestInfo {
	String guestID;
	String fullName;
	String identificationType;
	String identificationNumber;
	String category;
	String address;
	String homePhoneNumber;
	String mobilePhoneNumber;
    
	public void setGuestID(String str) {
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
        category = str;
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
	
    
    
    public String getGuestID() {
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
        return category;
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

