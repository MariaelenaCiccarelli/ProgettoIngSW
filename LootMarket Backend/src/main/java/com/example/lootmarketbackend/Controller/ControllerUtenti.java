package com.example.lootmarketbackend.Controller;

import com.example.lootmarketbackend.DAO.UtenteDAO;
import com.example.lootmarketbackend.ImplementationPostgresDAO.UtenteImplementazionePostgresDAO;
import com.example.lootmarketbackend.Models.*;

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





    //1 Verifica con successo, 0 altrimenti
    public int verificaUtente(String mailUtente, String passwordUtente){

        UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
        int status = utenteDAO.verificaUtenteDB(mailUtente, passwordUtente);

        return status;
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
            if(ragioniSociali.get(i).isEmpty()){
                utente = new Utente(emails.get(i), passwords.get(i), nomi.get(i), cognomi.get(i), codiciFiscali.get(i), nazioni.get(i), numeriCellulare.get(i), dateNascita.get(i), contatti.get(i), immaginiProfilo.get(i), biografie.get(i), indirizziFatturazione.get(i), indirizziSpedizione.get(i));
            }else{
                utente = new UtenteBusiness(emails.get(i), passwords.get(i), nomi.get(i), cognomi.get(i), codiciFiscali.get(i), nazioni.get(i), numeriCellulare.get(i), dateNascita.get(i), contatti.get(i), immaginiProfilo.get(i), biografie.get(i), indirizziFatturazione.get(i), indirizziSpedizione.get(i), ragioniSociali.get(i), partiteIva.get(i), numeriAziendali.get(i));
            }
            databaseUtenti.add(utente);
        }
    }





    public  int aggiungiUtenteDAO(String email, String password, String nome, String cognome, String codiceFiscale, String nazione, String numeroCellulare, LocalDate dataNascita, Contatti contatti, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione){
        if(utenteEsistente(email)){
            return 2;
        }
        Utente utente = new Utente(email, password, nome, cognome, codiceFiscale, nazione, numeroCellulare, dataNascita, contatti, immagineProfilo , biografia, indirizzoFatturazione, indirizzoSpedizione);
        databaseUtenti.add(utente);
        UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
        return utenteDAO.aggiungiUtenteDB(email, password, nome, cognome, codiceFiscale, nazione, numeroCellulare, dataNascita, contatti, biografia, immagineProfilo, indirizzoFatturazione, indirizzoSpedizione, "", "", "");
    }





    public int modificaUtenteDAO(String email, String nazione, String numeroCellulare, String sito, String socialFacebook, String socialInstagram, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione, String numeroAziendale){
        UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
        Contatti contatti = new Contatti(sito, socialFacebook, socialInstagram);
        if(utenteDAO.modificaUtenteDB(email, nazione, numeroCellulare, contatti, biografia, immagineProfilo, indirizzoFatturazione, indirizzoSpedizione, numeroAziendale)==1){
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
                return 1;
            }
        }
        return 0;
    }





    //1: operazione avvenuta con successo, 0 altrimenti
    public int upgradeUtenteDAO(String email, String ragioneSociale, String partitaIva, String numeroAziendale){
        int i = getIndiceUtenteByEmail(email);
        if(i != -1){
            UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
            if(utenteDAO.upgradeUtenteDB(email, ragioneSociale, partitaIva, numeroAziendale)==1){
                UtenteBusiness utenteBusiness = new UtenteBusiness(databaseUtenti.get(i), ragioneSociale, partitaIva, numeroAziendale);
                databaseUtenti.remove(i);
                databaseUtenti.add(utenteBusiness);
                return 1;
            }
        }
        return 0;
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





    public Boolean utenteEsistente(String mail){
        for(Utente utente : databaseUtenti){
            if(utente.getEmail().equals(mail)){
                return true;
            }
        }
        return false;
    }





}