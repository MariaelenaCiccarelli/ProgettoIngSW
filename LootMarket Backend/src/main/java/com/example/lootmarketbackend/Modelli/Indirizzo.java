package com.example.lootmarketbackend.Modelli;

public class Indirizzo {

    //ATTRIBUTI
    private String via;
    private String citta;
    private String provincia;
    private String CAP;


    public Indirizzo(String via, String citta, String provincia, String CAP) {
        this.via = via;
        this.citta = citta;
        this.provincia = provincia;
        this.CAP = CAP;
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

    public String getCAP() {
        return CAP;
    }
}
