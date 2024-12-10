package com.example.lootmarketbackend.dto;

public class NotificaDTO {
    int id;
    int tipo;
    String Destinatario;
    int idAsta;
    String titoloAsta;

    public NotificaDTO(int id, int tipo, String destinatario, int idAsta, String titoloAsta) {
        this.id = id;
        this.tipo = tipo;
        Destinatario = destinatario;
        this.idAsta = idAsta;
        this.titoloAsta = titoloAsta;
    }

    public int getId() {
        return id;
    }

    public int getTipo() {
        return tipo;
    }

    public String getDestinatario() {
        return Destinatario;
    }

    public int getIdAsta() {
        return idAsta;
    }

    public String getTitoloAsta() {
        return titoloAsta;
    }
}