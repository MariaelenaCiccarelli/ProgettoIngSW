package com.example.lootmarketbackend.Modelli;

public class Contatti {

    String sitoWeb;
    String facebook;
    String instagram;

    public Contatti(String sitoWeb, String facebook, String instagram){
        this.sitoWeb = sitoWeb;
        this.facebook = facebook;
        this.instagram = instagram;
    }

    public String getSitoWeb() {
        return sitoWeb;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }
}
