package com.example.lootmarketbackend.Models;

import java.time.LocalDateTime;

public class AstaTempoFisso implements Asta {

    private int idAsta;
    private String emailCreatore;
    private String titolo;
    private String categoria;
    private double prezzoPartenza;
    private LocalDateTime dataScadenza;
    private String descrizione;
    private byte[] immagineProdotto;
    private double ultimaOfferta;
    private double sogliaMinima;

    public AstaTempoFisso(int idAsta,
                          String emailCreatore,
                          String titolo,
                          String categoria,
                          double prezzoPartenza,
                          LocalDateTime dataScadenza,
                          String descrizione,
                          byte[] immagineProdotto,
                          double ultimaOfferta,
                          double sogliaMinima) {

        this.idAsta = idAsta;
        this.emailCreatore = emailCreatore;
        this.titolo = titolo;
        this.categoria = categoria;
        this.prezzoPartenza = prezzoPartenza;
        this.dataScadenza = dataScadenza;
        this.descrizione = descrizione;
        this.immagineProdotto = immagineProdotto;
        this.ultimaOfferta = ultimaOfferta;
        this.sogliaMinima = sogliaMinima;
    }

    public int getIdAsta() {
        return idAsta;
    }

    public void setIdAsta(int idAsta) {
        this.idAsta = idAsta;
    }

    public String getEmailCreatore() {
        return emailCreatore;
    }

    public void setEmailCreatore(String emailCreatore) {
        this.emailCreatore = emailCreatore;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrezzoPartenza() {
        return prezzoPartenza;
    }

    public void setPrezzoPartenza(double prezzoPartenza) {
        this.prezzoPartenza = prezzoPartenza;
    }

    public LocalDateTime getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDateTime dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public byte[] getImmagineProdotto() {
        return immagineProdotto;
    }

    public void setImmagineProdotto(byte[] immagineProdotto) {
        this.immagineProdotto = immagineProdotto;
    }

    public double getUltimaOfferta() {
        return ultimaOfferta;
    }

    public void setUltimaOfferta(double ultimaOfferta) {
        this.ultimaOfferta = ultimaOfferta;
    }

    public double getSogliaMinima() {
        return sogliaMinima;
    }

    public void setSogliaMinima(double sogliaMinima) {
        this.sogliaMinima = sogliaMinima;
    }





    //ritorna -1 se la presentazione dell'offerta non va a buon fine, 1 se invece viene effettuata con successo
    public int presentaOfferta(double offerta){
        if(offerta > this.ultimaOfferta){
            setUltimaOfferta(offerta);
            return 1;
        }else{
            return -1;
        }
    }





}