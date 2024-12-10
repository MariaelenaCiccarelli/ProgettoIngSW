package com.example.lootmarketbackend.Modelli;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Offerta implements Legame {

    private double offerta;
    private LocalDateTime timestamp;
    String emailUtente;
    int idAsta;

    public Offerta(String emailUtente, int IdAsta, double offerta, LocalDate data, LocalTime ora) {
        this.emailUtente = emailUtente;
        this.idAsta = IdAsta;
        this.offerta = offerta;
        this.timestamp = LocalDateTime.of(data, ora);
    }

    public double getOfferta() {
        return offerta;
    }

    public void setOfferta(double offerta) {
        this.offerta = offerta;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

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