package com.example.lootmarketbackend.Models;

import java.time.LocalDate;


public class Utente {

    private String email;
    private String password;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String nazione;
    private String numeroCellulare;
    private LocalDate dataNascita;
    private Contatti contatti;
    private byte[] immagineProfilo;
    private String biografia;
    private Indirizzo indirizzoSpedizione;
    private Indirizzo indirizzoFatturazione;

    public Utente(String email, String password, String nome, String cognome, String codiceFiscale, String nazione, String numeroCellulare, LocalDate dataNascita, Contatti contatti, byte[] immagineProfilo, String biografia, Indirizzo indirizzoSpedizione, Indirizzo indirizzoFatturazione) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.nazione = nazione;
        this.numeroCellulare = numeroCellulare;
        this.dataNascita = dataNascita;
        this.contatti = contatti;
        this.immagineProfilo = immagineProfilo;
        this.biografia = biografia;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.indirizzoFatturazione = indirizzoFatturazione;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public String getNumeroCellulare() {
        return numeroCellulare;
    }

    public void setNumeroCellulare(String numeroCellulare) {
        this.numeroCellulare = numeroCellulare;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public Contatti getContatti() {
        return contatti;
    }

    public void setContatti(Contatti contatti) {
        this.contatti = contatti;
    }

    public byte[] getImmagineProfilo() {
        return immagineProfilo;
    }

    public void setImmagineProfilo(byte[] immagineProfilo) {
        this.immagineProfilo = immagineProfilo;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public Indirizzo getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    public void setIndirizzoSpedizione(Indirizzo indirizzoSpedizione) {
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

    public Indirizzo getIndirizzoFatturazione() {
        return indirizzoFatturazione;
    }

    public void setIndirizzoFatturazione(Indirizzo indirizzoFatturazione) {
        this.indirizzoFatturazione = indirizzoFatturazione;
    }
}