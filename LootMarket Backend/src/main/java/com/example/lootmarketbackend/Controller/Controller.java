package com.example.lootmarketbackend.Controller;

import com.example.lootmarketbackend.Modelli.*;
import com.example.lootmarketbackend.dto.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;

@Service
public class Controller {

    public ControllerUtenti controllerUtenti;
    public ControllerAste controllerAste;
    public ControllerLegami controllerLegami;
    public ControllerNotifiche controllerNotifiche;

    public Controller(){
        controllerUtenti = new ControllerUtenti();
        controllerAste = new ControllerAste();
        controllerLegami = new ControllerLegami();
        controllerNotifiche = new ControllerNotifiche();
        checkAsteScadute();
    }





    public void checkAsteScadute(){
        System.out.println("Controllo scadenza Aste avviato");
        for(int i=0; i<controllerAste.getDatabaseSize(); i++){
            Asta asta = controllerAste.getAstaDatabase(i);
            if((asta.getDataScadenza().isBefore(LocalDateTime.now())) && (!(asta instanceof AstaConclusa))){
                System.out.println("Asta scaduta individuata: "+asta.getTitolo()+" scade il "+asta.getDataScadenza());
                Offerta ultimaOfferta = controllerLegami.getUltimaOffertaLegame(asta.getIdAsta());
                if(ultimaOfferta == null){
                    System.out.println("Ultima offerta non trovata");
                    controllerAste.concludiAstaDAO(asta.getIdAsta(), "", -2);
                }else{
                    if(!(asta instanceof AstaTempoFisso) || ultimaOfferta.getOfferta() >= asta.getSogliaMinima()){
                        System.out.println("Ultima offerta trovata, asta terminita con successo");
                        String emailVincitore =  ultimaOfferta.getEmailUtente();
                        controllerAste.concludiAstaDAO(asta.getIdAsta(), emailVincitore, asta.getUltimaOfferta());
                    }
                    else{
                        System.out.println("Ultima offerta non supera la soglia minima, asta fallita");
                        controllerAste.concludiAstaDAO(asta.getIdAsta(), "", -3);
                    }
                }
                System.out.println("Genero le notifiche");
                generaNotificaAstaScaduta(asta, ultimaOfferta);
            }
        }
        System.out.println("Controllo scadenza Aste terminato!");
    }





    //ritorna -1 se la presentazione dell'offerta non va a buon fine a causa di un errore, -2 offerta non valida per il tipo di asta, 1 se l'offerta viene presentata con successo, 0 se l'asta è conclusa
    public int nuovaOfferta(String email,int idAsta, double nuovaOfferta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        int k = controllerUtenti.getIndiceUtenteByEmail(email);
        if(i!=-1 && k!= -1) {
            int status  = controllerAste.effettuaOfferta(email, i, nuovaOfferta);
            if(status==1){
                int j = controllerLegami.getIndiceLegameByEmailAndIdAsta(email, idAsta);
                generaNotificheNuovaOfferta(idAsta);
                if (j == -1) {
                    controllerLegami.aggiungiLegamiDAO(idAsta, email, nuovaOfferta, LocalDateTime.now());
                }else {
                    controllerLegami.modificaUltimaOffertaLegameDAO(idAsta, email, nuovaOfferta, LocalDateTime.now());
                }
                controllerAste.modificaUltimaOffertaAstaDAO(idAsta, nuovaOfferta);
                return 1;
            }else{
                return status;
            }
        }
        return -1;
    }





