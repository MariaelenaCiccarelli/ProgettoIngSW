package com.example.lootmarketbackend.Models;

import java.time.LocalDateTime;

public class AstaConclusa implements Asta{

    private int idAsta;
    private String emailCreatore;
    private String titolo;
    private String categoria;
    private double prezzoPartenza;
    private LocalDateTime dataScadenza;
    private String descrizione;
    private byte[] immagineProdotto;
    private String emailVincitore;
    private double costoFinale;


    public AstaConclusa(int idAsta, String emailCreatore, String titolo, String categoria, double prezzoPartenza, LocalDateTime dataScadenza, String descrizione, byte[] immagineProdotto, String emailVincitore, double costoFinale) {
        this.idAsta = idAsta;
        this.emailCreatore = emailCreatore;
        this.titolo = titolo;
        this.categoria = categoria;
        this.prezzoPartenza = prezzoPartenza;
        this.dataScadenza = dataScadenza;
        this.descrizione = descrizione;
        this.immagineProdotto = immagineProdotto;
        this.emailVincitore = emailVincitore;
        this.costoFinale = costoFinale;
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
        return costoFinale;
    }

    public void setUltimaOfferta(double ultimaOfferta) {}

    public double getSogliaMinima() {
        return 0;
    }

    public void setSogliaMinima(double sogliaMinima) {}

    public String getEmailVincitore() {
        return emailVincitore;
    }

    public void setEmailVincitore(String emailVincitore) {
        this.emailVincitore = emailVincitore;
    }

    public double getCostoFinale() {
        return costoFinale;
    }

    public void setCostoFinale(double costoFinale) {
        this.costoFinale = costoFinale;
    }

}