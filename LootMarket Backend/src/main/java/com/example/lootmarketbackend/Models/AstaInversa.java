package com.example.lootmarketbackend.Models;

import java.time.LocalDateTime;

public class AstaInversa extends AstaTempoFisso {

    public AstaInversa(int idAsta,
                       String emailCreatore,
                       String titolo,
                       String categoria,
                       double prezzoPartenza,
                       LocalDateTime dataScadenza,
                       String descrizione,
                       byte[] immagineProdotto,
                       double ultimaOfferta) {
        super(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, 0);
    }






    @Override
    //ritorna -1 se la presentazione dell'offerta non va a buon fine, 1 se invece viene effettuata con successo
    public int presentaOfferta(double offerta){
        if(offerta < getUltimaOfferta() && offerta >0){
            setUltimaOfferta(offerta);
            return 1;
        }else{
            return -1;
        }
    }





}