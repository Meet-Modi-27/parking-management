package com.example.parking_management.models;

public class bookingsModel {
    String locationId,number,spotId,timestamp,userId,userName;

    bookingsModel(){

    }

    public bookingsModel(String locationId, String number, String spotId, String timestamp, String userId, String userName) {
        this.locationId = locationId;
        this.number = number;
        this.spotId = spotId;
        this.timestamp = timestamp;
        this.userId = userId;
        this.userName = userName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
