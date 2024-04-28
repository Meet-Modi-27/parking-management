package com.example.parking_management.models;

public class spotModel {

    String location,spotType,spotId;
    Boolean isEmpty;

    spotModel(){

    }

    public spotModel(String location, String spotType, String spotId, Boolean isEmpty) {
        this.location = location;
        this.spotType = spotType;
        this.spotId = spotId;
        this.isEmpty = isEmpty;
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

    public Boolean getEmpty() {
        return isEmpty;
    }

    public void setEmpty(Boolean empty) {
        isEmpty = empty;
    }
}
