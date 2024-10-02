package DAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public interface LegameDAO {

    void leggiLegamiDB(ArrayList<Integer> idAste,
                       ArrayList<String> emailOfferenti,
                       ArrayList<Double> offerte,
                       ArrayList<LocalDate> date,
                       ArrayList<LocalTime> ore);

    void aggiungiLegameDB(int idAsta,
                          String emailOfferente,
                          double offerta,
                          LocalDateTime timestamp);

    void eliminaLegameDB(int idAsta,
                         String emailOfferente);

    void modificaUltimaOffertaLegameDB(int idAsta,
                                       String emailOfferente,
                                       double offerta,
                                       LocalDateTime timestamp);
}