    //ritorna -1 se l'iscrizione non va a buon fine, 1 se invece l'iscrizione viene effettuata con successo
    public int iscrizione(String email, int idAsta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        int k = controllerUtenti.getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1){
            controllerLegami.aggiungiLegamiDAO(idAsta, email, -1.0, LocalDateTime.now());
            return 1;
        }
        return -1;
    }





    //ritorna -1 se la disiscrizione non va a buon fine, 1 se invece la disiscrizione viene effettuata con successo
    public int disiscrizione(String email, int idAsta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        int k = controllerUtenti.getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1){
            if(controllerAste.getMailCreatoreAstaByIndice(i).equals(email)){
                return -1;
            }
            int j = controllerLegami.getIndiceLegameByEmailAndIdAsta(email, idAsta);
            if(j != -1){
                if(controllerLegami.getOffertaLegameByIndice(j) == -1.0){
                    controllerLegami.eliminaLegameDAO(idAsta, email);
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





    //recupera le i-esime 10 aste non concluse
    public ArrayList<AstaDTO> recuperaAsteHome(int indice){
        ArrayList<AstaDTO> arrayRitorno = new ArrayList<>();
        int i = -(10*indice);
        int j = 0;
        int indiceAsta = controllerAste.getDatabaseSize()-1;
        while((i < 10) && ((indiceAsta - j) >= 0)){
            Asta asta = controllerAste.getAstaDatabase(indiceAsta - j);
            if(!(asta instanceof AstaConclusa)){
                if(i >= 0){
                    String tipo;
                    if(asta.getSogliaMinima() == 0){
                        tipo = "Asta Inversa";
                    }else{
                        tipo = "Asta a Tempo Fisso";
                    }
                    AstaDTO astaDTO = new AstaDTO(asta.getIdAsta(), asta.getEmailCreatore(), asta.getTitolo(), asta.getCategoria(), asta.getPrezzoPartenza(), asta.getDataScadenza().getYear(), asta.getDataScadenza().getMonthValue(), asta.getDataScadenza().getDayOfMonth(), asta.getDescrizione(), asta.getUltimaOfferta(), asta.getSogliaMinima(), tipo, false, Base64.getEncoder().encodeToString(asta.getImmagineProdotto()));
                    arrayRitorno.add(astaDTO);
                }
                i++;
            }
            j++;
        }
        return arrayRitorno;
    }





    //recupera la specifica asta e costruisce il rispettivo DTO per quello specifico utente
    public DettagliAstaDTO getDettagliAsta(int idAsta, String emailUtente){
        int indice = controllerAste.getIndiceAstaById(idAsta);
        if(indice==-1){
            return null;
        }else{
            Asta astaRitorno = controllerAste.getAstaDatabase(indice);
            Utente utenteAutore = controllerUtenti.getUtenteDatabase(controllerUtenti.getIndiceUtenteByEmail(astaRitorno.getEmailCreatore()));
            String nomeAutore = utenteAutore.getNome() + " " + utenteAutore.getCognome();
            int tipoAstaCodice = controllerAste.getTipoAsta(astaRitorno);
            String tipoAsta;
            switch (tipoAstaCodice){
                case -1: tipoAsta = "Asta Conclusa"; break;
                case 0: tipoAsta = "Asta a Tempo Fisso"; break;
                case 1: tipoAsta = "Asta Inversa"; break;
                default: tipoAsta = "Sconosciuto"; break;
            }
            String statusLegame;
            boolean ultimaOffertaTua = false;
            int j = controllerLegami.getIndiceLegameByEmailAndIdAsta(emailUtente, idAsta);
            if(j == -1){
                statusLegame = "NonIscritto";
            }else{
                Legame legameUtente = controllerLegami.getLegameDatabase(j);
                if(legameUtente instanceof Iscrizione){
                    statusLegame = "Iscritto";
                }else{
                    statusLegame = "OffertaFatta";
                    Offerta ultimaOfferta = controllerLegami.getUltimaOffertaLegame(idAsta);
                    if(ultimaOfferta.getEmailUtente().equals(emailUtente)){
                        ultimaOffertaTua = true;
                    }
                }
            }
            return new DettagliAstaDTO(astaRitorno.getIdAsta(),
                    astaRitorno.getEmailCreatore(),
                    astaRitorno.getTitolo(),
                    astaRitorno.getCategoria(),
                    astaRitorno.getPrezzoPartenza(),
                    astaRitorno.getDataScadenza().getYear(),
                    astaRitorno.getDataScadenza().getMonthValue(),
                    astaRitorno.getDataScadenza().getDayOfMonth(),
                    astaRitorno.getDescrizione(),
                    astaRitorno.getUltimaOfferta(),
                    astaRitorno.getSogliaMinima(),
                    tipoAsta,
                    ultimaOffertaTua,
                    Base64.getEncoder().encodeToString(astaRitorno.getImmagineProdotto()),
                    nomeAutore,
                    statusLegame);
        }
    }





    //recupera le aste relative ad un utente
    public ArrayList<AstaDTO> getAsteByEmailUtente(String email){
        ArrayList<AstaDTO> arrayRitorno = new ArrayList<>();
        for(int i = 0; i < controllerLegami.getDatabaseSize(); i++){
            if(controllerLegami.getMailUtenteLegameByIndice(i).equals(email)){
                Asta asta;
                AstaDTO astaDTO;
                asta = controllerAste.getAstaDatabase(controllerAste.getIndiceAstaById(controllerLegami.getIdAstaLegameByIndice(i)));
                boolean offertaFatta = false;
                String tipo="";
                if(controllerLegami.getOffertaLegameByIndice(i)!=-1){
                    offertaFatta = true;
                }
                if((asta instanceof AstaConclusa)){
                    tipo = "Asta Conclusa";
                }else if(asta instanceof AstaTempoFisso){
                    if(asta instanceof AstaInversa){
                        tipo = "Asta Inversa";
                    }else{
                        tipo = "Asta a Tempo Fisso";
                    }
                }
                astaDTO = new AstaDTO(asta.getIdAsta(), asta.getEmailCreatore(), asta.getTitolo(), asta.getCategoria(), asta.getPrezzoPartenza(), asta.getDataScadenza().getYear(), asta.getDataScadenza().getMonthValue(), asta.getDataScadenza().getDayOfMonth(), asta.getDescrizione(), asta.getUltimaOfferta(), asta.getSogliaMinima(), tipo, offertaFatta, Base64.getEncoder().encodeToString(asta.getImmagineProdotto()));
                arrayRitorno.add(astaDTO);
            }
        }
        return arrayRitorno;
    }





    //ritorna -1 se errore, 1 se l'asta viene creata con successo
    public int creaAsta(String emailCreatore,
                        String titolo,
                        String categoria,
                        double prezzoPartenza,
                        int giorno,
                        int mese,
                        int anno,
                        String descrizione,
                        byte[] immagineProdotto,
                        double ultimaOfferta,
                        double sogliaMinima,
                        String tipoAsta){
        LocalDateTime dataScadenza = LocalDateTime.of(anno, mese, giorno, 23, 59, 59);
        if(controllerAste.aggiungiAstaDAO(emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima, tipoAsta)==1){
            int idAsta = controllerAste.getIdUltimaAsta();
            return iscrizione(emailCreatore, idAsta);
        }else {
            return -1;
        }
    }





    //recupera i propri dati personali di un utente
    public UtenteDTO getDatiUtentePersonali(String mailUtente){
        Utente utente = controllerUtenti.getUtenteByEmail(mailUtente);
        String ragioneSociale;
        String partitaIva;
        String numeroAziendale;
        if((utente instanceof UtenteBusiness)){
            ragioneSociale = ((UtenteBusiness) utente).getRagioneSociale();
            partitaIva = ((UtenteBusiness) utente).getPartitaIva();
            numeroAziendale = ((UtenteBusiness) utente).getNumeroAziendale();
        }else{
            ragioneSociale = "";
            partitaIva = "";
            numeroAziendale = "";
        }
        String immagineProfiloCodificata = Base64.getEncoder().encodeToString(utente.getImmagineProfilo());
        return new UtenteDTO(utente.getNome(), utente.getCodiceFiscale(), utente.getEmail(), utente.getDataNascita().getYear(), utente.getDataNascita().getMonthValue(), utente.getDataNascita().getDayOfMonth(), utente.getNazione(), utente.getNumeroCellulare(), utente.getIndirizzoSpedizione(), utente.getIndirizzoFatturazione(), utente.getContatti().getSitoWeb(), utente.getContatti().getFacebook(), utente.getContatti().getInstagram(), utente.getBiografia(), ragioneSociale, partitaIva, numeroAziendale, immagineProfiloCodificata);
    }





    //recupera i dati publici di un utente
    public UtenteDTO getDatiUtenteTerzi(String mailUtente){
        Utente utente = controllerUtenti.getUtenteByEmail(mailUtente);
        String ragioneSociale;
        String partitaIva;
        String numeroAziendale;
        if((utente instanceof UtenteBusiness)){
            ragioneSociale = ((UtenteBusiness) utente).getRagioneSociale();
            partitaIva = ((UtenteBusiness) utente).getPartitaIva();
            numeroAziendale = ((UtenteBusiness) utente).getNumeroAziendale();
        }else{
            ragioneSociale = "";
            partitaIva = "";
            numeroAziendale = "";
        }
        String immagineProfiloCodificata = Base64.getEncoder().encodeToString(utente.getImmagineProfilo());
        return new UtenteDTO(utente.getNome(), "", utente.getEmail(), 0, 0, 0, utente.getNazione(), "", null, null, utente.getContatti().getSitoWeb(), utente.getContatti().getFacebook(), utente.getContatti().getInstagram(), utente.getBiografia(), ragioneSociale, partitaIva, numeroAziendale, immagineProfiloCodificata);
    }





    //recupera le aste attive publiche di un utente
    public ArrayList<AstaDTO> getAsteByEmailUtenteTerzi(String emailUtente, String emailUtenteTerzi){
        ArrayList<AstaDTO> arrayRitorno = new ArrayList<>();
        for(int i = 0; i < controllerLegami.getDatabaseSize(); i++){
            if(controllerLegami.getMailUtenteLegameByIndice(i).equals(emailUtenteTerzi)){
                Asta asta;
                AstaDTO astaDTO;
                String tipo="";
                Boolean offertaFatta = false;
                asta = controllerAste.getAstaDatabase(controllerAste.getIndiceAstaById(controllerLegami.getIdAstaLegameByIndice(i)));
                if(asta.getEmailCreatore().equals(emailUtenteTerzi) && !(asta instanceof AstaConclusa)){
                    for(int j = 0; j < controllerLegami.getDatabaseSize(); j++){
                        if((controllerLegami.getIdAstaLegameByIndice(j) == asta.getIdAsta()) &&
                                (controllerLegami.getMailUtenteLegameByIndice(j).equals(emailUtente)) &&
                                (controllerLegami.getOffertaLegameByIndice(j)!=-1)){
                            offertaFatta = true;
                            }
                        }
                    astaDTO = new AstaDTO(asta.getIdAsta(), asta.getEmailCreatore(), asta.getTitolo(), asta.getCategoria(), asta.getPrezzoPartenza(), asta.getDataScadenza().getYear(), asta.getDataScadenza().getMonthValue(), asta.getDataScadenza().getDayOfMonth(), asta.getDescrizione(), asta.getUltimaOfferta(), asta.getSogliaMinima(), tipo, offertaFatta, Base64.getEncoder().encodeToString(asta.getImmagineProdotto()));
                    arrayRitorno.add(astaDTO);
                }
            }
        }
        return arrayRitorno;
    }





    //genera le notifiche al seguito di una nuova offerta ad una asta indicata
    public void generaNotificheNuovaOfferta(int idAsta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        if(i != -1){
            String emailCreatore = controllerAste.getMailCreatoreAstaByIndice(i);
            controllerNotifiche.aggiungiNotificaDAO(5, emailCreatore, idAsta);
            Legame legameUltimaOfferta = controllerLegami.getUltimaOffertaLegame(idAsta);
            if(legameUltimaOfferta != null){
                controllerNotifiche.aggiungiNotificaDAO(6,legameUltimaOfferta.getEmailUtente(), idAsta);
            }
        }
    }





    //genera le notifiche al seguito della scadenza di una asta specificata
    public int generaNotificaAstaScaduta(Asta asta, Offerta legameUltimaOfferta){
        //variabile che decreta come l'asta è conclusa: 0 nessuna offerta, 1 nessuna offerta valida, 2 con successo, altrimenti errore: -1
        int successo = 0;
        if(asta == null || asta instanceof AstaConclusa){
            successo = -1;
            return successo;
        }
        if(legameUltimaOfferta != null){
            if((!(asta instanceof AstaTempoFisso) || legameUltimaOfferta.getOfferta() >= asta.getSogliaMinima() )&&(legameUltimaOfferta.getOfferta()>0.0)){
                successo = 2;
            }else{
                successo= 1;
            }
        }
        for(Legame legame: controllerLegami.databaseLegami){
            if(legame.getIdAsta() == asta.getIdAsta()){ //controllo che il legame riguardi l'asta che sta scadendo
                if(!(legame.getEmailUtente().equals(asta.getEmailCreatore()))){ //controllo che il legame non sia del creatore
                    if(((successo!=0) && legame instanceof Offerta legameOfferta )&&(legameOfferta.getOfferta() == legameUltimaOfferta.getOfferta())){ //controllo che il legame sia effettivamene una offerta
                        if(successo==2){
                            controllerNotifiche.aggiungiNotificaDAO(4,legameUltimaOfferta.getEmailUtente(), asta.getIdAsta());
                        }else{
                            controllerNotifiche.aggiungiNotificaDAO(7,legameUltimaOfferta.getEmailUtente(), asta.getIdAsta());
                        }
                    }else{
                        controllerNotifiche.aggiungiNotificaDAO(3,legame.getEmailUtente(), asta.getIdAsta());
                    }
                }
            }
        }
        if(successo==2){
            controllerNotifiche.aggiungiNotificaDAO(2, asta.getEmailCreatore(), asta.getIdAsta());
        }else if(successo==1){
            controllerNotifiche.aggiungiNotificaDAO(8, asta.getEmailCreatore(), asta.getIdAsta());
        }else{
            controllerNotifiche.aggiungiNotificaDAO(1, asta.getEmailCreatore(), asta.getIdAsta());
        }
        return successo;
    }





    public ArrayList<NotificaDTO> getNotificheByEmailUtente(String email){
        ArrayList<NotificaDTO> arrayRitorno = new ArrayList<>();
        for(int i = 0; i < controllerNotifiche.getDatabaseSize(); i++){
            if(controllerNotifiche.getNotificaDatabase(i).getDestinatario().equals(email)){
                Notifica notifica = controllerNotifiche.getNotificaDatabase(i);
                Asta asta = controllerAste.getAstaDatabase(controllerAste.getIndiceAstaById(notifica.getIdAsta()));
                NotificaDTO notificaDTO = new NotificaDTO(notifica.getId(), notifica.getTipo(), notifica.getDestinatario(), notifica.getIdAsta(), asta.getTitolo());
                arrayRitorno.add(notificaDTO);
            }
        }
        return arrayRitorno;
    }





    public int eliminaNotifica(int idNotifica){
        return controllerNotifiche.eliminaNotificaDAO(idNotifica);
    }





    public int modificaUtente(String email, String nazione, String numeroCellulare, String sito, String socialFacebook, String socialInstagram, String biografia, byte[] immagineProfilo, Indirizzo indirizzoFatturazione, Indirizzo indirizzoSpedizione, String numeroAziendale){
        return controllerUtenti.modificaUtenteDAO(email, nazione, numeroCellulare, sito, socialFacebook, socialInstagram, biografia, immagineProfilo, indirizzoFatturazione, indirizzoSpedizione,numeroAziendale);
    }

    public int upgradeUtente(String email, String ragioneSociale, String partitaIva, String numeroAziendale){
        return  controllerUtenti.upgradeUtenteDAO(email, ragioneSociale, partitaIva, numeroAziendale);
    }

    public int registraUtente(UtenteAutenticazioneDTO utenteAutenticazioneDTO){
        byte[] immagineProfilo = null;
        try {
            immagineProfilo = Files.readAllBytes(Paths.get("placeholder.png"));
        } catch (IOException e) {
            System.out.println("Errore nella lettura della immagine");
            return 0;
        }
        Contatti contatti = new Contatti("", "", "");
        Indirizzo indirizzoVuoto = new Indirizzo("", "", "", "");
        LocalDate dataNascita = LocalDate.of(utenteAutenticazioneDTO.dataDiNascitaAnno, utenteAutenticazioneDTO.dataDiNascitaMese, utenteAutenticazioneDTO.dataDiNascitaGiorno);
        return controllerUtenti.aggiungiUtenteDAO(utenteAutenticazioneDTO.mail, utenteAutenticazioneDTO.password, utenteAutenticazioneDTO.nome, utenteAutenticazioneDTO.cognome, utenteAutenticazioneDTO.codiceFiscale, utenteAutenticazioneDTO.nazione, utenteAutenticazioneDTO.numeroCellulare, dataNascita, contatti, "", immagineProfilo, indirizzoVuoto, indirizzoVuoto);
    }



    // ritorna: 0 dati utente non corretti, 1 utente base, 2 utenteBusiness
    public int accediUtente(UtenteAutenticazioneDTO utenteAutenticazioneDTO) {
        int status = controllerUtenti.verificaUtente(utenteAutenticazioneDTO.mail, utenteAutenticazioneDTO.password);
        if (status == 1) { //Utente verificato con successo
            Boolean isBusiness = false;
            if (controllerUtenti.getUtenteByEmail(utenteAutenticazioneDTO.mail) instanceof UtenteBusiness) {
                return 2;
            } else {
                return 1;
            }

        } else {
            return 0;
        }
    }
}