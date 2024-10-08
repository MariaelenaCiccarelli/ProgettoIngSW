package Controller;

import DAO.AstaDAO;
import DAO.LegameDAO;
import DAO.UtenteDAO;
import ImplementazionePostgresDAO.AstaImplementazionePostgresDAO;
import ImplementazionePostgresDAO.LegameImplementazionePostgresDAO;
import ImplementazionePostgresDAO.UtenteImplementazionePostgresDAO;
import Modelli.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Controller {

    public ArrayList<Utente> databaseUtenti;
    public ArrayList<Asta> databaseAste;
    public ArrayList<Legame> databaseLegami;

    public Controller(){
        databaseUtenti = new ArrayList<>();
        databaseAste = new ArrayList<>();
        databaseLegami = new ArrayList<>();
        leggiDB();
    }

    //LEGAMI
    public void leggiLegamiDAO(){
        ArrayList<Integer> idAste = new ArrayList<>();
        ArrayList<String> emailOfferenti = new ArrayList<>();
        ArrayList<Double> offerte = new ArrayList<>();
        ArrayList<LocalDate> date = new ArrayList<>();
        ArrayList<LocalTime> ore = new ArrayList<>();

        LegameDAO legameDAO = new LegameImplementazionePostgresDAO();

        legameDAO.leggiLegamiDB(idAste, emailOfferenti, offerte, date, ore);

        for(int i = 0; i < idAste.size(); i++){
            Legame legame;

            if(offerte.get(i) == -1){
                legame = new Iscrizione(emailOfferenti.get(i), idAste.get(i));
            }else{
                legame = new Offerta(emailOfferenti.get(i), idAste.get(i), offerte.get(i), date.get(i), ore.get(i));
            }

            databaseLegami.add(legame);
        }
    }

    public void aggiungiLegamiDAO(int idAsta, String emailOfferente, double offerta, LocalDateTime timestamp){
        Legame legame;
        if(offerta == -1){
            legame = new Iscrizione(emailOfferente, idAsta);
        }else{
            legame = new Offerta(emailOfferente, idAsta, offerta, timestamp.toLocalDate(), timestamp.toLocalTime());
        }

        databaseLegami.add(legame);
        LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
        legameDAO.aggiungiLegameDB(idAsta, emailOfferente, offerta, timestamp);
    }

    public void eliminaLegameDAO(int idAsta, String emailOfferente){
        int i = 0;
        while((idAsta != databaseLegami.get(i).getIdAsta() && !emailOfferente.equals(databaseLegami.get(i).getEmailUtente())) && i < databaseLegami.size()){
            databaseLegami.remove(i);
            i++;
        }
        LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
        legameDAO.eliminaLegameDB(idAsta, emailOfferente);
    }

    public void modificaUltimaOffertaLegameDAO(int idAsta, String emailOfferente, double offerta, LocalDateTime timestamp){
        int i = 0;
        while((idAsta != databaseLegami.get(i).getIdAsta() && !emailOfferente.equals(databaseLegami.get(i).getEmailUtente())) && i < databaseLegami.size()){
            databaseLegami.get(i).setOfferta(offerta);
            databaseLegami.get(i).setTimestamp(timestamp);
            i++;
        }
        LegameDAO legameDAO = new LegameImplementazionePostgresDAO();
        legameDAO.modificaUltimaOffertaLegameDB(idAsta, emailOfferente, offerta, timestamp);
    }

    //ASTE
    public void leggiAsteDAO(){
        ArrayList<Integer> idAste = new ArrayList<>();
        ArrayList<String> emailCreatori = new ArrayList<>();
        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<String> categorie = new ArrayList<>();
        ArrayList<Double> prezziPartenza = new ArrayList<>();
        ArrayList<LocalDateTime> dateScadenza = new ArrayList<>();
        ArrayList<String> descrizioni = new ArrayList<>();
        ArrayList<byte[]> immaginiProdotti = new ArrayList<>();
        ArrayList<Double> ultimeOfferte = new ArrayList<>();
        ArrayList<Double> soglieMinime = new ArrayList<>();
        ArrayList<String> tipiAste = new ArrayList<>();
        ArrayList<String> emailVincitori = new ArrayList<>();
        ArrayList<Double> costiFinali = new ArrayList<>();

        AstaDAO astaDAO = new AstaImplementazionePostgresDAO();

        astaDAO.leggiAsteDB(idAste, emailCreatori, titoli, categorie, prezziPartenza, dateScadenza, descrizioni, immaginiProdotti, ultimeOfferte, soglieMinime, tipiAste, emailVincitori, costiFinali);

        for(int i = 0; i < idAste.size(); i++) {
            Asta asta;

            if (costiFinali.get(i) == -1) {
                if (tipiAste.get(i).equals("Asta a Tempo Fisso")) {
                    asta = new AstaTempoFisso(idAste.get(i), emailCreatori.get(i), titoli.get(i), categorie.get(i), prezziPartenza.get(i), dateScadenza.get(i), descrizioni.get(i), immaginiProdotti.get(i), ultimeOfferte.get(i), soglieMinime.get(i));
                } else {
                    asta = new AstaInversa(idAste.get(i), emailCreatori.get(i), titoli.get(i), categorie.get(i), prezziPartenza.get(i), dateScadenza.get(i), descrizioni.get(i), immaginiProdotti.get(i), ultimeOfferte.get(i));
                }
            } else {
                asta = new AstaConclusa(idAste.get(i), emailCreatori.get(i), titoli.get(i), categorie.get(i), prezziPartenza.get(i), dateScadenza.get(i), descrizioni.get(i), immaginiProdotti.get(i), emailVincitori.get(i), costiFinali.get(i));
            }

            databaseAste.add(asta);
        }
    }

    public void aggiungiAstaDAO(String emailCreatore,
                                String titolo,
                                String categoria,
                                double prezzoPartenza,
                                LocalDateTime dataScadenza,
                                String descrizione,
                                byte[] immagineProdotto,
                                double ultimaOfferta,
                                double sogliaMinima,
                                String tipoAsta){
        Asta asta;
        int idAsta = databaseAste.size();
        if(tipoAsta.equals("Asta a Tempo Fisso")){
            asta = new AstaTempoFisso(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima);
        }else{
            asta = new AstaInversa(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta);
        }

        databaseAste.add(asta);
        AstaDAO astaDAO = new AstaImplementazionePostgresDAO();
        astaDAO.aggiungiAstaDB(emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima, tipoAsta);
    }

    //ritorna -1 se errore, 1 se va a buon fine
    public int creaAsta(String emailCreatore,
                        String titolo,
                        String categoria,
                        double prezzoPartenza,
                        LocalDateTime dataScadenza,
                        String descrizione,
                        byte[] immagineProdotto,
                        double ultimaOfferta,
                        double sogliaMinima,
                        String tipoAsta){
        aggiungiAstaDAO(emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima, tipoAsta);
        int idAsta = databaseAste.size()-1;
        return iscrizione(emailCreatore, idAsta);
    }

    public void checkAsteScadute(){
        for(int i = 0; i < databaseAste.size(); i++){
            Asta asta = databaseAste.get(i);
            String emailVincitore;
            if(asta.getDataScadenza().isBefore(LocalDateTime.now())){
                int j = 0;
                while((j < databaseLegami.size()) && (databaseLegami.get(j).getOfferta() != asta.getUltimaOfferta())){
                    j++;
                }
                emailVincitore =  databaseLegami.get(j).getEmailUtente();
                concludiAstaDAO(asta.getIdAsta(), emailVincitore, asta.getUltimaOfferta());
            }
        }
    }

    public void concludiAstaDAO(int idAsta, String emailVincitore, double costoFinale){
        int i = getIndiceAstaById(idAsta);
        if(i != -1) {
            Asta astaConclusa = new AstaConclusa(idAsta, databaseAste.get(i).getEmailCreatore(), databaseAste.get(i).getTitolo(), databaseAste.get(i).getCategoria(), databaseAste.get(i).getPrezzoPartenza(), databaseAste.get(i).getDataScadenza(), databaseAste.get(i).getDescrizione(), databaseAste.get(i).getImmagineProdotto(), emailVincitore, costoFinale);
            databaseAste.remove(i);
            databaseAste.add(astaConclusa);
            AstaDAO astaDAO = new AstaImplementazionePostgresDAO();
            astaDAO.concludiAstaDB(idAsta, emailVincitore, costoFinale);
        }
    }

    public void modificaUltimaOffertaAstaDAO(int idAsta, double ultimaOfferta){
        int i = getIndiceAstaById(idAsta);
        if(i != -1){
            AstaDAO astaDAO = new AstaImplementazionePostgresDAO();
            astaDAO.modificaUltimaOffertaAstaDB(idAsta, ultimaOfferta);
        }
    }

    //ritorna -1 se asta non presente
    public int getIndiceAstaById(int idAsta) {
        int i = 0;
        while(idAsta != databaseAste.get(i).getIdAsta() && i < databaseAste.size()){
            i++;
        }
        if(idAsta == databaseAste.get(i).getIdAsta()){
            return i;
        }
        return -1;
    }

    //UTENTI
    public void leggiUtentiDAO(){
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<String> passwords = new ArrayList<>();
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> codiciFiscali = new ArrayList<>();
        ArrayList<String> nazioni = new ArrayList<>();
        ArrayList<String> numeriCellulare = new ArrayList<>();
        ArrayList<LocalDate> dateNascita = new ArrayList<>();
        ArrayList<Contatti> contatti = new ArrayList<>();
        ArrayList<String> biografie = new ArrayList<>();
        ArrayList<byte[]> immaginiProfilo = new ArrayList<>();
        ArrayList<Indirizzo> indirizziFatturazione = new ArrayList<>();
        ArrayList<Indirizzo> indirizziSpedizione = new ArrayList<>();
        ArrayList<String> ragioniSociali = new ArrayList<>();
        ArrayList<String> partiteIva = new ArrayList<>();
        ArrayList<String> numeriAziendali = new ArrayList<>();

        UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();

        utenteDAO.leggiUtentiDB(emails, passwords, nomi, cognomi, codiciFiscali, nazioni, numeriCellulare, dateNascita, contatti, biografie, immaginiProfilo, indirizziFatturazione, indirizziSpedizione, ragioniSociali, partiteIva, numeriAziendali);

        for(int i = 0; i < emails.size(); i++){
            Utente utente;

            if(numeriAziendali.get(i).isEmpty()){
                utente = new Utente(emails.get(i), passwords.get(i), nomi.get(i), cognomi.get(i), codiciFiscali.get(i), nazioni.get(i), numeriCellulare.get(i), dateNascita.get(i), contatti.get(i), immaginiProfilo.get(i), biografie.get(i), indirizziFatturazione.get(i), indirizziSpedizione.get(i));
            }else{
                utente = new UtenteBusiness(emails.get(i), passwords.get(i), nomi.get(i), cognomi.get(i), codiciFiscali.get(i), nazioni.get(i), numeriCellulare.get(i), dateNascita.get(i), contatti.get(i), immaginiProfilo.get(i), biografie.get(i), indirizziFatturazione.get(i), indirizziSpedizione.get(i), ragioniSociali.get(i), partiteIva.get(i), numeriAziendali.get(i));
            }

            databaseUtenti.add(utente);
        }
    }

    public void aggiungiUtenteDAO(String email, String password, String nome, String cognome, String codiceFiscale, String nazione, String numeroCellulare, LocalDate dataNascita, Contatti contatti, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione){
        Utente utente = new Utente(email, password, nome, cognome, codiceFiscale, nazione, numeroCellulare, dataNascita, contatti, immagineProfilo , biografia, indirizzoFatturazione, indirizzoSpedizione);
        databaseUtenti.add(utente);
        UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
        utenteDAO.aggiungiUtenteDB(email, password, nome, cognome, codiceFiscale, nazione, numeroCellulare, dataNascita, contatti, biografia, immagineProfilo, indirizzoFatturazione, indirizzoSpedizione, "", "", "");
    }

    public void modificaUtenteDAO(String email, String nazione, String numeroCellulare, Contatti contatti, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione, String numeroAziendale){
        int i = getIndiceUtenteByEmail(email);
        if(i != -1){
            databaseUtenti.get(i).setNazione(nazione);
            databaseUtenti.get(i).setNumeroCellulare(numeroCellulare);
            databaseUtenti.get(i).setContatti(contatti);
            databaseUtenti.get(i).setBiografia(biografia);
            databaseUtenti.get(i).setImmagineProfilo(immagineProfilo);
            databaseUtenti.get(i).setIndirizzoFatturazione(indirizzoFatturazione);
            databaseUtenti.get(i).setIndirizzoSpedizione(indirizzoSpedizione);
            if(databaseUtenti.get(i) instanceof UtenteBusiness utenteBusiness){
                utenteBusiness.setNumeroAziendale(numeroAziendale);
            }
            UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
            utenteDAO.modificaUtenteDB(email, nazione, numeroCellulare, contatti, biografia, immagineProfilo, indirizzoFatturazione, indirizzoSpedizione, numeroAziendale);
        }
    }

    public void upgradeUtenteDAO(String email, String ragioneSociale, String partitaIva, String numeroAziendale){
        int i = getIndiceUtenteByEmail(email);
        if(i != -1){
            UtenteBusiness utenteBusiness = new UtenteBusiness(databaseUtenti.get(i), ragioneSociale, partitaIva, numeroAziendale);
            databaseUtenti.remove(i);
            databaseUtenti.add(utenteBusiness);
            UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
            utenteDAO.upgradeUtenteDB(email, ragioneSociale, partitaIva, numeroAziendale);
        }
    }

    //ritorna -1 se asta non presente
    public int getIndiceUtenteByEmail(String email) {
        int i = 0;
        while(!email.equals(databaseUtenti.get(i).getEmail()) && i < databaseUtenti.size()){
            i++;
        }
        if(email.equals(databaseUtenti.get(i).getEmail())){
            return i;
        }
        return -1;
    }

    //ritorna -1 se la presentazione dell'offerta non va a buon fine, 1 se invece va tutto bene
    public int nuovaOfferta(String email, int idAsta, double nuovaOfferta){
        int i = getIndiceAstaById(idAsta);
        int k = getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1) {
            Asta asta = databaseAste.get(i);
            if(asta instanceof AstaTempoFisso astaTempoFisso){
                if (astaTempoFisso.presentaOfferta(nuovaOfferta) == -1) {
                    return -1;
                } else {
                    int j = getIndiceLegameByEmailAndIdAsta(email, idAsta);
                    if (j != -1) {
                        aggiungiLegamiDAO(idAsta, email, nuovaOfferta, LocalDateTime.now());
                    } else {
                        modificaUltimaOffertaLegameDAO(idAsta, email, nuovaOfferta, LocalDateTime.now());
                    }
                    modificaUltimaOffertaAstaDAO(idAsta, nuovaOfferta);
                    return 1;
                }
            }else{
                return -1;
            }
        }
        return -1;
    }

    //ritorna -1 se l'iscrizione non va a buon fine, 1 se invece va tutto bene
    public int iscrizione(String email, int idAsta){
        int i = getIndiceAstaById(idAsta);
        int k = getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1){
            aggiungiLegamiDAO(idAsta, email, -1, LocalDateTime.now());
            return 1;
        }
        return -1;
    }

    //ritorna -1 se la disiscrizione non va a buon fine, 1 se invece va tutto bene
    public int disiscrizione(String email, int idAsta){
        int i = getIndiceAstaById(idAsta);
        int k = getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1){
            int j = getIndiceLegameByEmailAndIdAsta(email, idAsta);
            if(j != -1){
                if(databaseLegami.get(j).getOfferta() == -1){
                    eliminaLegameDAO(idAsta, email);
                    return 1;
                }else{
                    return -1;
                }
            }else{
                return -1;
            }
        }
        return -1;
    }

    public ArrayList<Asta> recuperaAsteHome(int indice){
        ArrayList<Asta> arrayRitorno = new ArrayList<>();
        int i = -(10*indice);
        int j = 0;
        int indiceAsta = databaseAste.size() - 1;
        while((i < 10) && ((indiceAsta - j) > 0)){
            Asta asta = databaseAste.get(indiceAsta - j);
            if(!(asta instanceof AstaConclusa)){
                i++;
                if(i >= 0){
                    arrayRitorno.add(asta);
                }
            }
            j++;
        }
        return arrayRitorno;
    }

    public Asta ottieniDettagliAsta(int idAsta, String emailUtente, String nomeAutore, int tipoAsta, int statusLegame){
        Asta astaRitorno = databaseAste.get(getIndiceAstaById(idAsta));
        Utente utenteAutore = databaseUtenti.get(getIndiceUtenteByEmail(astaRitorno.getEmailCreatore()));
        nomeAutore = utenteAutore.getNome() + " " + utenteAutore.getCognome();
        tipoAsta = getTipoAsta(astaRitorno);

        //statusLegame -1 non è iscritto, 1 iscritto, 2 se ha presentato offerta (include anche iscrizione)
        int j = getIndiceLegameByEmailAndIdAsta(emailUtente, idAsta);
        if(j == -1){
            statusLegame = j;
        }else{
            if(databaseLegami.get(j) instanceof Iscrizione){
                statusLegame = 1;
            }else{
                statusLegame = 2;
            }
        }
        return astaRitorno;
    }

    public Utente getUtenteByEmail(String email){
        int i = getIndiceUtenteByEmail(email);
        if(i != -1){
            return databaseUtenti.get(i);
        }else{
            return null;
        }
    }

    public ArrayList<Asta> getAsteByEmailUtente(String email){
        ArrayList<Asta> arrayRitorno = new ArrayList<>();
        for(int i = 0; i < databaseLegami.size(); i++){
            if(databaseLegami.get(i).getEmailUtente().equals(email)){
                arrayRitorno.add(databaseAste.get(getIndiceAstaById(databaseLegami.get(i).getIdAsta())));
            }
        }
        return arrayRitorno;
    }

    //ritorna 0 se è un'asta a tempo fisso, 1 asta inversa, -1 asta conclusa, 2 se non trova nulla
    public int getTipoAsta(Asta asta){
        if(asta instanceof AstaTempoFisso){
            return 0;
        }else if(asta instanceof AstaInversa){
            return 1;
        }else if(asta instanceof AstaConclusa){
            return -1;
        }else{
            return 2;
        }
    }

    //ritorna -1 se asta non presente
    public int getIndiceLegameByEmailAndIdAsta(String email, int idAsta) {
        int i = 0;
        while(!email.equals(databaseLegami.get(i).getEmailUtente()) && idAsta != databaseLegami.get(i).getIdAsta() && i < databaseLegami.size()){
            i++;
        }
        if(email.equals(databaseLegami.get(i).getEmailUtente()) && idAsta == databaseLegami.get(i).getIdAsta()){
            return i;
        }
        return -1;
    }

    //LETTURA DB
    private void leggiDB(){
        leggiAsteDAO();
        leggiLegamiDAO();
        leggiUtentiDAO();
    }
}
