package Modelli;

import java.time.LocalDateTime;

public class AstaConclusa implements Asta{

    //ATTRIBUTI
    private int idAsta;
    private String emailCreatore;
    private String titolo;
    private String categoria;
    private double prezzoPartenza;
    private LocalDateTime dataScadenza;
    private String descrizione;
    private byte[] immagineProdotto;
    private String emailVincitore;
    private double costoFinale;


    public AstaConclusa(int idAsta, String emailCreatore, String titolo, String categoria, double prezzoPartenza, LocalDateTime dataScadenza, String descrizione, byte[] immagineProdotto, String emailVincitore, double costoFinale) {
        this.idAsta = idAsta;
        this.emailCreatore = emailCreatore;
        this.titolo = titolo;
        this.categoria = categoria;
        this.prezzoPartenza = prezzoPartenza;
        this.dataScadenza = dataScadenza;
        this.descrizione = descrizione;
        this.immagineProdotto = immagineProdotto;
        this.emailVincitore = emailVincitore;
        this.costoFinale = costoFinale;
    }
}
