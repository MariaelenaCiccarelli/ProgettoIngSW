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

    public int getIdAsta();

    public void setIdAsta(int idAsta);

    public String getEmailCreatore();

    public void setEmailCreatore(String emailCreatore);

    public String getTitolo();

    public void setTitolo(String titolo);

    public String getCategoria();

    public void setCategoria(String categoria);

    public double getPrezzoPartenza();

    public void setPrezzoPartenza(double prezzoPartenza);

    public LocalDateTime getDataScadenza();

    public void setDataScadenza(LocalDateTime dataScadenza);

    public String getDescrizione();

    public void setDescrizione(String descrizione);

    public byte[] getImmagineProdotto();

    public void setImmagineProdotto(byte[] immagineProdotto);

    public double getUltimaOfferta();

    public void setUltimaOfferta(double ultimaOfferta);

    public double getSogliaMinima();

    public void setSogliaMinima(double sogliaMinima);

}
