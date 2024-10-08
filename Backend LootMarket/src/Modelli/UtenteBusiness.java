package Modelli;

import java.time.LocalDate;

public class UtenteBusiness extends Utente {

    //ATTRIBUTI
    private String ragioneSociale;
    private String partitaIva;
    private String numeroAziendale;

    public UtenteBusiness(String email, String password, String nome, String cognome, String codiceFiscale, String nazione, String numeroCellulare, LocalDate dataNascita, Contatti contatti, byte[] immagineProfilo, String biografia, Indirizzo indirizzoSpedizione, Indirizzo indirizzoFatturazione, String ragioneSociale, String partitaIva, String numeroAziendale) {
        super(email, password, nome, cognome, codiceFiscale, nazione, numeroCellulare, dataNascita, contatti, immagineProfilo, biografia, indirizzoSpedizione, indirizzoFatturazione);
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
        this.numeroAziendale = numeroAziendale;
    }

    public UtenteBusiness(Utente utente, String ragioneSociale, String partitaIva, String numeroAziendale){
        super(utente.getEmail(), utente.getPassword(), utente.getNome(), utente.getCognome(), utente.getCodiceFiscale(), utente.getNazione(), utente.getNumeroCellulare(), utente.getDataNascita(), utente.getContatti(), utente.getImmagineProfilo(), utente.getBiografia(), utente.getIndirizzoSpedizione(), utente.getIndirizzoFatturazione());
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
        this.numeroAziendale = numeroAziendale;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getNumeroAziendale() {
        return numeroAziendale;
    }

    public void setNumeroAziendale(String numeroAziendale) {
        this.numeroAziendale = numeroAziendale;
    }
}
