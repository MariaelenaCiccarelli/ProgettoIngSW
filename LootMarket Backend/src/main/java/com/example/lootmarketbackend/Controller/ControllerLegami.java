package com.example.lootmarketbackend.Controller;

import com.example.lootmarketbackend.DAO.LegameDAO;
import com.example.lootmarketbackend.ImplementazionePostgresDAO.LegameImplementazionePostgresDAO;
import com.example.lootmarketbackend.Modelli.Iscrizione;
import com.example.lootmarketbackend.Modelli.Legame;
import com.example.lootmarketbackend.Modelli.Offerta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class ControllerLegami {

    public ArrayList<Legame> databaseLegami;

    public ControllerLegami(){
        databaseLegami = new ArrayList<>();
        leggiLegamiDAO();
    }





    public int getDatabaseSize(){
        return databaseLegami.size();
    }





    public Legame getLegameDatabase(int indice){
        return databaseLegami.get(indice);
    }





    public void leggiLegamiDAO(){

        ArrayList<Integer> idAste = new ArrayList<>();
        ArrayList<String> emailOfferenti = new ArrayList<>();
        ArrayList<Double> offerte = new ArrayList<>();
        ArrayList<LocalDate> date = new ArrayList<>();
        ArrayList<LocalTime> ore = new ArrayList<>();

        LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
        legameDAO.leggiLegamiDB(idAste, emailOfferenti, offerte, date, ore);
        for(int i = 0; i < idAste.size(); i++){
            Legame legame;
            if(offerte.get(i) == -1){
                legame = new Iscrizione(emailOfferenti.get(i), idAste.get(i));
            }else{
                legame = new Offerta(emailOfferenti.get(i), idAste.get(i), offerte.get(i), date.get(i), ore.get(i));
            }
            databaseLegami.add(legame);
        }
    }





    public void aggiungiLegamiDAO(int idAsta, String emailOfferente, double offerta, LocalDateTime timestamp){
        Legame legame;
        if(offerta == -1){
            legame = new Iscrizione(emailOfferente, idAsta);
        }else{
            legame = new Offerta(emailOfferente, idAsta, offerta, timestamp.toLocalDate(), timestamp.toLocalTime());
        }
        databaseLegami.add(legame);
        LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
        legameDAO.aggiungiLegameDB(idAsta, emailOfferente, offerta, timestamp);
    }





    public void eliminaLegameDAO(int idAsta, String emailOfferente){
        int i = 0;
        while((i < databaseLegami.size()) && (idAsta != databaseLegami.get(i).getIdAsta() || !emailOfferente.equals(databaseLegami.get(i).getEmailUtente()))){
            i++;
        }
        if(i<databaseLegami.size()){
            databaseLegami.remove(i);
            LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
            legameDAO.eliminaLegameDB(idAsta, emailOfferente);
        }
    }





    public void modificaUltimaOffertaLegameDAO(int idAsta, String emailOfferente, double offerta, LocalDateTime timestamp){
        int i = 0;
        while((i < databaseLegami.size()) && (idAsta != databaseLegami.get(i).getIdAsta() || !emailOfferente.equals(databaseLegami.get(i).getEmailUtente()))){
            i++;
        }
        if(i<databaseLegami.size()){
            Offerta nuovoLegame = new Offerta( emailOfferente, idAsta, offerta, timestamp.toLocalDate(),timestamp.toLocalTime());
            databaseLegami.set(i, nuovoLegame);
            LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
            legameDAO.modificaUltimaOffertaLegameDB(idAsta, emailOfferente, offerta, timestamp);
        }

    }





    //ritorna -1 se asta non presente
    public int getIndiceLegameByEmailAndIdAsta(String email, int idAsta) {
        if(databaseLegami.size()==0){
            return -1;
        }
        int i = 0;
        while((i < databaseLegami.size()) && (!email.equals(databaseLegami.get(i).getEmailUtente()) || (idAsta != databaseLegami.get(i).getIdAsta()))){
            i++;
        }
        if(i<databaseLegami.size()){
            if(email.equals(databaseLegami.get(i).getEmailUtente()) && idAsta == databaseLegami.get(i).getIdAsta()){
                return i;
            }
        }
        return -1;
    }





    public Offerta getUltimaOffertaLegame(int idAsta){
        Offerta ultimoLegame=null;
        for(Legame legame : databaseLegami){
            if(legame.getIdAsta() == idAsta){
                if(legame instanceof Offerta legameOfferta) {
                    if(ultimoLegame == null){
                        ultimoLegame = legameOfferta;
                    }else{
                        if(ultimoLegame.getTimestamp().isBefore(legameOfferta.getTimestamp())){
                            ultimoLegame = legameOfferta;
                        }
                    }
                }
            }
        }
        return ultimoLegame;
    }

    public double getOffertaLegameByIndice(int indice){
        return databaseLegami.get(indice).getOfferta();
    }

    public String getMailUtenteLegameByIndice(int indice){
        return databaseLegami.get(indice).getEmailUtente();
    }

    public int getIdAstaLegameByIndice(int indice){
        return databaseLegami.get(indice).getIdAsta();
    }



    
}