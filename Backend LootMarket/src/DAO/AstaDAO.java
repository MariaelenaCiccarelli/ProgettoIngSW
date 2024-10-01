package DAO;

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

    void aggiungiAstaDB(int idAsta,
                        String emailCreatore,
                        String titolo,
                        String categoria,
                        double prezzoPartenza,
                        LocalDateTime dataScadenza,
                        String descrizione,
                        byte[] immagineProdotto,
                        double ultimaOfferta,
                        double sogliaMinima,
                        String tipoAsta);

    void concludiAstaDB(String idAsta,
                        String emailVincitore,
                        double costoFinale);

    void modificaUltimaOffertaAstaDB(String idAsta,
                                     double ultimaOfferta);

}