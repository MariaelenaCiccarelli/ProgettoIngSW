package com.example.lootmarketbackend.ImplementazionePostgresDAO;

import com.example.lootmarketbackend.DAO.NotificaDAO;
import com.example.lootmarketbackend.Database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class NotificaImplementazionePostgresDAO implements NotificaDAO {


    private Connection connection;

    public NotificaImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void leggiNotificheDB(ArrayList<Integer> idNotifiche,
                              ArrayList<Integer> tipiNotifiche,
                              ArrayList<String> destinatari,
                              ArrayList<Integer> idAste) {

        try{
            PreparedStatement leggiNotificheStatement = connection.prepareStatement("SELECT * FROM \"Notifiche\"");
            ResultSet rs = leggiNotificheStatement.executeQuery();

            while(rs.next()){
                idNotifiche.add(rs.getInt("Id"));
                tipiNotifiche.add(rs.getInt("Tipo"));
                destinatari.add(rs.getString("Destinatario"));
                idAste.add(rs.getInt("Asta"));
            }
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }
    //1 notifica aggiunta con successo, 0 altrimenti
    @Override
    public int aggiungiNotificaDB(
                                 int tipo,
                                 String destinatario,
                                 int idAsta) {
        try{
            PreparedStatement aggiungiNotificheStatement = connection.prepareStatement("INSERT INTO \"Notifiche\"" + "(\"Tipo\", \"Destinatario\", \"Asta\", \"Data\", \"Ora\")" +
                    "VALUES(?, ?, ?, ?, ?);");
            aggiungiNotificheStatement.setInt(1, tipo);
            aggiungiNotificheStatement.setString(2, destinatario);
            aggiungiNotificheStatement.setInt(3, idAsta);
            aggiungiNotificheStatement.setDate(4, Date.valueOf(LocalDate.now()));
            aggiungiNotificheStatement.setTime(5, Time.valueOf(LocalTime.now()));



            aggiungiNotificheStatement.executeUpdate();
            connection.close();
            return 1;
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
            return 0;
        }

    }
    //1 notifica eliminata con successo, 0 altrimenti
    @Override
    public int eliminaNotificaDB(int id) {
        try {
            PreparedStatement eliminaNotificaStatement = connection.prepareStatement("DELETE FROM \"Notifiche\" WHERE \"Id\" = '"+id+"'");
            eliminaNotificaStatement.executeUpdate();
            connection.close();
            return 1;
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
            return 0;
        }

    }
}
