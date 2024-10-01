package Modelli;

import java.time.LocalDateTime;

public interface Asta {

    //ATTRIBUTI
    int idAsta = 0;
    String emailCreatore = "emailCreatore";
    String titolo = "titolo";
    String categoria = "categoria";
    double prezzoPartenza = 0.00;
    LocalDateTime dataScadenza = null;
    String descrizione = "descrizione";
    byte[] immagineProdotto = new byte[0];

}
