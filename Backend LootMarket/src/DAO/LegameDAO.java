package DAO;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface LegameDAO {

    void leggiLegamiDB(ArrayList<Integer> idAste,
                       ArrayList<String> emailUtenti,
                       ArrayList<Double> offerte,
                       ArrayList<LocalDateTime> timestamp);

    void aggiungiLegameDB(int idAsta,
                          String emailUtente,
                          double offerta,
                          LocalDateTime timestamp);

    void eliminaLegameDB(int idAsta,
                         String emailUtente);

    void modificaUltimaOffertaLegameDB(int idAsta,
                                       String emailUtente,
                                       double offerta,
                                       LocalDateTime timestamp);
}