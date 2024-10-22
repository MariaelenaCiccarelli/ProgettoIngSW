package com.example.lootmarketbackend.Controller;

import com.example.lootmarketbackend.DAO.AstaDAO;
import com.example.lootmarketbackend.ImplementazionePostgresDAO.AstaImplementazionePostgresDAO;
import com.example.lootmarketbackend.Modelli.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class ControllerAste {

    public ArrayList<Asta> databaseAste = new ArrayList<>();
    private int idUltimaAsta;

    public ControllerAste(){
        databaseAste = new ArrayList<>();
        leggiAsteDAO();
        databaseAste.sort(Comparator.comparing(Asta::getIdAsta));
        idUltimaAsta = databaseAste.getLast().getIdAsta();
    }

    public int getIdUltimaAsta(){return idUltimaAsta;}
    public int getDatabaseSize(){return databaseAste.size();}

    public Asta getAstaDatabase(int indice){
        return databaseAste.get(indice);
    }


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

    public int aggiungiAstaDAO(String emailCreatore,
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
        int idAsta = idUltimaAsta+1;
        if(tipoAsta.equals("Asta a Tempo Fisso")){
            asta = new AstaTempoFisso(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima);
        }else{
            asta = new AstaInversa(idAsta, emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta);
        }


        AstaDAO astaDAO = new AstaImplementazionePostgresDAO();
        if(astaDAO.aggiungiAstaDB(emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima, tipoAsta)==1){
            databaseAste.add(asta);
            idUltimaAsta++;
            return 1;
        }else{
            return -1;
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
}
