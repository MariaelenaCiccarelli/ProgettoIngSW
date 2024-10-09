package com.example.lootmarketbackend.Modelli;

import java.time.LocalDateTime;

public class Iscrizione implements Legame {
    String emailUtente;
    int idAsta;

    public Iscrizione(String emailUtente, int idAsta) {
        this.emailUtente = emailUtente;
        this.idAsta = idAsta;
    }


    public double getOfferta() {
        return -1;
    }

    public void setOfferta(double offerta){}

    public LocalDateTime getTimestamp() {
        return null;
    }

    public void setTimestamp(LocalDateTime timestamp) {}

    public String getEmailUtente() {
        return emailUtente;
    }

    public void setEmailUtente(String emailUtente) {
        this.emailUtente = emailUtente;
    }

    public int getIdAsta() {
        return idAsta;
    }

    public void setIdAsta(int idAsta) {
        this.idAsta = idAsta;
    }
}
