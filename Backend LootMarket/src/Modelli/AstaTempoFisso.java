package Modelli;

import java.time.LocalDateTime;

public class AstaTempoFisso implements Asta {

    //ATTRIBUTI
    private int idAsta;
    private String emailCreatore;
    private String titolo;
    private String categoria;
    private double prezzoPartenza;
    private LocalDateTime dataScadenza;
    private String descrizione;
    private byte[] immagineProdotto;
    private double ultimaOfferta;
    private double sogliaMinima;

    public AstaTempoFisso(int idAsta,
                          String emailCreatore,
                          String titolo,
                          String categoria,
                          double prezzoPartenza,
                          LocalDateTime dataScadenza,
                          String descrizione,
                          byte[] immagineProdotto,
                          double ultimaOfferta,
                          double sogliaMinima) {

        this.idAsta = idAsta;
        this.emailCreatore = emailCreatore;
        this.titolo = titolo;
        this.categoria = categoria;
        this.prezzoPartenza = prezzoPartenza;
        this.dataScadenza = dataScadenza;
        this.descrizione = descrizione;
        this.immagineProdotto = immagineProdotto;
        this.ultimaOfferta = ultimaOfferta;
        this.sogliaMinima = sogliaMinima;
    }
}
