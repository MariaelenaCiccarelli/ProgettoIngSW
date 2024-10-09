package Controller;

import DAO.UtenteDAO;
import ImplementazionePostgresDAO.UtenteImplementazionePostgresDAO;
import Modelli.Contatti;
import Modelli.Indirizzo;
import Modelli.Utente;
import Modelli.UtenteBusiness;

import java.time.LocalDate;
import java.util.ArrayList;

public class ControllerUtenti {

    public ArrayList<Utente> databaseUtenti;

    public ControllerUtenti(){
        databaseUtenti = new ArrayList<>();
        leggiUtentiDAO();
    }

    public int getDatabaseSize(){
        return databaseUtenti.size();
    }

    public Utente getUtenteDatabase(int indice){
        return databaseUtenti.get(indice);
    }

    public void leggiUtentiDAO(){
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<String> passwords = new ArrayList<>();
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

            if(numeriAziendali.get(i).isEmpty()){
                utente = new Utente(emails.get(i), passwords.get(i), nomi.get(i), cognomi.get(i), codiciFiscali.get(i), nazioni.get(i), numeriCellulare.get(i), dateNascita.get(i), contatti.get(i), immaginiProfilo.get(i), biografie.get(i), indirizziFatturazione.get(i), indirizziSpedizione.get(i));
            }else{
                utente = new UtenteBusiness(emails.get(i), passwords.get(i), nomi.get(i), cognomi.get(i), codiciFiscali.get(i), nazioni.get(i), numeriCellulare.get(i), dateNascita.get(i), contatti.get(i), immaginiProfilo.get(i), biografie.get(i), indirizziFatturazione.get(i), indirizziSpedizione.get(i), ragioniSociali.get(i), partiteIva.get(i), numeriAziendali.get(i));
            }

            databaseUtenti.add(utente);
        }
    }

    public void aggiungiUtenteDAO(String email, String password, String nome, String cognome, String codiceFiscale, String nazione, String numeroCellulare, LocalDate dataNascita, Contatti contatti, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione){
        Utente utente = new Utente(email, password, nome, cognome, codiceFiscale, nazione, numeroCellulare, dataNascita, contatti, immagineProfilo , biografia, indirizzoFatturazione, indirizzoSpedizione);
        databaseUtenti.add(utente);
        UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
        utenteDAO.aggiungiUtenteDB(email, password, nome, cognome, codiceFiscale, nazione, numeroCellulare, dataNascita, contatti, biografia, immagineProfilo, indirizzoFatturazione, indirizzoSpedizione, "", "", "");
    }

    public void modificaUtenteDAO(String email, String nazione, String numeroCellulare, Contatti contatti, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione, String numeroAziendale){
        int i = getIndiceUtenteByEmail(email);
        if(i != -1){
            databaseUtenti.get(i).setNazione(nazione);
            databaseUtenti.get(i).setNumeroCellulare(numeroCellulare);
            databaseUtenti.get(i).setContatti(contatti);
            databaseUtenti.get(i).setBiografia(biografia);
            databaseUtenti.get(i).setImmagineProfilo(immagineProfilo);
            databaseUtenti.get(i).setIndirizzoFatturazione(indirizzoFatturazione);
            databaseUtenti.get(i).setIndirizzoSpedizione(indirizzoSpedizione);
            if(databaseUtenti.get(i) instanceof UtenteBusiness utenteBusiness){
                utenteBusiness.setNumeroAziendale(numeroAziendale);
            }
            UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
            utenteDAO.modificaUtenteDB(email, nazione, numeroCellulare, contatti, biografia, immagineProfilo, indirizzoFatturazione, indirizzoSpedizione, numeroAziendale);
        }
    }

    public void upgradeUtenteDAO(String email, String ragioneSociale, String partitaIva, String numeroAziendale){
        int i = getIndiceUtenteByEmail(email);
        if(i != -1){
            UtenteBusiness utenteBusiness = new UtenteBusiness(databaseUtenti.get(i), ragioneSociale, partitaIva, numeroAziendale);
            databaseUtenti.remove(i);
            databaseUtenti.add(utenteBusiness);
            UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
            utenteDAO.upgradeUtenteDB(email, ragioneSociale, partitaIva, numeroAziendale);
        }
    }

    //ritorna -1 se asta non presente
    public int getIndiceUtenteByEmail(String email) {
        int i = 0;
        while(!email.equals(databaseUtenti.get(i).getEmail()) && i < databaseUtenti.size()){
            i++;
        }
        if(email.equals(databaseUtenti.get(i).getEmail())){
            return i;
        }
        return -1;
    }

    public Utente getUtenteByEmail(String email){
        int i = getIndiceUtenteByEmail(email);
        if(i != -1){
            return databaseUtenti.get(i);
        }else{
            return null;
        }
    }
}
