package Modelli;

import java.time.LocalDateTime;

public class AstaInversa extends AstaTempoFisso {
    public AstaInversa(int idAsta, String emailCreatore, String titolo, String categoria, double prezzoPartenza, LocalDateTime dataScadenza, String descrizione, byte[] immagineProdotto, double ultimaOfferta) {
        super(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, 0);
    }
}
