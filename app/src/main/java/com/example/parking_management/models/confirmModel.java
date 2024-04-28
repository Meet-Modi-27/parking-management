package com.example.parking_management.models;

public class confirmModel {
    String userId,userName,spotId,timestamp,locationId,number;

    confirmModel(){

    }

    public confirmModel(String userId, String userName, String spotId, String timestamp, String locationId, String number) {
        this.userId = userId;
        this.userName = userName;
        this.spotId = spotId;
        this.timestamp = timestamp;
        this.locationId = locationId;
        this.number = number;
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
}
