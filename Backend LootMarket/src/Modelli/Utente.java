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
    private ArrayList<String> contatti;
    private byte[] immagineProfilo;
    private String biografia;
    private Indirizzo indirizzoSpedizione;
    private Indirizzo indirizzoFatturazione;

}
