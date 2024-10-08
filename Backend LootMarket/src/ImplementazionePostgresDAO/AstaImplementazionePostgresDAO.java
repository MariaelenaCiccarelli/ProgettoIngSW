package ImplementazionePostgresDAO;

import DAO.AstaDAO;
import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                dateScadenza.add(LocalDateTime.of(rs.getDate("Data di Scadenza").toLocalDate(), LocalTime.of(23,59, 59)));
                descrizioni.add(rs.getString("Descrizione"));
                immaginiProdotti.add(rs.getBytes("Immagine Prodotto"));
                ultimeOfferte.add(rs.getDouble("Ultima Offerta"));
                soglieMinime.add(rs.getDouble("Soglia Minima"));
                tipiAste.add(rs.getString("Tipo di Asta"));
                emailVincitori.add(rs.getString("Email Vincitore"));
                costiFinali.add(rs.getDouble("Costo Finale"));
            }
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }

    @Override
    public void aggiungiAstaDB(String emailCreatore,
                               String titolo,
                               String categoria,
                               double prezzoPartenza,
                               LocalDateTime dataScadenza,
                               String descrizione,
                               byte[] immagineProdotto,
                               double ultimaOfferta,
                               double sogliaMinima,
                               String tipoAsta) {

        try{
            String stringa = new String("");
            PreparedStatement aggiungiAstaStatement = connection.prepareStatement("INSERT INTO \"Aste\"" + "(\"Email Creatore\", \"Titolo\", \"Categoria\", " +
                    "\"Prezzo di Partenza\", \"Data di Scadenza\", \"Descrizione\", \"Immagine Prodotto\", \"Ultima Offerta\", \"Soglia Minima\", \"Tipo di Asta\" , \"Email Vincitore\", " +
                    "\"Costo Finale\")" + "VALUES('"+emailCreatore+"', '"+titolo+"','"+categoria+"', '"+prezzoPartenza+"', '"+dataScadenza+"', '"+descrizione+"', '"+immagineProdotto+"', '"+ultimaOfferta+"', '"+sogliaMinima+"', '"+tipoAsta+"', '"+stringa+"', '"+-1+"');");
            aggiungiAstaStatement.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }

    @Override
    public void concludiAstaDB(int idAsta, String emailVincitore, double costoFinale) {

        try{
            PreparedStatement concludiAstaStatement = connection.prepareStatement("UPDATE \"Aste\" SET \"Email Vincitore\" = ? , \"Costo Finale\" = ?, \"Tipo di Asta\" = ? WHERE \"Id Asta\" = ?");
            concludiAstaStatement.setString(1, emailVincitore);
            concludiAstaStatement.setDouble(2, costoFinale);
            concludiAstaStatement.setString(3, "Asta Conclusa");
            concludiAstaStatement.setInt(4, idAsta);
            concludiAstaStatement.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }

    @Override
    public void modificaUltimaOffertaAstaDB(int idAsta, double ultimaOfferta) {

        try{
            PreparedStatement modificaUltimaOffertaStatement = connection.prepareStatement("UPDATE \"Aste\" SET \"Ultima Offerta\" = ? WHERE \"Id Asta\" = ?");
            modificaUltimaOffertaStatement.setDouble(1, ultimaOfferta);
            modificaUltimaOffertaStatement.setInt(2, idAsta);
            modificaUltimaOffertaStatement.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }

    }
}
