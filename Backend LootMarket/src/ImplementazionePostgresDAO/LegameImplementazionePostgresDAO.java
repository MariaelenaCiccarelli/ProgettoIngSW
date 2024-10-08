package ImplementazionePostgresDAO;

import DAO.LegameDAO;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class LegameImplementazionePostgresDAO implements LegameDAO {

    private Connection connection;

    public LegameImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void leggiLegamiDB(ArrayList<Integer> idAste,
                              ArrayList<String> emailOfferenti,
                              ArrayList<Double> offerte,
                              ArrayList<LocalDate> date,
                              ArrayList<LocalTime> ore) {

        try{
            PreparedStatement leggiLegamiStatement = connection.prepareStatement("SELECT * FROM \"Legami\"");
            ResultSet rs = leggiLegamiStatement.executeQuery();

            while(rs.next()){
                idAste.add(rs.getInt("Id Asta"));
                emailOfferenti.add(rs.getString("Email Offerente"));
                offerte.add(rs.getDouble("Offerta"));
                date.add(rs.getDate("Data").toLocalDate());
                ore.add(rs.getTime("Ora").toLocalTime());
            }
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }

    @Override
    public void aggiungiLegameDB(int idAsta,
                                 String emailOfferente,
                                 double offerta,
                                 LocalDateTime timestamp) {

        try{
            PreparedStatement aggiungiLegameStatement = connection.prepareStatement("INSERT INTO \"Legami\"" + "(\"Id Asta\", \"Email Offerente\", \"Offerta\", \"Data\", \"Ora\")" +
                    "VALUES('"+idAsta+"','"+emailOfferente+"', '"+offerta+"','"+timestamp.toLocalDate()+"', '"+timestamp.toLocalTime()+"');");
            aggiungiLegameStatement.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }

    @Override
    public void eliminaLegameDB(int idAsta, String emailOfferente) {

        try {
            PreparedStatement eliminaLegameStatement = connection.prepareStatement("DELETE FROM \"Legami\" WHERE \"Id Asta\" = '"+idAsta+"' AND \"Email Offerente\" = '"+emailOfferente+"'");
            eliminaLegameStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }

    }

    @Override
    public void modificaUltimaOffertaLegameDB(int idAsta,
                                              String emailOfferente,
                                              double offerta,
                                              LocalDateTime timestamp) {

        try{
            //PreparedStatement modificaUltimaOffertaLegameStatement = connection.prepareStatement("UPDATE \"Legami\" SET (\"Email Offerente\" = '"+emailOfferente+"' , \"Offerta\" = '"+offerta+"', \"Data\" = '"+timestamp.toLocalDate()+"', \"Ora\" = '"+timestamp.toLocalTime()+"') WHERE \"Id Asta\" = '"+idAsta+"'");
            PreparedStatement modificaUltimaOffertaLegameStatement = connection.prepareStatement("UPDATE \"Legami\" SET \"Offerta\" = ?, \"Data\" = ?, \"Ora\" = ? WHERE \"Id Asta\" = ? AND \"Email Offerente\" = ?");
            modificaUltimaOffertaLegameStatement.setDouble(1, offerta);
            modificaUltimaOffertaLegameStatement.setDate(2, Date.valueOf(timestamp.toLocalDate()));
            modificaUltimaOffertaLegameStatement.setTime(3, Time.valueOf(timestamp.toLocalTime()));
            modificaUltimaOffertaLegameStatement.setInt(4, idAsta);
            modificaUltimaOffertaLegameStatement.setString(5, emailOfferente);
            modificaUltimaOffertaLegameStatement.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }
}
