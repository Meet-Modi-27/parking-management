package com.example.parking_management.models;

public class vehicleModel {
    String make,model,number,type,userId;

    vehicleModel(){

    }
    public vehicleModel(String make, String model, String number, String type, String userId) {
        this.make = make;
        this.model = model;
        this.number = number;
        this.type = type;
        this.userId = userId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
