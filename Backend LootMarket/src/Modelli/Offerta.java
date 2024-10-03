package Modelli;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Offerta implements Legame {

    //ATTRIBUTI
    private double offerta;
    private LocalDateTime timestamp;
    String emailUtente;
    int idAsta;

    public Offerta(String emailUtente, int IdAsta, double offerta, LocalDate data, LocalTime ora) {
        this.emailUtente = emailUtente;
        this.idAsta = IdAsta;
        this.offerta = offerta;
        this.timestamp = LocalDateTime.of(data, ora);
    }
}
