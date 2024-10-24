package com.example.lootmarketbackend.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class AstaDTO {
    public int idAsta;
    public String emailCreatore;
    public String titolo;
    public String categoria;
    public double prezzoPartenza;
    public int anno;
    public int mese;
    public int giorno;
    public String descrizione;
    //public byte[] immagineProdotto;
    public double ultimaOfferta;
    public double sogliaMinima;
    public String tipoAsta;
    public Boolean offertaFatta;


    public AstaDTO(int idAsta, String emailCreatore, String titolo, String categoria, double prezzoPartenza, int anno, int mese, int giorno, String descrizione, double ultimaOfferta, double sogliaMinima, String tipoAsta, Boolean offertaFatta) {
        this.idAsta = idAsta;
        this.emailCreatore = emailCreatore;
        this.titolo = titolo;
        this.categoria = categoria;
        this.prezzoPartenza = prezzoPartenza;
        this.anno = anno;
        this.mese = mese;
        this.giorno = giorno;
        this.descrizione = descrizione;
        //this.immagineProdotto = immagineProdotto;
        this.ultimaOfferta = ultimaOfferta;
        this.sogliaMinima = sogliaMinima;
        this.tipoAsta = tipoAsta;
        this.offertaFatta = offertaFatta;
    }
}
