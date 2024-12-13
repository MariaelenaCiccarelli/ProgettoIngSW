package com.example.lootmarketbackend.Models;

import java.time.LocalDateTime;

public interface Legame {

    String emailUtente = "emailUtente";
    int idAsta = 0;

    public double getOfferta();

    public void setOfferta(double offerta);

    public LocalDateTime getTimestamp();

    public void setTimestamp(LocalDateTime timestamp);

    public String getEmailUtente();

    public void setEmailUtente(String emailUtente);

    public int getIdAsta();

    public void setIdAsta(int idAsta);
}