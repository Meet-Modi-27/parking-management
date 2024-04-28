package com.example.parking_management.models;

public class spotModel {

    String location,spotType,spotId,userId;

    spotModel(){

    }

    public spotModel(String location, String spotType, String spotId, String userId) {
        this.location = location;
        this.spotType = spotType;
        this.spotId = spotId;
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
