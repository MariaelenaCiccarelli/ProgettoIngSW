package com.example.lootmarketbackend.services;

public class MyToken {

    private String token;
    private boolean business;

    public MyToken(String token, boolean business) {
        this.token = token;
        this.business = business;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getBusiness() {
        return business;
    }

    public void setBusiness(boolean business) {
        this.business = business;
    }
}
