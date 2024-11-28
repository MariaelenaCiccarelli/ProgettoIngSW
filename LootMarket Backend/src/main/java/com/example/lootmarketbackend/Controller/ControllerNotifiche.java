package com.example.lootmarketbackend.Controller;

import com.example.lootmarketbackend.DAO.LegameDAO;
import com.example.lootmarketbackend.DAO.NotificaDAO;
import com.example.lootmarketbackend.ImplementazionePostgresDAO.LegameImplementazionePostgresDAO;
import com.example.lootmarketbackend.ImplementazionePostgresDAO.NotificaImplementazionePostgresDAO;
import com.example.lootmarketbackend.Modelli.Iscrizione;
import com.example.lootmarketbackend.Modelli.Legame;
import com.example.lootmarketbackend.Modelli.Notifica;
import com.example.lootmarketbackend.Modelli.Offerta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class ControllerNotifiche {

    public ArrayList<Notifica> databaseNotifiche = new ArrayList<>();

    public ControllerNotifiche(){
        databaseNotifiche = new ArrayList<>();
        leggiNotificheDAO();
    }

    public int getDatabaseSize(){
        return databaseNotifiche.size();
    }

    public Notifica getNotificaDatabase(int indice){
        return databaseNotifiche.get(indice);
    }

    public int getIdUltimaNotifica(){
        if(databaseNotifiche.size() > 0){
            return databaseNotifiche.getLast().getIdAsta();
        }
        else{
            return  0;
        }
    }



    public void leggiNotificheDAO(){
        ArrayList<Integer> idNotifiche = new ArrayList<>();
        ArrayList<Integer> tipiNotifiche = new ArrayList<>();
        ArrayList<String> destinatari = new ArrayList<>();
        ArrayList<Integer> idAste = new ArrayList<>();


        NotificaDAO notificaDAO = new NotificaImplementazionePostgresDAO();

        notificaDAO.leggiNotificheDB(idNotifiche, tipiNotifiche, destinatari, idAste);

        for(int i = 0; i < idNotifiche.size(); i++){
            Notifica notifica = new Notifica(idNotifiche.get(i), tipiNotifiche.get(i), destinatari.get(i), idAste.get(i));

            databaseNotifiche.add(notifica);
        }
    }

    //1 notifica aggiunta con successo, 0 altrimenti
    public int aggiungiNotificaDAO(int tipo, String destinatario, int idAsta){
        NotificaDAO notificaDAO = new NotificaImplementazionePostgresDAO();
        if(notificaDAO.aggiungiNotificaDB(tipo, destinatario, idAsta)==1){
            Notifica notifica = new Notifica(getIdUltimaNotifica()+1,tipo,destinatario,idAsta);
            databaseNotifiche.add(notifica);
            return 1;
        }
        return 0;
    }

    //1 notifica eliminata con successo, 0 altrimenti
    public int eliminaNotificaDAO(int id){
        NotificaDAO notificaDAO = new NotificaImplementazionePostgresDAO();
        if(notificaDAO.eliminaNotificaDB(id)==1){
            int i= 0;
            while((i < databaseNotifiche.size()) && (id != databaseNotifiche.get(i).getId())){
                i++;
            }
            if(i<databaseNotifiche.size()){
                databaseNotifiche.remove(i);
                return 1;
            }
        }
        return 0;
    }

}
