package com.example.lootmarketbackend.DAO;

import java.util.ArrayList;

public interface NotificaDAO {

    void leggiNotificheDB(ArrayList<Integer> idNotifiche,
                       ArrayList<Integer> tipiNotifiche,
                       ArrayList<String> destinatari,
                       ArrayList<Integer> idAste);

    int aggiungiNotificaDB(int tipo, String destinatario, int idAsta);

    int eliminaNotificaDB(int id);

}