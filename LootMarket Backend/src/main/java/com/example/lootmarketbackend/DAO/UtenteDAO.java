package com.example.lootmarketbackend.DAO;

import com.example.lootmarketbackend.Modelli.Contatti;
import com.example.lootmarketbackend.Modelli.Indirizzo;

import java.time.LocalDate;
import java.util.ArrayList;

public interface UtenteDAO {

    void leggiUtentiDB(ArrayList<String> emails,
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
                       ArrayList<String> numeriAziendali);


    int verificaUtenteDB(String mailUtente, String passwordUtente);

    int aggiungiUtenteDB(String email,
                          String password,
                          String nome,
                          String cognome,
                          String codiceFiscale,
                          String nazione,
                          String numeroCellulare,
                          LocalDate dataNascita,
                          Contatti contatti,
                          String biografia,
                          byte[] immagineProfilo,
                          Indirizzo indirizzoFatturazione,
                          Indirizzo indirizzoSpedizione,
                          String ragioneSociale,
                          String partitaIva,
                          String numeroAziendale);

    void modificaUtenteDB(String email,
                          String nazione,
                          String numeroCellulare,
                          Contatti contatti,
                          String biografia,
                          byte[] immagineProfilo,
                          Indirizzo indirizzoFatturazione,
                          Indirizzo indirizzoSpedizione,
                          String numeroAziendale);

    void upgradeUtenteDB(String email,
                         String ragioneSociale,
                         String partitaIva,
                         String numeroAziendale);
}
