package com.example.parking_management.models;

public class usersModel {
    String name,username,email,number,userId;

    usersModel(){

    }
    public usersModel(String name, String username, String email, String number, String userId) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.number = number;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

