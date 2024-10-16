package com.example.lootmarketbackend.Controller;

import com.example.lootmarketbackend.Modelli.*;
import com.example.lootmarketbackend.dto.AstaDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class Controller {

    public ControllerUtenti controllerUtenti;
    public ControllerAste controllerAste;
    public ControllerLegami controllerLegami;

    public Controller(){
        controllerUtenti = new ControllerUtenti();
        controllerAste = new ControllerAste();
        controllerLegami = new ControllerLegami();
    }

    public void checkAsteScadute(){
        for(int i = 0; i < controllerAste.getDatabaseSize(); i++){
            Asta asta = controllerAste.getAstaDatabase(i);
            String emailVincitore;
            if(asta.getDataScadenza().isBefore(LocalDateTime.now())){
                int j = 0;
                while((j < controllerLegami.getDatabaseSize()) && (controllerLegami.getLegameDatabase(j).getOfferta() != asta.getUltimaOfferta())){
                    j++;
                }
                emailVincitore =  controllerLegami.getLegameDatabase(j).getEmailUtente();
                controllerAste.concludiAstaDAO(asta.getIdAsta(), emailVincitore, asta.getUltimaOfferta());
            }
        }
    }

    //ritorna -1 se la presentazione dell'offerta non va a buon fine, 1 se invece va tutto bene
    public int nuovaOfferta(String email, int idAsta, double nuovaOfferta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        int k = controllerUtenti.getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1) {
            Asta asta = controllerAste.getAstaDatabase(i);
            if(asta instanceof AstaTempoFisso astaTempoFisso){
                if (astaTempoFisso.presentaOfferta(nuovaOfferta) == -1) {
                    return -1;
                } else {
                    int j = controllerLegami.getIndiceLegameByEmailAndIdAsta(email, idAsta);
                    if (j != -1) {
                        controllerLegami.aggiungiLegamiDAO(idAsta, email, nuovaOfferta, LocalDateTime.now());
                    } else {
                        controllerLegami.modificaUltimaOffertaLegameDAO(idAsta, email, nuovaOfferta, LocalDateTime.now());
                    }
                    controllerAste.modificaUltimaOffertaAstaDAO(idAsta, nuovaOfferta);
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
        int i = controllerAste.getIndiceAstaById(idAsta);
        int k = controllerUtenti.getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1){
            controllerLegami.aggiungiLegamiDAO(idAsta, email, -1, LocalDateTime.now());
            return 1;
        }
        return -1;
    }

    //ritorna -1 se la disiscrizione non va a buon fine, 1 se invece va tutto bene
    public int disiscrizione(String email, int idAsta){
        int i = controllerAste.getIndiceAstaById(idAsta);
        int k = controllerUtenti.getIndiceUtenteByEmail(email);
        if(i != -1 && k!= -1){
            int j = controllerLegami.getIndiceLegameByEmailAndIdAsta(email, idAsta);
            if(j != -1){
                if(controllerLegami.getLegameDatabase(j).getOfferta() == -1){
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
        int indiceAsta = controllerAste.getDatabaseSize() - 1;
        while((i < 10) && ((indiceAsta - j) > 0)){
            Asta asta = controllerAste.getAstaDatabase(indiceAsta - j);
            if(!(asta instanceof AstaConclusa)){
                i++;
                if(i >= 0){
                    String tipo;
                    if(asta.getSogliaMinima() == -1){
                        tipo = "Asta Inversa";
                    }else{
                        tipo = "Asta a Tempo Fisso";
                    }
                    System.out.println("Creo AstaDTO con titolo:" + asta.getTitolo());
                    AstaDTO astaDTO = new AstaDTO(asta.getIdAsta(), asta.getEmailCreatore(), asta.getTitolo(), asta.getCategoria(), asta.getPrezzoPartenza(), asta.getDataScadenza().getYear(), asta.getDataScadenza().getMonthValue(), asta.getDataScadenza().getDayOfMonth(), asta.getDescrizione(), asta.getUltimaOfferta(), asta.getSogliaMinima(), tipo);
                    arrayRitorno.add(astaDTO);
                }
            }
            j++;
        }
        return arrayRitorno;
    }

    public Asta ottieniDettagliAsta(int idAsta, String emailUtente, String nomeAutore, int tipoAsta, int statusLegame){
        Asta astaRitorno = controllerAste.getAstaDatabase(controllerAste.getIndiceAstaById(idAsta));
        Utente utenteAutore = controllerUtenti.getUtenteDatabase(controllerUtenti.getIndiceUtenteByEmail(astaRitorno.getEmailCreatore()));
        nomeAutore = utenteAutore.getNome() + " " + utenteAutore.getCognome();
        tipoAsta = controllerAste.getTipoAsta(astaRitorno);

        //statusLegame -1 non è iscritto, 1 iscritto, 2 se ha presentato offerta (include anche iscrizione)
        int j = controllerLegami.getIndiceLegameByEmailAndIdAsta(emailUtente, idAsta);
        if(j == -1){
            statusLegame = j;
        }else{
            if(controllerLegami.getLegameDatabase(j) instanceof Iscrizione){
                statusLegame = 1;
            }else{
                statusLegame = 2;
            }
        }
        return astaRitorno;
    }

    public ArrayList<Asta> getAsteByEmailUtente(String email){
        ArrayList<Asta> arrayRitorno = new ArrayList<>();
        for(int i = 0; i < controllerLegami.getDatabaseSize(); i++){
            if(controllerLegami.getLegameDatabase(i).getEmailUtente().equals(email)){
                arrayRitorno.add(controllerAste.getAstaDatabase(controllerAste.getIndiceAstaById(controllerLegami.getLegameDatabase(i).getIdAsta())));
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
        controllerAste.aggiungiAstaDAO(emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima, tipoAsta);
        int idAsta = controllerAste.getDatabaseSize()-1;
        return iscrizione(emailCreatore, idAsta);
    }
}
