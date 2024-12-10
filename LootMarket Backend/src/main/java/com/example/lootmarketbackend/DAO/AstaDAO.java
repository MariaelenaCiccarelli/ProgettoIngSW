package com.example.lootmarketbackend.DAO;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface AstaDAO {

    void leggiAsteDB(ArrayList<Integer> idAste,
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
                     ArrayList<Double> costiFinali);

    int aggiungiAstaDB(String emailCreatore,
                        String titolo,
                        String categoria,
                        double prezzoPartenza,
                        LocalDateTime dataScadenza,
                        String descrizione,
                        byte[] immagineProdotto,
                        double ultimaOfferta,
                        double sogliaMinima,
                        String tipoAsta);

    void concludiAstaDB(int idAsta, String emailVincitore, double costoFinale);

    void modificaUltimaOffertaAstaDB(int idAsta, double ultimaOfferta);

}