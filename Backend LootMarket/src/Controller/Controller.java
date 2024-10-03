package Controller;

import DAO.AstaDAO;
import DAO.LegameDAO;
import DAO.UtenteDAO;
import ImplementazionePostgresDAO.AstaImplementazionePostgresDAO;
import ImplementazionePostgresDAO.LegameImplementazionePostgresDAO;
import ImplementazionePostgresDAO.UtenteImplementazionePostgresDAO;
import Modelli.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Controller {

    public ArrayList<Utente> databaseUtenti;
    public ArrayList<Asta> databaseAste;
    public ArrayList<Legame> databaseLegami;

    public Controller(){
        databaseUtenti = new ArrayList<>();
        databaseAste = new ArrayList<>();
        databaseLegami = new ArrayList<>();
        leggiDB();
    }

    //LEGAMI
    private void leggiLegamiDAO(){
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

    private void aggiungiLegamiDAO(int idAsta, String emailOfferente, double offerta, LocalDateTime timestamp){
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

    private void eliminaLegameDAO(int idAsta, String emailOfferente){
        int i = 0;
        while((idAsta != databaseLegami.get(i).getIdAsta() && emailOfferente != databaseLegami.get(i).getEmailUtente()) && i < databaseLegami.size()){
            databaseLegami.remove(i);
            i++;
        }
        LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
        legameDAO.eliminaLegameDB(idAsta, emailOfferente);
    }

    private void modificaUltimaOffertaLegameDAO(int idAsta, String emailOfferente, double offerta, LocalDateTime timestamp){
        int i = 0;
        while((idAsta != databaseLegami.get(i).getIdAsta() && emailOfferente != databaseLegami.get(i).getEmailUtente()) && i < databaseLegami.size()){
            databaseLegami.get(i).setOfferta(offerta);
            databaseLegami.get(i).setTimestamp(timestamp);
            i++;
        }
        LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
        legameDAO.modificaUltimaOffertaLegameDB(idAsta, emailOfferente, offerta, timestamp);
    }

    //ASTE
    private void leggiAsteDAO(){
        ArrayList<Integer> idAste = new ArrayList<>();
        ArrayList<String> emailCreatori = new ArrayList<>();
        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<String> categorie = new ArrayList<>();
        ArrayList<Double> prezziPartenza = new ArrayList<>();
        ArrayList<LocalDateTime> dateScadenza = new ArrayList<>();
        ArrayList<String> descrizioni = new ArrayList<>();
        ArrayList<byte[]> immaginiProdotti = new ArrayList<>();
        ArrayList<Double> ultimeOfferte = new ArrayList<>();
        ArrayList<Double> soglieMinime = new ArrayList<>();
        ArrayList<String> tipiAste = new ArrayList<>();
        ArrayList<String> emailVincitori = new ArrayList<>();
        ArrayList<Double> costiFinali = new ArrayList<>();

        AstaDAO astaDAO = new AstaImplementazionePostgresDAO();

        astaDAO.leggiAsteDB(idAste, emailCreatori, titoli, categorie, prezziPartenza, dateScadenza, descrizioni, immaginiProdotti, ultimeOfferte, soglieMinime, tipiAste, emailVincitori, costiFinali);

        for(int i = 0; i < idAste.size(); i++) {
            Asta asta;

            if (costiFinali.get(i) == -1) {
                if (tipiAste.get(i) == "Asta a Tempo Fisso") {
                    asta = new AstaTempoFisso(idAste.get(i), emailCreatori.get(i), titoli.get(i), categorie.get(i), prezziPartenza.get(i), dateScadenza.get(i), descrizioni.get(i), immaginiProdotti.get(i), ultimeOfferte.get(i), soglieMinime.get(i));
                } else {
                    asta = new AstaInversa(idAste.get(i), emailCreatori.get(i), titoli.get(i), categorie.get(i), prezziPartenza.get(i), dateScadenza.get(i), descrizioni.get(i), immaginiProdotti.get(i), ultimeOfferte.get(i));
                }
            } else {
                asta = new AstaConclusa(idAste.get(i), emailCreatori.get(i), titoli.get(i), categorie.get(i), prezziPartenza.get(i), dateScadenza.get(i), descrizioni.get(i), immaginiProdotti.get(i), emailVincitori.get(i), costiFinali.get(i));
            }

            databaseAste.add(asta);
        }
    }

    private void aggiungiAstaDAO(int idAsta,
                                 String emailCreatore,
                                 String titolo,
                                 String categoria,
                                 double prezzoPartenza,
                                 LocalDateTime dataScadenza,
                                 String descrizione,
                                 byte[] immagineProdotto,
                                 double ultimaOfferta,
                                 double sogliaMinima,
                                 String tipoAsta){
        Asta asta;
        if(tipoAsta.equals("Asta a Tempo Fisso")){
            asta = new AstaTempoFisso(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima);
        }else{
            asta = new AstaInversa(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta);
        }

        databaseAste.add(asta);
        AstaDAO astaDAO = new AstaImplementazionePostgresDAO();
        astaDAO.aggiungiAstaDB(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima, tipoAsta);
    }

    private  void concludiAstaDAO(int idAsta, String emailVincitore, double costoFinale){
        int i = 0;
        while(idAsta != databaseAste.get(i).getIdAsta() && i < databaseAste.size()){
            Asta astaConclusa = new AstaConclusa(idAsta, databaseAste.get(i).getEmailCreatore(), databaseAste.get(i).getTitolo(), databaseAste.get(i).getCategoria(), databaseAste.get(i).getPrezzoPartenza(), databaseAste.get(i).getDataScadenza(), databaseAste.get(i).getDescrizione(), databaseAste.get(i).getImmagineProdotto(), emailVincitore, costoFinale);
            databaseAste.remove(i);
            databaseAste.add(astaConclusa);
        }
        AstaDAO astaDAO = new AstaImplementazionePostgresDAO();
        astaDAO.concludiAstaDB(idAsta, emailVincitore, costoFinale);
    }

    //UTENTI
    private void leggiUtentiDAO(){
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<char[]> passwords = new ArrayList<>();
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> codiciFiscali = new ArrayList<>();
        ArrayList<String> nazioni = new ArrayList<>();
        ArrayList<String> numeriCellulare = new ArrayList<>();
        ArrayList<LocalDate> dateNascita = new ArrayList<>();
        ArrayList<Contatti> contatti = new ArrayList<>();
        ArrayList<String> biografie = new ArrayList<>();
        ArrayList<byte[]> immaginiProfilo = new ArrayList<>();
        ArrayList<Indirizzo> indirizziFatturazione = new ArrayList<>();
        ArrayList<Indirizzo> indirizziSpedizione = new ArrayList<>();
        ArrayList<String> ragioniSociali = new ArrayList<>();
        ArrayList<String> partiteIva = new ArrayList<>();
        ArrayList<String> numeriAziendali = new ArrayList<>();

        UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();

        utenteDAO.leggiUtentiDB(emails, passwords, nomi, cognomi, codiciFiscali, nazioni, numeriCellulare, dateNascita, contatti, biografie, immaginiProfilo, indirizziFatturazione, indirizziSpedizione, ragioniSociali, partiteIva, numeriAziendali);

        for(int i = 0; i < emails.size(); i++){
            Utente utente;

            if(numeriAziendali.get(i).equals("")){
                utente = new Utente(emails.get(i), passwords.get(i), nomi.get(i), cognomi.get(i), codiciFiscali.get(i), nazioni.get(i), numeriCellulare.get(i), dateNascita.get(i), contatti.get(i), immaginiProfilo.get(i), biografie.get(i), indirizziFatturazione.get(i), indirizziSpedizione.get(i));
            }else{
                utente = new UtenteBusiness(emails.get(i), passwords.get(i), nomi.get(i), cognomi.get(i), codiciFiscali.get(i), nazioni.get(i), numeriCellulare.get(i), dateNascita.get(i), contatti.get(i), immaginiProfilo.get(i), biografie.get(i), indirizziFatturazione.get(i), indirizziSpedizione.get(i), ragioniSociali.get(i), partiteIva.get(i), numeriAziendali.get(i));
            }

            databaseUtenti.add(utente);
        }
    }


    private void leggiDB(){
        leggiAsteDAO();
        leggiLegamiDAO();
        leggiUtentiDAO();
    }
}
