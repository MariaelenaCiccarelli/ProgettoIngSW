package Modelli;

public class Iscrizione implements Legame {
    String emailUtente;
    int idAsta;

    public Iscrizione(String emailUtente, int idAsta) {
        this.emailUtente = emailUtente;
        this.idAsta = idAsta;
    }
}
