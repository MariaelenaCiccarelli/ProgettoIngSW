package com.example.lootmarketbackend.API;

import com.example.lootmarketbackend.Controller.Controller;
import com.example.lootmarketbackend.Modelli.Contatti;
import com.example.lootmarketbackend.Modelli.Indirizzo;
import com.example.lootmarketbackend.dto.AstaDTO;
import com.example.lootmarketbackend.dto.UtenteAutenticazioneDTO;
import com.example.lootmarketbackend.dto.UtenteDTO;

import com.example.lootmarketbackend.services.JwtUtil;
import com.example.lootmarketbackend.services.MyToken;

import io.jsonwebtoken.*;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    @PostMapping("/postCreaAsta")
    public void postAsta(@RequestBody AstaDTO astaDTO, @RequestParam String token){

        System.out.println("Richiesta creazione Asta|Token Ricevuto: "+token);
        try {
            Claims claims = JwtUtil.decodeJWT(token);
            if(claims!=null){
                if(claims.getIssuer().equals("LootMarket")){
                    System.out.println("Token Valido!");

                    byte[] immagineProdotto = {1, 0, 1, 0, 0, 0, 0, 1};
                    LocalDateTime dataScadenza = LocalDateTime.of(astaDTO.anno, astaDTO.mese, astaDTO.giorno, 23, 59, 59);
                    controller.creaAsta(astaDTO.emailCreatore, astaDTO.titolo, astaDTO.categoria, astaDTO.prezzoPartenza, dataScadenza, astaDTO.descrizione, immagineProdotto, astaDTO.ultimaOfferta, astaDTO.sogliaMinima, astaDTO.tipoAsta);

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
    public int postModificaUtente(@RequestBody UtenteDTO utenteDTO){
        byte[] immagineProdotto = {1, 0, 1, 0, 0, 0, 0, 1};
        Contatti contatti = new Contatti(utenteDTO.sito, utenteDTO.socialFacebook, utenteDTO.socialInstagram);
        controller.controllerUtenti.modificaUtenteDAO(utenteDTO.mail, utenteDTO.nazione, utenteDTO.numeroCellulare, contatti, utenteDTO.biografia, immagineProdotto, utenteDTO.indirizzoFatturazione, utenteDTO.indirizzoSpedizione, utenteDTO.numeroAziendale);
        return 1;
    }

    @PostMapping( "/postRegistraUtente")
    @ResponseBody
    public MyToken postRegistraUtente(@RequestBody UtenteAutenticazioneDTO utenteAutenticazioneDTO){
        byte[] immagineProdotto = {1, 0, 1, 0, 0, 0, 0, 1};
        Contatti contatti = new Contatti("", "", "");
        Indirizzo indirizzoVuoto = new Indirizzo("", "", "", "");
        LocalDate dataNascita = LocalDate.of(utenteAutenticazioneDTO.dataDiNascitaAnno, utenteAutenticazioneDTO.dataDiNascitaMese, utenteAutenticazioneDTO.dataDiNascitaGiorno);
        String jwsToken;
        int status = controller.controllerUtenti.aggiungiUtenteDAO(utenteAutenticazioneDTO.mail, utenteAutenticazioneDTO.password, utenteAutenticazioneDTO.nome, utenteAutenticazioneDTO.cognome, utenteAutenticazioneDTO.codiceFiscale, utenteAutenticazioneDTO.nazione, utenteAutenticazioneDTO.numeroCellulare, dataNascita, contatti, "", immagineProdotto, indirizzoVuoto, indirizzoVuoto);
        if(status==1){ //Utente creato con successo
            try {
                jwsToken = JwtUtil.encodeJWT(utenteAutenticazioneDTO.mail);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //return token
            MyToken myToken = new MyToken(jwsToken);
            System.out.println(myToken.getToken());
            return myToken;
        }
        else{ //errore durante la registrazione dell'utente
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la registrazione!");

        }
    }

    @PostMapping("/postAccediUtente")
    @ResponseBody
    public MyToken postAccediUtente(@RequestBody UtenteAutenticazioneDTO utenteAutenticazioneDTO){
        int status = controller.controllerUtenti.verificaUtente(utenteAutenticazioneDTO.mail, utenteAutenticazioneDTO.password);
        if(status==1){ //Utente verificato con successo
            try{
            String jwsToken = JwtUtil.encodeJWT(utenteAutenticazioneDTO.mail);
                MyToken myToken = new MyToken(jwsToken);
                return myToken;
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }else{ //errore durante l'autenticazione dell'utente
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la autenticazione!");

        }
    }

    @GetMapping ("/getAsteUtente")
    public ArrayList<AstaDTO> getAsteHome(@RequestParam String email, @RequestParam String token){
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

}
