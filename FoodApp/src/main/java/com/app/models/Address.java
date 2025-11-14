package com.app.models;

import java.util.Date;


public class Address {
    
    private int addressId;
    private int userId;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String landmark;
    private Date createdAt;

    // Default constructor
    public Address() {
    }

    // Parameterized constructor
    public Address(int addressId, int userId, String street, String city, String state, String zip, String landmark, Date createdAt) {
        super();
        this.addressId = addressId;
        this.userId = userId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.landmark = landmark;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Address [addressId=" + addressId + ", userId=" + userId + ", street=" + street + ", city=" + city
                + ", state=" + state + ", zip=" + zip + ", landmark=" + landmark + ", createdAt=" + createdAt + "]";
    }
}
