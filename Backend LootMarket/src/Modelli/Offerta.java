package Modelli;

import java.time.LocalDateTime;

public class Offerta implements Legame {

    //ATTRIBUTI
    private double offerta;
    private LocalDateTime timestamp;
    String emailUtente;
    int idAsta;

    public Offerta(String emailUtente, int IdAsta, double offerta, LocalDateTime timestamp) {
        this.emailUtente = emailUtente;
        this.idAsta = IdAsta;
        this.offerta = offerta;
        this.timestamp = timestamp;

    }
}
