package com.example.lootmarketbackend.dto;

public class OffertaDTO {
    public int idAsta;
    public String mailUtente;
    public double offerta;

    public OffertaDTO(int idAsta, String mailUtente, double offerta) {
        this.idAsta = idAsta;
        this.mailUtente = mailUtente;
        this.offerta = offerta;
    }
}
