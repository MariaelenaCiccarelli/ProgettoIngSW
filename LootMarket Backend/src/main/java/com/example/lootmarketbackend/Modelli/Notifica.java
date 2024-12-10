package com.example.lootmarketbackend.Modelli;

public class Notifica {
    int id;
    int tipo;
    String Destinatario;
    int idAsta;

    public Notifica(int id, int tipo, String destinatario, int idAsta) {
        this.id = id;
        this.tipo = tipo;
        Destinatario = destinatario;
        this.idAsta = idAsta;
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
}