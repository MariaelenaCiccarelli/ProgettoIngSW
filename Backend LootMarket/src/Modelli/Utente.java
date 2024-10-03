package Modelli;

import java.time.LocalDate;
import java.util.ArrayList;

public class Utente {

    //ATTRIBUTI
    private String email;
    private char[] password;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String nazione;
    private String numeroCellulare;
    private LocalDate dataNascita;
    private Contatti contatti;
    private byte[] immagineProfilo;
    private String biografia;
    private Indirizzo indirizzoSpedizione;
    private Indirizzo indirizzoFatturazione;

    public Utente(String email, char[] password, String nome, String cognome, String codiceFiscale, String nazione, String numeroCellulare, LocalDate dataNascita, Contatti contatti, byte[] immagineProfilo, String biografia, Indirizzo indirizzoSpedizione, Indirizzo indirizzoFatturazione) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.nazione = nazione;
        this.numeroCellulare = numeroCellulare;
        this.dataNascita = dataNascita;
        this.contatti = contatti;
        this.immagineProfilo = immagineProfilo;
        this.biografia = biografia;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.indirizzoFatturazione = indirizzoFatturazione;
    }
}
