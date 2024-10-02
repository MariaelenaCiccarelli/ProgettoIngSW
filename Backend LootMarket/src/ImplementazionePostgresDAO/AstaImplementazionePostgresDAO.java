package ImplementazionePostgresDAO;

import DAO.AstaDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AstaImplementazionePostgresDAO implements AstaDAO {

    private Connection connection;

    public AstaImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void leggiAsteDB(ArrayList<Integer> idAste,
                            ArrayList<String> emailCreatori,
                            ArrayList<String> titoli,
                            ArrayList<String> categorie,
                            ArrayList<Double> prezziPartenza,
                            ArrayList<LocalDateTime> dateScadenza,
                            ArrayList<String> descrizioni,
                            ArrayList<byte[]> immaginiProdotti,
                            ArrayList<Double> ultimeOfferte,
                            ArrayList<Double> soglieMinime,
                            ArrayList<String> tipiAste,
                            ArrayList<String> emailVincitori,
                            ArrayList<Double> costiFinali) {

        try{
            PreparedStatement leggiAsteStatement = connection.prepareStatement("SELECT * FROM \"Aste\"");
            ResultSet rs = leggiAsteStatement.executeQuery();

            while(rs.next()){
                idAste.add(rs.getInt("Id Asta"));
                emailCreatori.add(rs.getString("Email Creatore"));
                titoli.add(rs.getString("Titolo"));
                categorie.add(rs.getString("Categoria"));
                prezziPartenza.add(rs.getDouble("Prezzo di Partenza"));

            }
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }

    @Override
    public void aggiungiAstaDB(int idAsta,
                               String emailCreatore,
                               String titolo,
                               String categoria,
                               double prezzoPartenza,
                               LocalDateTime dataScadenza,
                               String descrizione,
                               byte[] immagineProdotto,
                               double ultimaOfferta,
                               double sogliaMinima,
                               String tipoAsta) {

    }

    @Override
    public void concludiAstaDB(String idAsta, String emailVincitore, double costoFinale) {

    }

    @Override
    public void modificaUltimaOffertaAstaDB(String idAsta, double ultimaOfferta) {

    }
}
