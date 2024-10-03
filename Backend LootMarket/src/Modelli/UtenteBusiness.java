package Modelli;

import java.time.LocalDate;

public class UtenteBusiness extends Utente {

    //ATTRIBUTI
    private String ragioneSociale;
    private String partitaIva;
    private String numeroAziendale;

    public UtenteBusiness(String email, char[] password, String nome, String cognome, String codiceFiscale, String nazione, String numeroCellulare, LocalDate dataNascita, Contatti contatti, byte[] immagineProfilo, String biografia, Indirizzo indirizzoSpedizione, Indirizzo indirizzoFatturazione, String ragioneSociale, String partitaIva, String numeroAziendale) {
        super(email, password, nome, cognome, codiceFiscale, nazione, numeroCellulare, dataNascita, contatti, immagineProfilo, biografia, indirizzoSpedizione, indirizzoFatturazione);
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
        this.numeroAziendale = numeroAziendale;
    }
}
