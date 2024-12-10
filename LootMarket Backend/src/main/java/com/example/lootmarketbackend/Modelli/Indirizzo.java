package com.example.lootmarketbackend.Modelli;

public class Indirizzo {

    private String via;
    private String citta;
    private String provincia;
    private String cap;

    public Indirizzo(String via, String citta, String provincia, String cap) {
        this.via = via;
        this.citta = citta;
        this.provincia = provincia;
        this.cap = cap;
    }

    public String getVia() {
        return via;
    }

    public String getCitta() {
        return citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getCap() {return cap;}
}