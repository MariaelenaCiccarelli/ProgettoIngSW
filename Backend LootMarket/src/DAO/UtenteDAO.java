package DAO;

import Modelli.Contatti;
import Modelli.Indirizzo;

import java.time.LocalDate;
import java.util.ArrayList;

public interface UtenteDAO {

    void leggiUtentiDB(ArrayList<String> emails,
                       ArrayList<char[]> passwords,
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


    void aggiungiUtenteDB(String email,
                          char[] password,
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
