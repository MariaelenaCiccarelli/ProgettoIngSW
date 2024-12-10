package com.example.lootmarketbackend.dto;

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
    public double ultimaOfferta;
    public double sogliaMinima;
    public String tipoAsta;
    public Boolean offertaFatta;
    public String immagineAsta;

    public AstaDTO(int idAsta, String emailCreatore, String titolo, String categoria, double prezzoPartenza, int anno, int mese, int giorno, String descrizione, double ultimaOfferta, double sogliaMinima, String tipoAsta, Boolean offertaFatta, String immagineAsta) {
        this.idAsta = idAsta;
        this.emailCreatore = emailCreatore;
        this.titolo = titolo;
        this.categoria = categoria;
        this.prezzoPartenza = prezzoPartenza;
        this.anno = anno;
        this.mese = mese;
        this.giorno = giorno;
        this.descrizione = descrizione;
        this.ultimaOfferta = ultimaOfferta;
        this.sogliaMinima = sogliaMinima;
        this.tipoAsta = tipoAsta;
        this.offertaFatta = offertaFatta;
        this.immagineAsta = immagineAsta;
    }
}