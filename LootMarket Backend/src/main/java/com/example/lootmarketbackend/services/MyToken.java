package com.example.lootmarketbackend.services;

public class MyToken {

    private String token;
    private Boolean isBusiness;

    public MyToken(String token, Boolean isBusiness) {
        this.token = token;
        this.isBusiness = isBusiness;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
