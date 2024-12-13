package com.example.lootmarketbackend.Controller;

import com.example.lootmarketbackend.Models.*;

import com.example.lootmarketbackend.dto.DettagliAstaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ControllerTest {

    private ControllerAste controllerAsteMock;
    private ControllerUtenti controllerUtentiMock;
    private ControllerLegami controllerLegamiMock;
    private ControllerNotifiche controllerNotificheMock;
    private Controller controllerSpy;


    @BeforeEach
    public void setup(){
        controllerAsteMock = Mockito.mock(ControllerAste.class);
        controllerUtentiMock = Mockito.mock(ControllerUtenti.class);
        controllerLegamiMock = Mockito.mock(ControllerLegami.class);
        controllerNotificheMock = Mockito.mock(ControllerNotifiche.class);
        Controller realController = new Controller();
        controllerSpy = Mockito.spy(realController);


        ReflectionTestUtils.setField(controllerSpy, "controllerAste", controllerAsteMock);
        ReflectionTestUtils.setField(controllerSpy, "controllerUtenti", controllerUtentiMock);
        ReflectionTestUtils.setField(controllerSpy, "controllerLegami", controllerLegamiMock);
        ReflectionTestUtils.setField(controllerSpy, "controllerNotifiche", controllerNotificheMock);


        Offerta legame1 = new Offerta("marioBlu@gmail.com", 1, 20, LocalDate.of(2030, 12, 30), LocalTime.MIDNIGHT);
        Offerta legame2 = new Offerta("marioBlu@gmail.com", 3, 20, LocalDate.now(), LocalTime.MIDNIGHT);
        Offerta legame5 = new Offerta("marioBlu@gmail.com", 5, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Iscrizione legame3 = new Iscrizione("marioGiallo@gmail.com", 1);
        Iscrizione legame4 = new Iscrizione("marioGiallo@gmail.com", 3);
        Iscrizione legame6 = new Iscrizione("marioGiallo@gmail.com", 5);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        databaseLegami.add(legame5);
        databaseLegami.add(legame6);
        controllerLegamiMock.databaseLegami = databaseLegami;


        Mockito.when(controllerLegamiMock.getIndiceLegameByEmailAndIdAsta(Mockito.anyString(), Mockito.anyInt()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);
                    int idAsta = invocation.getArgument(1);

                    for (int i = 0; i < databaseLegami.size(); i++) {
                        if (databaseLegami.get(i).getEmailUtente().equals(email) && databaseLegami.get(i).getIdAsta() == idAsta) {
                            return i; // Restituisci l'indice se il legame corrisponde
                        }
                    }
                    return -1; // Restituisci -1 se il legame non è trovato
                });






        Mockito.when(controllerLegamiMock.getUltimaOffertaLegame(Mockito.anyInt())).thenAnswer(invocation -> {
           int idAsta = invocation.getArgument(0);
            Offerta ultimoLegame=null;
            for(Legame legame : databaseLegami){
                if(legame.getIdAsta() == idAsta){
                    if(legame instanceof Offerta legameOfferta) {
                        if(ultimoLegame == null){
                            ultimoLegame = legameOfferta;
                        }else{
                            if(ultimoLegame.getTimestamp().isBefore(legameOfferta.getTimestamp())){
                                ultimoLegame = legameOfferta;
                            }
                        }
                    }
                }
            }
            return ultimoLegame;
        });





        Mockito.when(controllerLegamiMock.getLegameDatabase(Mockito.anyInt())).thenAnswer(invocation -> {
            int indice = invocation.getArgument(0);
            return databaseLegami.get(indice);
        });





        Utente  utente1 = new Utente("marioRossi@gmail.com", "aaa", "Mario", "Rossi", "209h12e90h09eheh", "Italia", "323423434432", LocalDate.now(), null, null, "aaa", null, null);
        Utente  utente2 = new Utente("marioBlu@gmail.com", "aaa", "Mario", "Rossi", "209h12e90h09eheh", "Italia", "323423434432", LocalDate.now(), null, null, "aaa", null, null);
        Utente  utente3 = new UtenteBusiness("marioGiallo@gmail.com", "aaa", "Mario", "Rossi", "209h12e90h09eheh", "Italia", "323423434432", LocalDate.now(), null, null, "aaa", null, null, "aaa", "aaa", "2312331");
        Utente  utente4 = new UtenteBusiness("marioVerde@gmail.com", "aaa", "Mario", "Rossi", "209h12e90h09eheh", "Italia", "323423434432", LocalDate.now(), null, null, "aaa", null, null, "aaa", "aaa", "2312331");
        ArrayList<Utente> databaseUtenti = new ArrayList<>();
        databaseUtenti.add(utente1);
        databaseUtenti.add(utente2);
        databaseUtenti.add(utente3);
        databaseUtenti.add(utente4);


        Mockito.when(controllerUtentiMock.getIndiceUtenteByEmail(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);
                    for (int i = 0; i < databaseUtenti.size(); i++) {
                        if (databaseUtenti.get(i).getEmail().equals(email)) {
                            return i; // Restituisci l'indice se l'email corrisponde
                        }
                    }
                    return -1; // Restituisci -1 se l'email non è trovata
                });

        Mockito.when(controllerUtentiMock.getUtenteDatabase(Mockito.anyInt())).thenAnswer(invocation -> {
            int indice = invocation.getArgument(0);
            return databaseUtenti.get(indice);
        });





        Asta asta1 = new AstaTempoFisso(1, "marioRossi@gmail.com", "Asta a Tempo Fisso di Prova", "Fumetti & Manga", 10, LocalDateTime.of(LocalDate.of(2030, 12, 30), LocalTime.now()), "Descrizione Asta di prova", "immagineProdotto".getBytes(), 20, 100);
        Asta asta2 = new AstaTempoFisso(2, "marioRossi@gmail.com", "aaa", "aaaa", 20, LocalDateTime.now(), "aaa", null, 20, 100);
        Asta asta3 = new AstaInversa(3, "marioRossi@gmail.com", "Asta Inversa di Prova", "Fumetti & Manga", 30, LocalDateTime.of(LocalDate.of(2030, 12, 30), LocalTime.now()), "Descrizione Asta di prova", "immagineProdotto".getBytes(), 20);
        Asta asta4 = new AstaInversa(4, "marioRossi@gmail.com", "aaa", "aaaa", 10, LocalDateTime.now(), "aaa", null, 20);
        Asta asta5 = new AstaConclusa(5, "marioRossi@gmail.com", "Asta Conclusa di Prova", "Fumetti & Manga", 10, LocalDateTime.of(LocalDate.of(2030, 12, 30), LocalTime.now()), "Descrizione Asta di prova", "immagineProdotto".getBytes(), "emailVincitore@gmail.com", 100);
        ArrayList<Asta> databaseAsta = new ArrayList<>();
        databaseAsta.add(asta1);
        databaseAsta.add(asta2);
        databaseAsta.add(asta3);
        databaseAsta.add(asta4);
        databaseAsta.add(asta5);
        controllerAsteMock.databaseAste = databaseAsta;


        Mockito.when(controllerAsteMock.getIndiceAstaById(Mockito.anyInt()))
                .thenAnswer(invocation -> {
                    int idAsta = invocation.getArgument(0);
                    for (int i = 0; i < databaseAsta.size(); i++) {
                        if (databaseAsta.get(i).getIdAsta()==idAsta) {
                            return i; // Restituisci l'indice se l'asta corrisponde
                        }
                    }
                    return -1; // Restituisci -1 se l'asta non è trovata
                });

        Mockito.when(controllerAsteMock.getAstaDatabase(Mockito.anyInt())).thenAnswer(invocation -> {
            int indice = invocation.getArgument(0);
            return databaseAsta.get(indice);
        });






        Mockito.when(controllerAsteMock.effettuaOfferta(Mockito.anyString(), Mockito.anyInt(), Mockito.anyDouble())).thenAnswer(invocation -> {
            String emailOfferente = invocation.getArgument(0);
            int indiceAsta = invocation.getArgument(1);
            Double offerta = invocation.getArgument(2);

            Asta asta = databaseAsta.get(indiceAsta);
            if(asta.getEmailCreatore().equals(emailOfferente)){
                return -1;
            }
            if(asta instanceof AstaConclusa){
                return 0;
            }
            if(asta instanceof AstaInversa astaInversa) {
                if (astaInversa.presentaOfferta(offerta) == -1) {
                    return -2;
                }
            }else if (asta instanceof AstaTempoFisso astaTempoFisso) {
                if (astaTempoFisso.presentaOfferta(offerta) == -1) {
                    return -2;
                }
            }
            return 1;
        });





        Mockito.when(controllerAsteMock.getTipoAsta(Mockito.isA(Asta.class))).thenAnswer(invocation -> {
            Asta asta = invocation.getArgument(0);
            if(asta instanceof AstaInversa){
                return 1;
            }else if(asta instanceof AstaTempoFisso){
                return 0;
            }else if(asta instanceof AstaConclusa){
                return -1;
            }else{
                return 2;
            }
        });
    }





    @Test
    void testDisiscrizioneAstaNonEsistenteUtenteNonEsistente() {
        // Arrange

        when(controllerAsteMock.getIndiceAstaById(Mockito.anyInt())).thenReturn(-1);
        when(controllerUtentiMock.getIndiceUtenteByEmail(Mockito.anyString())).thenReturn(-1);
        when(controllerLegamiMock.getLegameDatabase(Mockito.anyInt())).thenReturn(new Offerta("emailUtente@gmail.com",
                1, -1, LocalDate.now(), LocalTime.MIDNIGHT));
        when(controllerLegamiMock.getIndiceLegameByEmailAndIdAsta(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
        Mockito.doNothing().when(controllerLegamiMock).eliminaLegameDAO(Mockito.anyInt(), Mockito.anyString());

        // Act
        int result = controllerSpy.disiscrizione("emailUtente@gmail.com", 1);

        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testDisiscrizioneAstaEsistenteUtenteNonEsistente() {
        // Arrange

        when(controllerAsteMock.getIndiceAstaById(Mockito.anyInt())).thenReturn(1);
        when(controllerUtentiMock.getIndiceUtenteByEmail(Mockito.anyString())).thenReturn(-1);
        when(controllerLegamiMock.getLegameDatabase(Mockito.anyInt())).thenReturn(new Offerta(
                "emailUtente@gmail.com", 1, -1, LocalDate.now(), LocalTime.MIDNIGHT));
        when(controllerLegamiMock.getIndiceLegameByEmailAndIdAsta(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
        Mockito.doNothing().when(controllerLegamiMock).eliminaLegameDAO(Mockito.anyInt(), Mockito.anyString());

        // Act
        int result = controllerSpy.disiscrizione("emailUtente@gmail.com", 1);

        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testDisiscrizioneAstaNonEsistenteUtenteEsistenteNonIscritto() {
        // Arrange
        AstaTempoFisso a = new AstaTempoFisso(1,"emailCreatore@gmail.com",
                "aaaa", "aaaa", 10, LocalDateTime.now(), "aaaa",
                null, 23, 100);
        when(controllerAsteMock.getIndiceAstaById(Mockito.anyInt())).thenReturn(-1);
        when(controllerAsteMock.getAstaDatabase(Mockito.anyInt())).thenReturn(a);
        when(controllerUtentiMock.getIndiceUtenteByEmail(Mockito.anyString())).thenReturn(-1);
        when(controllerLegamiMock.getIndiceLegameByEmailAndIdAsta(Mockito.anyString(), Mockito.anyInt())).thenReturn(-1);
        when(controllerLegamiMock.getLegameDatabase(Mockito.anyInt())).thenReturn(new Offerta("emailUtente@gmail.com",
                1, -1, LocalDate.now(), LocalTime.MIDNIGHT));
        Mockito.doNothing().when(controllerLegamiMock).eliminaLegameDAO(Mockito.anyInt(), Mockito.anyString());

        // Act
        int result = controllerSpy.disiscrizione("emailUtente@gmail.com", 1);

        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testDisiscrizioneAstaEsistenteUtenteEsistenteOfferente() {
        // Arrange
        AstaTempoFisso a = new AstaTempoFisso(1,"emailCreatore@gmail.com",
                "aaaa", "aaaa", 10, LocalDateTime.now(), "aaaa",
                null, 23, 100);

        when(controllerAsteMock.getIndiceAstaById(Mockito.anyInt())).thenReturn(1);
        when(controllerAsteMock.getAstaDatabase(Mockito.anyInt())).thenReturn(a);
        when(controllerUtentiMock.getIndiceUtenteByEmail(Mockito.anyString())).thenReturn(1);
        when(controllerLegamiMock.getIndiceLegameByEmailAndIdAsta(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
        when(controllerLegamiMock.getLegameDatabase(Mockito.anyInt())).thenReturn(new Offerta(
                "emailUtente@gmail.com", 1, 10, LocalDate.now(), LocalTime.MIDNIGHT));
        Mockito.doNothing().when(controllerLegamiMock).eliminaLegameDAO(Mockito.anyInt(), Mockito.anyString());

        // Act
        int result = controllerSpy.disiscrizione("emailUtente@gmail.com", 1);

        // Assert
        assertEquals(-1, result);
    }






    @Test
    void testDisiscrizioneAstaEsistenteUtenteEsistenteCreatore() {
        // Arrange
        AstaTempoFisso a = new AstaTempoFisso(1,"emailCreatore@gmail.com", "aaaa",
                "aaaa", 10, LocalDateTime.now(), "aaaa", null, 23, 100);

        when(controllerAsteMock.getIndiceAstaById(Mockito.anyInt())).thenReturn(1);
        when(controllerAsteMock.getAstaDatabase(Mockito.anyInt())).thenReturn(a);
        when(controllerUtentiMock.getIndiceUtenteByEmail(Mockito.anyString())).thenReturn(1);
        when(controllerLegamiMock.getIndiceLegameByEmailAndIdAsta(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
        when(controllerLegamiMock.getLegameDatabase(Mockito.anyInt())).thenReturn(new Offerta(
                "emailCreatore@gmail.com", 1, -1, LocalDate.now(), LocalTime.MIDNIGHT));
        Mockito.doNothing().when(controllerLegamiMock).eliminaLegameDAO(Mockito.anyInt(), Mockito.anyString());

        // Act
        int result = controllerSpy.disiscrizione("emailCreatore@gmail.com", 1);

        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testDisiscrizioneAstaEsistenteUtenteEsistenteIscritto() {
        // Arrange
        AstaTempoFisso a = new AstaTempoFisso(1,"emailCreatore@gmail.com", "aaaa",
                "aaaa", 10, LocalDateTime.now(), "aaaa", null, 23, 100);

        when(controllerAsteMock.getIndiceAstaById(Mockito.anyInt())).thenReturn(1);
        when(controllerAsteMock.getAstaDatabase(Mockito.anyInt())).thenReturn(a);
        when(controllerUtentiMock.getIndiceUtenteByEmail(Mockito.anyString())).thenReturn(1);
        when(controllerLegamiMock.getIndiceLegameByEmailAndIdAsta(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
        when(controllerLegamiMock.getLegameDatabase(Mockito.anyInt())).thenReturn(new Offerta(
                "emailUtente@gmail.com", 1, -1, LocalDate.now(), LocalTime.MIDNIGHT));
        Mockito.doNothing().when(controllerLegamiMock).eliminaLegameDAO(Mockito.anyInt(), Mockito.anyString());

        // Act
        int result = controllerSpy.disiscrizione("emailUtente@gmail.com", 1);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testGeneraNotificaAstaScadutaNullconUltimaOffertaNull() {
        // Arrange
        Legame legame1 = new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame2 = new Offerta("aaa22", 10, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame3 = new Iscrizione("aaa123", 10);
        Legame legame4 = new Iscrizione("aaabb", 10);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        controllerLegamiMock.databaseLegami = databaseLegami;
        when(controllerNotificheMock.aggiungiNotificaDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1);


        // Act
        int result = controllerSpy.generaNotificaAstaScaduta(null, null);

        // Assert
        assertEquals(-1, result);

    }





    @Test
    void testGeneraNotificaAstaScadutaNullUltimaOffertaSufficiente() {
        // Arrange
        Legame legame1 = new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame2 = new Offerta("aaa22", 10, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame3 = new Iscrizione("aaa123", 10);
        Legame legame4 = new Iscrizione("aaabb", 10);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        controllerLegamiMock.databaseLegami = databaseLegami;
        when(controllerNotificheMock.aggiungiNotificaDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1);


        // Act
        int result = controllerSpy.generaNotificaAstaScaduta(null,
                        new Offerta("aaa", 1, 100, LocalDate.now(), LocalTime.MIDNIGHT));

        // Assert
        assertEquals(-1, result);

    }





    @Test
    void testGeneraNotificaAstaScadutaGiaConclusaOffertaSufficiente() {
        // Arrange
        Legame legame1 = new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame2 = new Offerta("aaa22", 10, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame3 = new Iscrizione("aaa123", 10);
        Legame legame4 = new Iscrizione("aaabb", 10);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        controllerLegamiMock.databaseLegami = databaseLegami;
        when(controllerNotificheMock.aggiungiNotificaDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1);


        // Act
        int result = controllerSpy.generaNotificaAstaScaduta(new AstaConclusa(10, "emailcreatore@gmail.com",
                        "aaa", "aaaa", 10, LocalDateTime.now(),"aaaa",
                        null, "emailVincitore@gmail.com", 100.0 ),
                new Offerta("aaa", 1, -1, LocalDate.now(), LocalTime.MIDNIGHT));

        // Assert
        assertEquals(-1, result);

    }





    @Test
    void testGeneraNotificaAstaScadutaUltimaOffertaNull() {
        // Arrange
        Legame legame1 = new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame2 = new Offerta("aaa22", 10, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame3 = new Iscrizione("aaa123", 10);
        Legame legame4 = new Iscrizione("aaabb", 10);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        controllerLegamiMock.databaseLegami = databaseLegami;
        when(controllerNotificheMock.aggiungiNotificaDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1);

        // Act
        int result = controllerSpy.generaNotificaAstaScaduta(new AstaTempoFisso(1,
                        "emailCreatore@gmail.com", "aaaa", "aaaa", 10,
                        LocalDateTime.now(),"aaaa",  null, 199,
                        100),
                        null);

        // Assert
        assertEquals(0, result);

    }





    @Test
    void testGeneraNotificaAstaScadutaUltimaOffertaNegativa() {
        // Arrange
        Legame legame1 = new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame2 = new Offerta("aaa22", 10, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame3 = new Iscrizione("aaa123", 10);
        Legame legame4 = new Iscrizione("aaabb", 10);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        controllerLegamiMock.databaseLegami = databaseLegami;
        when(controllerNotificheMock.aggiungiNotificaDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1);

        // Act
        int result = controllerSpy.generaNotificaAstaScaduta(new AstaTempoFisso(1,
                        "emailCreatore@gmail.com", "aaaa", "aaaa", 10,
                        LocalDateTime.now(),"aaaa",  null, 199,
                        100),
                         new Offerta("aaa", 1, -1, LocalDate.now(), LocalTime.MIDNIGHT));

        // Assert
        assertEquals(1, result);

    }





    @Test
    void testGeneraNotificaAstaScadutaUltimaOffertaNonSufficiente() {
        // Arrange
        Legame legame1 = new Offerta("aaa", 10, 99, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame2 = new Offerta("aaa22", 10, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame3 = new Iscrizione("aaa123", 10);
        Legame legame4 = new Iscrizione("aaabb", 10);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        controllerLegamiMock.databaseLegami = databaseLegami;
        when(controllerNotificheMock.aggiungiNotificaDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1);

        // Act
        int result = controllerSpy.generaNotificaAstaScaduta(new AstaTempoFisso(1,
                        "emailCreatore@gmail.com", "aaaa", "aaaa", 10,
                        LocalDateTime.now(),"aaaa",  null, 99,
                        100),
                new Offerta("aaa", 1, 99, LocalDate.now(), LocalTime.MIDNIGHT));

        // Assert
        assertEquals(1, result);

    }





    @Test
    void testGeneraNotificaAstaScadutaTempoFissoConSuccesso() {
        // Arrange
        Legame legame1 = new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame2 = new Offerta("aaa22", 10, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame3 = new Iscrizione("aaa123", 10);
        Legame legame4 = new Iscrizione("aaabb", 10);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        controllerLegamiMock.databaseLegami = databaseLegami;
        when(controllerNotificheMock.aggiungiNotificaDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1);

        // Act
        int result = controllerSpy.generaNotificaAstaScaduta(new AstaTempoFisso(10,
                        "emailCreatore@gmail.com", "aaaa", "aaaa", 10,
                        LocalDateTime.now(),"aaaa",  null, 199,
                        100),
                new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT));

        // Assert
        assertEquals(2, result);

    }





    @Test
    void testGeneraNotificaAstaScadutaInversaConSuccesso() {
        // Arrange
        Legame legame1 = new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame2 = new Offerta("aaa22", 10, 100, LocalDate.now(), LocalTime.MIDNIGHT);
        Legame legame3 = new Iscrizione("aaa123", 10);
        Legame legame4 = new Iscrizione("aaabb", 10);
        ArrayList<Legame> databaseLegami= new ArrayList<Legame>();
        databaseLegami.add(legame1);
        databaseLegami.add(legame2);
        databaseLegami.add(legame3);
        databaseLegami.add(legame4);
        controllerLegamiMock.databaseLegami = databaseLegami;
        when(controllerNotificheMock.aggiungiNotificaDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1);

        // Act
        int result = controllerSpy.generaNotificaAstaScaduta(new AstaInversa(10,
                        "emailCreatore@gmail.com", "aaaa", "aaaa", 10,
                        LocalDateTime.now(),"aaaa",  null, 199),
                new Offerta("aaa", 10, 199, LocalDate.now(), LocalTime.MIDNIGHT));

        // Assert
        assertEquals(2, result);

    }





    @Test
    void testNuovaOffertaEmailUtenteVuota() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("", 2, 30);


        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoEmailUtenteNonValidaPiuUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBianchi@gmail.com", 2, 21);


        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testNuovaOffertaAstaNonTrovataUtenteIscrittoOffertaZero() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 12, 0);

        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testNuovaOffertaAstaConclusaUtenteGiaOfferenteOffertaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 5, 3000000000000000000000000000.0);

        // Assert
        assertEquals(0, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteCreatoreOffertaMaggioreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioRossi@gmail.com", 1, 30);

        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaMaggioreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, 30);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaUgualeUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, 20);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaPiuUnoUltimaOfferta() {
        // Arrange

        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, 21);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaMenoUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, 19);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaMinoreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, 12);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaZero() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, 0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, 1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaMenoUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, -1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaNegativaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, -100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteSenzaLegamiOffertaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 1, 100000000000000000000000000000000.0);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaMaggioreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, 30);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaUgualeUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, 20);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaPiuUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, 21);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaMenoUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, 19);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaMinoreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, 12);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaZero() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, 0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, 1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaMenoUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, -1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaNegativaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, -100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteIscrittoOffertaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 1, 100000000000000000000000000000000.0);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaMaggioreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, 30);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaUgualeUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, 20);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaPiuUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, 21);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaMenoUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, 19);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaMinoreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, 12);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaZero() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, 0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, 1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaMenoUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, -1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaNegativaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, -100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaTempoFissoUtenteGiaOfferenteOffertaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 1, 100000000000000000000000000000000.0);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteCreatore() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioRossi@gmail.com", 3, 30);

        // Assert
        assertEquals(-1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaMaggioreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, 30);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaUgualeUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, 20);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaPiuUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, 21);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaMenoUnoUltimaOfferta() {
        // Arrange

        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, 19);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaMinoreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, 12);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaZero() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, 0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());


        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, 1);


        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaMenoUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, -1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaNegativaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, -100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteSenzaLegamiOffertaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioVerde@gmail.com", 3, 100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaMaggioreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, 30);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaUgualeUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, 20);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaPiuUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, 21);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaMenoUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, 19);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaMinoreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, 12);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaZero() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, 0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, 1);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaMenoUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, -1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaNegativaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, -100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteIscrittoOffertaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioGiallo@gmail.com", 3, 100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaMaggioreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, 30);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaUgualeUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, 20);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaPiuUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, 21);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaMenoUnoUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, 19);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaMinoreUltimaOfferta() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, 12);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaZero() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, 0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaUno() {
        // Arrange

        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, 1);

        // Assert
        assertEquals(1, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaMenoUno() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, -1);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaNegativaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, -100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testNuovaOffertaAstaInversaUtenteGiaOfferenteOffertaMoltoGrande() {
        // Arrange
        Mockito.doNothing().when(controllerLegamiMock).aggiungiLegamiDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerLegamiMock).modificaUltimaOffertaLegameDAO(Mockito.anyInt(), Mockito.anyString(), Mockito.anyDouble(), Mockito.any());
        Mockito.doNothing().when(controllerSpy).generaNotificheNuovaOfferta(Mockito.anyInt());

        // Act
        int result = controllerSpy.nuovaOfferta("marioBlu@gmail.com", 3, 100000000000000000000000000000000.0);

        // Assert
        assertEquals(-2, result);
    }





    @Test
    void testGetDettagliAstaAstaInesistenteUtenteOfferente() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(0, "marioBlu@gmail.com");

        // Assert
        assertNull(astaRisultato);
    }





    @Test
    void testGetDettagliAstaTempoFissoUtenteSenzaLegami() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(1, "marioVerde@gmail.com");

        // Assert
        assertEquals(1, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta a Tempo Fisso di Prova", astaRisultato.titolo);
        assertEquals(10, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(20, astaRisultato.ultimaOfferta);
        assertEquals(100, astaRisultato.sogliaMinima);
        assertEquals("Asta a Tempo Fisso", astaRisultato.tipoAsta);
        assertEquals(false, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("NonIscritto", astaRisultato.statusLegame);
    }





    @Test
    void testGetDettagliAstaTempoFissoUtenteIscritto() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(1, "marioGiallo@gmail.com");

        // Assert
        assertEquals(1, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta a Tempo Fisso di Prova", astaRisultato.titolo);
        assertEquals(10, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(20, astaRisultato.ultimaOfferta);
        assertEquals(100, astaRisultato.sogliaMinima);
        assertEquals("Asta a Tempo Fisso", astaRisultato.tipoAsta);
        assertEquals(false, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("Iscritto", astaRisultato.statusLegame);

    }





    @Test
    void testGetDettagliAstaTempoFissoUtenteOfferente() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(1, "marioBlu@gmail.com");

        // Assert
        assertEquals(1, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta a Tempo Fisso di Prova", astaRisultato.titolo);
        assertEquals(10, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(20, astaRisultato.ultimaOfferta);
        assertEquals(100, astaRisultato.sogliaMinima);
        assertEquals("Asta a Tempo Fisso", astaRisultato.tipoAsta);
        assertEquals(true, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("OffertaFatta", astaRisultato.statusLegame);
    }





    @Test
    void testGetDettagliAstaInversaUtenteSenzaLegami() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(3, "marioVerde@gmail.com");

        // Assert
        assertEquals(3, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta Inversa di Prova", astaRisultato.titolo);
        assertEquals(30, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(20, astaRisultato.ultimaOfferta);
        assertEquals(0.0, astaRisultato.sogliaMinima);
        assertEquals("Asta Inversa", astaRisultato.tipoAsta);
        assertEquals(false, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("NonIscritto", astaRisultato.statusLegame);
    }





    @Test
    void testGetDettagliAstaInversaUtenteIscritto() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(3, "marioGiallo@gmail.com");

        // Assert
        assertEquals(3, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta Inversa di Prova", astaRisultato.titolo);
        assertEquals(30, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(20, astaRisultato.ultimaOfferta);
        assertEquals(0.0, astaRisultato.sogliaMinima);
        assertEquals("Asta Inversa", astaRisultato.tipoAsta);
        assertEquals(false, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("Iscritto", astaRisultato.statusLegame);
    }





    @Test
    void testGetDettagliAstaInversaUtenteOfferente() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(3, "marioBlu@gmail.com");

        // Assert
        assertEquals(3, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta Inversa di Prova", astaRisultato.titolo);
        assertEquals(30, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(20, astaRisultato.ultimaOfferta);
        assertEquals(0.0, astaRisultato.sogliaMinima);
        assertEquals("Asta Inversa", astaRisultato.tipoAsta);
        assertEquals(true, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("OffertaFatta", astaRisultato.statusLegame);
    }





    @Test
    void testGetDettagliAstaConclusaUtenteSenzaLegami() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(5, "marioVerde@gmail.com");

        // Assert
        assertEquals(5, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta Conclusa di Prova", astaRisultato.titolo);
        assertEquals(10, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(100, astaRisultato.ultimaOfferta);
        assertEquals(0.0, astaRisultato.sogliaMinima);
        assertEquals("Asta Conclusa", astaRisultato.tipoAsta);
        assertEquals(false, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("NonIscritto", astaRisultato.statusLegame);
    }





    @Test
    void testGetDettagliAstaConclusaUtenteIscritto() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(5, "marioGiallo@gmail.com");

        // Assert
        assertEquals(5, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta Conclusa di Prova", astaRisultato.titolo);
        assertEquals(10, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(100, astaRisultato.ultimaOfferta);
        assertEquals(0.0, astaRisultato.sogliaMinima);
        assertEquals("Asta Conclusa", astaRisultato.tipoAsta);
        assertEquals(false, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("Iscritto", astaRisultato.statusLegame);
    }





    @Test
    void testGetDettagliAstaConclusaUtenteOfferente() {
        // Arrange

        // Act
        DettagliAstaDTO astaRisultato = controllerSpy.getDettagliAsta(5, "marioBlu@gmail.com");

        // Assert
        assertEquals(5, astaRisultato.idAsta);
        assertEquals("marioRossi@gmail.com", astaRisultato.emailCreatore);
        assertEquals("Fumetti & Manga", astaRisultato.categoria);
        assertEquals("Asta Conclusa di Prova", astaRisultato.titolo);
        assertEquals(10, astaRisultato.prezzoPartenza);
        assertEquals(2030, astaRisultato.anno);
        assertEquals(12, astaRisultato.mese);
        assertEquals(30, astaRisultato.giorno);
        assertEquals("Descrizione Asta di prova", astaRisultato.descrizione);
        assertEquals(100, astaRisultato.ultimaOfferta);
        assertEquals(0.0, astaRisultato.sogliaMinima);
        assertEquals("Asta Conclusa", astaRisultato.tipoAsta);
        assertEquals(true, astaRisultato.ultimaOffertaTua);
        assertEquals("Mario Rossi", astaRisultato.nomeAutore);
        assertEquals("OffertaFatta", astaRisultato.statusLegame);
    }





}