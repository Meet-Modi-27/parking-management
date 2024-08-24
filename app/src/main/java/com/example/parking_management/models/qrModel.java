package com.example.parking_management.models;

public class qrModel {
    String location,spotId,spotType;

    qrModel(){

    }
    public qrModel(String location, String spotId, String spotType) {
        this.location = location;
        this.spotId = spotId;
        this.spotType = spotType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }
}
