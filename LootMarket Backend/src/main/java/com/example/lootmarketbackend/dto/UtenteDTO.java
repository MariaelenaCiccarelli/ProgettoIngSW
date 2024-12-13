package com.example.lootmarketbackend.dto;

import com.example.lootmarketbackend.Models.Indirizzo;

public class UtenteDTO {
    public String nome;
    public String codiceFiscale;
    public String mail;
    public int dataDiNascitaAnno;
    public int dataDiNascitaMese;
    public int dataDiNascitaGiorno;
    public String nazione;
    public String numeroCellulare;
    public Indirizzo indirizzoSpedizione;
    public Indirizzo indirizzoFatturazione;
    public String sito;
    public String socialFacebook;
    public String socialInstagram;
    public String biografia;
    public String ragioneSociale;
    public String partitaIva;
    public String numeroAziendale;
    public String immagineProfilo;

    public UtenteDTO(String nome, String codiceFiscale, String mail, int dataDiNascitaAnno, int dataDiNascitaMese, int dataDiNascitaGiorno, String nazione, String numeroCellulare, Indirizzo indirizzoSpedizione, Indirizzo indirizzoFatturazione, String sito, String socialFacebook, String socialInstagram, String biografia, String ragioneSociale, String partitaIva, String numeroAziendale, String immagineProfilo) {
        this.nome = nome;
        this.codiceFiscale = codiceFiscale;
        this.mail = mail;
        this.dataDiNascitaAnno = dataDiNascitaAnno;
        this.dataDiNascitaMese = dataDiNascitaMese;
        this.dataDiNascitaGiorno = dataDiNascitaGiorno;
        this.nazione = nazione;
        this.numeroCellulare = numeroCellulare;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.indirizzoFatturazione = indirizzoFatturazione;
        this.sito = sito;
        this.socialFacebook = socialFacebook;
        this.socialInstagram = socialInstagram;
        this.biografia = biografia;
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
        this.numeroAziendale = numeroAziendale;
        this.immagineProfilo = immagineProfilo;
    }
}