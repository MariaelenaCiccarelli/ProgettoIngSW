package ImplementazionePostgresDAO;

import DAO.UtenteDAO;
import Database.ConnessioneDatabase;
import Modelli.Contatti;
import Modelli.Indirizzo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class UtenteImplementazionePostgresDAO implements UtenteDAO {

    private Connection connection;

    public UtenteImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void leggiUtentiDB(ArrayList<String> emails,
                              ArrayList<String> passwords,
                              ArrayList<String> nomi,
                              ArrayList<String> cognomi,
                              ArrayList<String> codiciFiscali,
                              ArrayList<String> nazioni,
                              ArrayList<String> numeriCellulare,
                              ArrayList<LocalDate> dateNascita,
                              ArrayList<Contatti> contatti,
                              ArrayList<String> biografie,
                              ArrayList<byte[]> immaginiProfilo,
                              ArrayList<Indirizzo> indirizziFatturazione,
                              ArrayList<Indirizzo> indirizziSpedizione,
                              ArrayList<String> ragioniSociali,
                              ArrayList<String> partiteIva,
                              ArrayList<String> numeriAziendali) {
        try{
            PreparedStatement leggiUtentiStatement = connection.prepareStatement("SELECT * FROM \"Utenti\"");
            ResultSet rs = leggiUtentiStatement.executeQuery();

            while(rs.next()){
                emails.add(rs.getString("Email"));
                passwords.add(rs.getString("Password"));
                nomi.add((rs.getString("Nome")));
                cognomi.add(rs.getString("Cognome"));
                codiciFiscali.add((rs.getString("Codice Fiscale")));
                nazioni.add(rs.getString("Nazione"));
                numeriCellulare.add(rs.getString("Numero di Cellulare"));
                dateNascita.add(LocalDate.of(
                        rs.getDate("Data di Nascita").toLocalDate().getYear(),
                        rs.getDate("Data di Nascita").toLocalDate().getMonth(),
                        rs.getDate("Data di Nascita").toLocalDate().getDayOfMonth()));
                contatti.add(new Contatti(rs.getString("Sito Web"), rs.getString("Facebook"), rs.getString("Instagram")));
                biografie.add(rs.getString("Biografia"));
                immaginiProfilo.add(rs.getBytes("Immagine del Profilo"));
                indirizziSpedizione.add(new Indirizzo(rs.getString("Via Spedizione"), rs.getString("Città Spedizione"), rs.getString("Provincia Spedizione"), rs.getString("CAP Spedizione")));
                indirizziFatturazione.add(new Indirizzo(rs.getString("Via Fatturazione"), rs.getString("Città Fatturazione"), rs.getString("Provincia Fatturazione"), rs.getString("CAP Fatturazione")));
                ragioniSociali.add(rs.getString("Ragione Sociale"));
                partiteIva.add(rs.getString("Partita Iva"));
                numeriAziendali.add(rs.getString("Numero Aziendale"));
            }

        }catch(Exception e){
            System.out.println("Errore: " + e.getMessage());
        }
    }

    @Override
    public void aggiungiUtenteDB(String email, String password, String nome, String cognome, String codiceFiscale, String nazione, String numeroCellulare, LocalDate dataNascita, Contatti contatti, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione, String ragioneSociale, String partitaIva, String numeroAziendale) {
        try {


            PreparedStatement addUtenteStatement = connection.prepareStatement("INSERT INTO \"Utenti\" " +"(\"Email\", \"Password\", \"Nome\", \"Cognome\", " +
                    "\"Codice Fiscale\", \"Nazione\", \"Numero di Cellulare\", \"Data di Nascita\", \"Sito Web\", \"Facebook\", \"Instagram\" , \"Biografia\", " +
                    "\"Immagine del Profilo\", \"Via Spedizione\", \"Città Spedizione\", \"Provincia Spedizione\", \"CAP Spedizione\", \"Via Fatturazione\", " +
                    "\"Città Fatturazione\", \"Provincia Fatturazione\", \"CAP Fatturazione\", \"Ragione Sociale\", \"Partita Iva\", \"Numero Aziendale\")" + "VALUES(" +
                    "'"+email+"'," +
                    "'"+password+"', " +
                    "'"+nome+"'," +
                    "'"+cognome+"', " +
                    "'"+codiceFiscale+"', " +
                    "'"+nazione+"', " +
                    "'"+numeroCellulare+"', " +
                    "'"+dataNascita+"', " +
                    "'"+contatti.getSitoWeb()+"', " +
                    "'"+contatti.getFacebook()+"', " +
                    "'"+contatti.getInstagram()+"', " +
                    "'"+biografia+"', " +
                    "'"+immagineProfilo+"', " +
                    "'"+indirizzoSpedizione.getVia()+"', " +
                    "'"+indirizzoSpedizione.getCitta()+"', " +
                    "'"+indirizzoSpedizione.getProvincia()+"', "+
                    "'"+indirizzoSpedizione.getCAP()+"',"+
                    "'"+indirizzoFatturazione.getVia()+"', " +
                    "'"+indirizzoFatturazione.getCitta()+"', " +
                    "'"+indirizzoFatturazione.getProvincia()+"', "+
                    "'"+indirizzoFatturazione.getCAP()+"',"+
                    "'"+ragioneSociale+"',"+
                    "'"+partitaIva+"',"+
                    "'"+numeroAziendale+"');");
            addUtenteStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    @Override
    public void modificaUtenteDB(String email, String nazione, String numeroCellulare, Contatti contatti, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione, String numeroAziendale) {
        try {


            PreparedStatement updateUtenteStatement = connection.prepareStatement("UPDATE \"Utenti\" SET " +
                    "\"Nazione\" = ?, \"Numero di Cellulare\" = ?, \"Sito Web\" = ?, \"Facebook\" = ?, \"Instagram\" = ?, \"Biografia\" = ?, \"Immagine del Profilo\" = ?, \"Via Spedizione\"= ?, \"Città Spedizione\"= ?, \"Provincia Spedizione\"= ?, \"CAP Spedizione\"=?, \"Via Fatturazione\"= ?, \"Città Fatturazione\"= ?, \"Provincia Fatturazione\"= ?, \"CAP Fatturazione\"= ?, \"Numero Aziendale\" = ? WHERE \"Email\" = ?");
            updateUtenteStatement.setString(1, nazione);
            updateUtenteStatement.setString(2, numeroCellulare);
            updateUtenteStatement.setString(3, contatti.getSitoWeb());
            updateUtenteStatement.setString(4, contatti.getFacebook());
            updateUtenteStatement.setString(5, contatti.getInstagram());
            updateUtenteStatement.setString(6, biografia);
            updateUtenteStatement.setBytes(7, immagineProfilo);
            updateUtenteStatement.setString(8, indirizzoSpedizione.getVia());
            updateUtenteStatement.setString(9, indirizzoSpedizione.getCitta());
            updateUtenteStatement.setString(10, indirizzoSpedizione.getProvincia());
            updateUtenteStatement.setString(11, indirizzoSpedizione.getCAP());
            updateUtenteStatement.setString(12, indirizzoFatturazione.getVia());
            updateUtenteStatement.setString(13, indirizzoFatturazione.getCitta());
            updateUtenteStatement.setString(14, indirizzoFatturazione.getProvincia());
            updateUtenteStatement.setString(15, indirizzoFatturazione.getCAP());
            updateUtenteStatement.setString(16, numeroAziendale);
            updateUtenteStatement.setString(17, email);
            updateUtenteStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    @Override
    public void upgradeUtenteDB(String email, String ragioneSociale, String partitaIva, String numeroAziendale) {
        try {
            PreparedStatement upgradeUtenteStatemente = connection.prepareStatement("UPDATE \"Utenti\" SET " +
                    "\"Ragione Sociale\"='"+ragioneSociale+"', \"Partita Iva\" ='"+partitaIva+"', \"Numero Aziendale\" ='"+numeroAziendale+"' WHERE \"Email\" ='"+email+"'");

            upgradeUtenteStatemente.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}