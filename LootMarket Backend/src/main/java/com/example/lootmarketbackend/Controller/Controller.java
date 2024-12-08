package com.example.lootmarketbackend.Controller;

import com.example.lootmarketbackend.Modelli.*;
import com.example.lootmarketbackend.dto.AstaDTO;
import com.example.lootmarketbackend.dto.DettagliAstaDTO;
import com.example.lootmarketbackend.dto.NotificaDTO;
import com.example.lootmarketbackend.dto.UtenteDTO;
import org.springframework.stereotype.Service;

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
        System.out.println("Controllo scadenza Aste avviato!");
        for(int i=0; i<controllerAste.getDatabaseSize(); i++){
            Asta asta = controllerAste.getAstaDatabase(i);
            if((asta.getDataScadenza().isBefore(LocalDateTime.now())) && (!(asta instanceof AstaConclusa))){
                System.out.println("Asta scaduta individuata: "+asta.getTitolo()+" scade il "+asta.getDataScadenza());
                Offerta ultimaOfferta = controllerLegami.getUltimaOffertaLegame(asta.getIdAsta());
                if(ultimaOfferta == null){
                    System.out.println("Ultima offerta non trovata!");
                    controllerAste.concludiAstaDAO(asta.getIdAsta(), "", -2);
                }else{
                    if(!(asta instanceof AstaTempoFisso) || ultimaOfferta.getOfferta() >= asta.getSogliaMinima()){
                        System.out.println("Ultima offerta trovata, asta terminita con successo!");
                        String emailVincitore =  ultimaOfferta.getEmailUtente();
                        controllerAste.concludiAstaDAO(asta.getIdAsta(), emailVincitore, asta.getUltimaOfferta());
                    }
                    else{
                        System.out.println("Ultima offerta non supera la soglia minima, asta fallita!");
                        controllerAste.concludiAstaDAO(asta.getIdAsta(), "", -3);
                    }

                }
                System.out.println("Genero le notifiche!");
                generaNotificaAstaScaduta(asta, ultimaOfferta);

            }
        }
        System.out.println("Controllo scadenza Aste terminato!");
    }





    //ritorna -1 se la presentazione dell'offerta non va a buon fine a causa di un errore, -2 offerta non valida per il tipo di asta, 1 se invece va tutto bene, 0 se l'asta è conclusa
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





    //ritorna -1 se l'iscrizione non va a buon fine, 1 se invece va tutto bene
    public int iscrizione(String email, int idAsta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        int k = controllerUtenti.getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1){
            controllerLegami.aggiungiLegamiDAO(idAsta, email, -1.0, LocalDateTime.now());
            return 1;
        }
        return -1;
    }





    //ritorna -1 se la disiscrizione non va a buon fine, 1 se invece va tutto bene
    public int disiscrizione(String email, int idAsta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        int k = controllerUtenti.getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1){
            if(controllerAste.getAstaDatabase(i).getEmailCreatore().equals(email)){
                return -1;
            }
            int j = controllerLegami.getIndiceLegameByEmailAndIdAsta(email, idAsta);
            if(j != -1){
                if(controllerLegami.getLegameDatabase(j).getOfferta() == -1.0){
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





    public ArrayList<AstaDTO> getAsteByEmailUtente(String email){
        ArrayList<AstaDTO> arrayRitorno = new ArrayList<>();
        for(int i = 0; i < controllerLegami.getDatabaseSize(); i++){
            if(controllerLegami.getLegameDatabase(i).getEmailUtente().equals(email)){
                Asta asta;
                AstaDTO astaDTO;
                asta = controllerAste.getAstaDatabase(controllerAste.getIndiceAstaById(controllerLegami.getLegameDatabase(i).getIdAsta()));
                Boolean offertaFatta = false;
                String tipo="";
                if(controllerLegami.getLegameDatabase(i).getOfferta()!=-1){
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
        if(controllerAste.aggiungiAstaDAO(emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima, tipoAsta)==1){
            int idAsta = controllerAste.getIdUltimaAsta();
            return iscrizione(emailCreatore, idAsta);
        }else {
            return -1;
        }
    }





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
        System.out.println(utente.getImmagineProfilo());
        String immagineProfiloCodificata = Base64.getEncoder().encodeToString(utente.getImmagineProfilo());
        return new UtenteDTO(utente.getNome(), utente.getCodiceFiscale(), utente.getEmail(), utente.getDataNascita().getYear(), utente.getDataNascita().getMonthValue(), utente.getDataNascita().getDayOfMonth(), utente.getNazione(), utente.getNumeroCellulare(), utente.getIndirizzoSpedizione(), utente.getIndirizzoFatturazione(), utente.getContatti().getSitoWeb(), utente.getContatti().getFacebook(), utente.getContatti().getInstagram(), utente.getBiografia(), ragioneSociale, partitaIva, numeroAziendale, immagineProfiloCodificata);
    }





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
        System.out.println(utente.getImmagineProfilo());
        String immagineProfiloCodificata = Base64.getEncoder().encodeToString(utente.getImmagineProfilo());
        return new UtenteDTO(utente.getNome(), "", utente.getEmail(), 0, 0, 0, utente.getNazione(), "", null, null, utente.getContatti().getSitoWeb(), utente.getContatti().getFacebook(), utente.getContatti().getInstagram(), utente.getBiografia(), ragioneSociale, partitaIva, numeroAziendale, immagineProfiloCodificata);
    }





    public ArrayList<AstaDTO> getAsteByEmailUtenteTerzi(String emailUtente, String emailUtenteTerzi){
        ArrayList<AstaDTO> arrayRitorno = new ArrayList<>();
        for(int i = 0; i < controllerLegami.getDatabaseSize(); i++){
            if(controllerLegami.getLegameDatabase(i).getEmailUtente().equals(emailUtenteTerzi)){
                Asta asta;
                AstaDTO astaDTO;
                String tipo="";
                Boolean offertaFatta = false;
                asta = controllerAste.getAstaDatabase(controllerAste.getIndiceAstaById(controllerLegami.getLegameDatabase(i).getIdAsta()));
                if(asta.getEmailCreatore().equals(emailUtenteTerzi) && !(asta instanceof AstaConclusa)){
                    for(int j = 0; j < controllerLegami.getDatabaseSize(); j++){
                        System.out.print(controllerLegami.getLegameDatabase(j).getIdAsta()+" vs "+ asta.getIdAsta());
                        System.out.print(controllerLegami.getLegameDatabase(j).getEmailUtente()+" vs "+ emailUtente);
                        System.out.println(controllerLegami.getLegameDatabase(j).getOfferta());
                        if((controllerLegami.getLegameDatabase(j).getIdAsta() == asta.getIdAsta()) &&
                                (controllerLegami.getLegameDatabase(j).getEmailUtente().equals(emailUtente)) &&
                                (controllerLegami.getLegameDatabase(j).getOfferta()!=-1)){
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





    public void generaNotificheNuovaOfferta(int idAsta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        if(i != -1){
            String emailCreatore = controllerAste.getAstaDatabase(i).getEmailCreatore();
            controllerNotifiche.aggiungiNotificaDAO(5, emailCreatore, idAsta);

            Legame legameUltimaOfferta = controllerLegami.getUltimaOffertaLegame(idAsta);
            if(legameUltimaOfferta != null){
                controllerNotifiche.aggiungiNotificaDAO(6,legameUltimaOfferta.getEmailUtente(), idAsta);
            }
        }
    }





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
        //scorro il database dei legami
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
                System.out.println(asta.getTitolo());

                NotificaDTO notificaDTO = new NotificaDTO(notifica.getId(), notifica.getTipo(), notifica.getDestinatario(), notifica.getIdAsta(), asta.getTitolo());

                arrayRitorno.add(notificaDTO);
            }
        }
        return arrayRitorno;
    }





    public int eliminaNotifica(int idNotifica){
        return controllerNotifiche.eliminaNotificaDAO(idNotifica);
    }

}
