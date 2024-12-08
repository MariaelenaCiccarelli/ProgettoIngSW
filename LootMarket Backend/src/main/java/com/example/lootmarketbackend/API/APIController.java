package com.example.lootmarketbackend.API;

import com.example.lootmarketbackend.Controller.Controller;
import com.example.lootmarketbackend.Modelli.*;
import com.example.lootmarketbackend.dto.*;
import com.example.lootmarketbackend.services.JwtUtil;
import com.example.lootmarketbackend.services.MyToken;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;





@RestController
public class APIController {

    @Autowired
    public APIController(Controller controller){
        this.controller = controller;
        controller.checkAsteScadute();
    }

    private Controller controller;





    @GetMapping ("/getAsteHome")
    public ArrayList<AstaDTO> getAsteHome(@RequestParam Integer indice, @RequestParam String token){
        System.out.println("Richiesta lista Aste|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return controller.recuperaAsteHome(indice);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }

    }





    @GetMapping ("/getDatiUtentePersonali")
    public UtenteDTO getDatiUtentePersonali(@RequestParam String mailUtente, @RequestParam String token){
        System.out.println("Richiesta Dati Utente: "+mailUtente+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return controller.getDatiUtentePersonali(mailUtente);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @GetMapping ("/getDatiUtenteTerzi")
    public UtenteDTO getDatiUtenteTerzi(@RequestParam String mailUtente, @RequestParam String token){
        System.out.println("Richiesta Dati Utente: "+mailUtente+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return controller.getDatiUtenteTerzi(mailUtente);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @GetMapping ("/getDettagliAsta")
    public DettagliAstaDTO getDettagliAsta(@RequestParam int idAsta, @RequestParam String mailUtente, @RequestParam String token){
        System.out.println("Richiesta Dati Asta: "+idAsta+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    DettagliAstaDTO dettagliAstaDTO = controller.getDettagliAsta(idAsta, mailUtente);
                    if(dettagliAstaDTO!=null){
                        return dettagliAstaDTO;
                    }else{
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asta inesistente!");
                    }
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @PostMapping("/postModificaUtente")
    public int postModificaUtente(@RequestPart MultipartFile immagineProfiloDTO, @RequestPart UtenteDTO utenteDTO, @RequestParam String token){
        System.out.println("Richiesta Modifica Utente: "+utenteDTO.mail+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    byte[] immagineProfilo = new byte[0];
                    try{
                        immagineProfilo = immagineProfiloDTO.getBytes();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    Contatti contatti = new Contatti(utenteDTO.sito, utenteDTO.socialFacebook, utenteDTO.socialInstagram);
                    if(controller.controllerUtenti.modificaUtenteDAO(utenteDTO.mail, utenteDTO.nazione, utenteDTO.numeroCellulare, contatti, utenteDTO.biografia, immagineProfilo, utenteDTO.indirizzoFatturazione, utenteDTO.indirizzoSpedizione, utenteDTO.numeroAziendale)==1){
                        System.out.println("Modifica utente effettuato con successo!");
                        return 1;
                    }else{
                        System.out.println("Errore nella modifica utente!");
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Modifica utente fallita!");
                    }
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @PostMapping("/postUpgradeUtente")
    public int postUpgradeUtente(@RequestBody UtenteDTO utenteDTO, @RequestParam String token){
        System.out.println("Richiesta Upgrade Utente: "+utenteDTO.mail+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    if(controller.controllerUtenti.upgradeUtenteDAO(utenteDTO.mail,utenteDTO.ragioneSociale, utenteDTO.partitaIva, utenteDTO.numeroAziendale)==0){
                        System.out.println("Upgrade utente fallito!");
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upgrade utente fallito!");
                    }
                    else{
                        System.out.println("Upgrade utente effettuato con successo!");
                        return 1;
                    }
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @PostMapping( "/postRegistraUtente")
    @ResponseBody
    public MyToken postRegistraUtente(@RequestBody UtenteAutenticazioneDTO utenteAutenticazioneDTO) throws IOException {
        System.out.println("Richiesta Registrazione Utente: "+utenteAutenticazioneDTO.mail);
        byte[] immagineProfilo = Files.readAllBytes(Paths.get("./CartellaImmagini/placeholder.png"));
        Contatti contatti = new Contatti("", "", "");
        Indirizzo indirizzoVuoto = new Indirizzo("", "", "", "");
        LocalDate dataNascita = LocalDate.of(utenteAutenticazioneDTO.dataDiNascitaAnno, utenteAutenticazioneDTO.dataDiNascitaMese, utenteAutenticazioneDTO.dataDiNascitaGiorno);
        String jwsToken;
        int status = controller.controllerUtenti.aggiungiUtenteDAO(utenteAutenticazioneDTO.mail, utenteAutenticazioneDTO.password, utenteAutenticazioneDTO.nome, utenteAutenticazioneDTO.cognome, utenteAutenticazioneDTO.codiceFiscale, utenteAutenticazioneDTO.nazione, utenteAutenticazioneDTO.numeroCellulare, dataNascita, contatti, "", immagineProfilo, indirizzoVuoto, indirizzoVuoto);
        if(status==1){ //Utente creato con successo
            try {
                jwsToken = JwtUtil.encodeJWT(utenteAutenticazioneDTO.mail);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            MyToken myToken = new MyToken(jwsToken, false);
            return myToken;
        }
        else if(status==2){ //errore durante la registrazione dell'utente
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "2");
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la registrazione!");
        }
    }





    @PostMapping("/postAccediUtente")
    @ResponseBody
    public MyToken postAccediUtente(@RequestBody UtenteAutenticazioneDTO utenteAutenticazioneDTO){
        System.out.println("Richiesta di Accesso Utente: "+utenteAutenticazioneDTO.mail);
        int status = controller.controllerUtenti.verificaUtente(utenteAutenticazioneDTO.mail, utenteAutenticazioneDTO.password);
        if(status==1){ //Utente verificato con successo
            Boolean isBusiness = false;
            if(controller.controllerUtenti.getUtenteByEmail(utenteAutenticazioneDTO.mail) instanceof UtenteBusiness){
                isBusiness = true;
            }
            try{
            String jwsToken = JwtUtil.encodeJWT(utenteAutenticazioneDTO.mail);
                MyToken myToken = new MyToken(jwsToken, isBusiness);
                return myToken;
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la autenticazione!");
        }
    }





    @GetMapping ("/getAsteUtente")
    public ArrayList<AstaDTO> getAsteUtente(@RequestParam String email, @RequestParam String token){
        System.out.println("Richiesta lista Aste Storico Utente: "+email+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return controller.getAsteByEmailUtente(email);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @GetMapping ("/getAsteUtenteTerzi")
    public ArrayList<AstaDTO> getAsteUtenteTerzi(@RequestParam String emailUtente, @RequestParam String emailUtenteTerzi, @RequestParam String token){
        System.out.println("Richiesta lista Aste Storico dell'utente: "+emailUtenteTerzi+"da parte di Utente: "+emailUtente+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return controller.getAsteByEmailUtenteTerzi(emailUtente, emailUtenteTerzi);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @PostMapping("/postCreaAsta")
    public int postAsta(@RequestPart MultipartFile immagineProdottoDTO, @RequestPart AstaDTO astaDTO, @RequestParam String token){
        System.out.println("Richiesta creazione Asta|Con token: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    String uniqueFileName = UUID.randomUUID().toString() + "_" + immagineProdottoDTO.getOriginalFilename();

                    Path uploadPath = Path.of("CartellaImmagini");
                    Path filePath = uploadPath.resolve(uniqueFileName);
                    if (!Files.exists(uploadPath)) {
                        try {
                            Files.createDirectories(uploadPath);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        Files.copy(immagineProdottoDTO.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    byte[] immagineProdotto = new byte[0];
                    try{
                        immagineProdotto = immagineProdottoDTO.getBytes();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Token Scaduto!");
                    }
                    LocalDateTime dataScadenza = LocalDateTime.of(astaDTO.anno, astaDTO.mese, astaDTO.giorno, 23, 59, 59);
                    controller.creaAsta(astaDTO.emailCreatore, astaDTO.titolo, astaDTO.categoria, astaDTO.prezzoPartenza, dataScadenza, astaDTO.descrizione,immagineProdotto, astaDTO.ultimaOfferta, astaDTO.sogliaMinima, astaDTO.tipoAsta);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
        return 0;
    }





    @PostMapping("/postIscrizione")
    public int postIscrizione(@RequestBody OffertaDTO offertaDTO, @RequestParam String token){
        System.out.println("Richiesta di Iscrizione all'asta: "+offertaDTO.idAsta+" da parte di "+offertaDTO.mailUtente+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return  controller.iscrizione(offertaDTO.mailUtente, offertaDTO.idAsta);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @PostMapping("/postDisiscrizione")
    public int postDisiscrizione(@RequestBody OffertaDTO offertaDTO, @RequestParam String token){
        System.out.println("Richiesta di Disiscrizione all'asta: "+offertaDTO.idAsta+" da parte di "+offertaDTO.mailUtente+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return  controller.disiscrizione(offertaDTO.mailUtente, offertaDTO.idAsta);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @PostMapping("/postNuovaOfferta")
    public int postNuovaOfferta(@RequestBody OffertaDTO offertaDTO, @RequestParam String token){
        System.out.println("Richiesta di Nuova Offerta all'asta: "+offertaDTO.idAsta+" da parte di "+offertaDTO.mailUtente+"con offerta: "+offertaDTO.offerta+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return  controller.nuovaOfferta(offertaDTO.mailUtente, offertaDTO.idAsta, offertaDTO.offerta);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @GetMapping ("/getNotificheUtente")
    public ArrayList<NotificaDTO> getNotificheUtente(@RequestParam String email, @RequestParam String token){
        System.out.println("Richiesta lista notifiche Utente: "+email+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return controller.getNotificheByEmailUtente(email);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @PostMapping("/postEliminaNotifica")
    public int postEliminaNotifica(@RequestParam int idNotifica, @RequestParam String token){
        System.out.println("Richiesta di eliminazione della notfica con id: "+idNotifica+"|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");
                    return  controller.eliminaNotifica(idNotifica);
                }else{
                    System.out.println("Token Non valido!");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido!");
                }
            }else{
                System.out.println("Errore nella decodifica del token!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la decodifica del token");
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token Scaduto!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Scaduto!");
        }
    }





    @GetMapping("/getEsisteUtente")
    public int getEsisteUtente(@RequestParam String email) {
        System.out.println("Richiesta di Esistenza utente: " + email);
        if(controller.controllerUtenti.utenteEsistente(email)){
            return 1;
        }
        else
            return 0;
    }
}
