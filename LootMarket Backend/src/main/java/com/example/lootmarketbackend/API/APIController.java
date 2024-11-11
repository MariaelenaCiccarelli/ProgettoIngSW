package com.example.lootmarketbackend.API;

import com.example.lootmarketbackend.Controller.Controller;
import com.example.lootmarketbackend.Modelli.Asta;
import com.example.lootmarketbackend.Modelli.Contatti;
import com.example.lootmarketbackend.Modelli.Indirizzo;
import com.example.lootmarketbackend.dto.AstaDTO;
import com.example.lootmarketbackend.dto.UtenteAutenticazioneDTO;
import com.example.lootmarketbackend.dto.UtenteDTO;

import com.example.lootmarketbackend.services.JwtUtil;
import com.example.lootmarketbackend.services.MyToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecretJwk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.security.Key;
import java.util.Date;

@RestController
public class APIController {


    @Autowired
    public APIController(Controller controller){
        this.controller = controller;
        controller.checkAsteScadute();
    }

    private Controller controller;

    @GetMapping ("/getAsteHome")
    public ArrayList<AstaDTO> getAsteHome(@RequestParam Integer indice){
        return controller.recuperaAsteHome(indice);
    }


    @GetMapping ("/getDatiUtentePersonali")
    public UtenteDTO getDatiUtentePersonali(@RequestParam String mailUtente){
        return controller.getDatiUtentePersonali(mailUtente);
    }

    @PostMapping("/postCreaAsta")
    public void postAsta(@RequestBody AstaDTO astaDTO){
        byte[] immagineProdotto = {1, 0, 1, 0, 0, 0, 0, 1};
        System.out.println("Chiamata post asta intercettata, titolo asta: "+ astaDTO.titolo);
        LocalDateTime dataScadenza = LocalDateTime.of(astaDTO.anno, astaDTO.mese, astaDTO.giorno, 23, 59, 59);
        controller.creaAsta(astaDTO.emailCreatore, astaDTO.titolo, astaDTO.categoria, astaDTO.prezzoPartenza, dataScadenza, astaDTO.descrizione, immagineProdotto, astaDTO.ultimaOfferta, astaDTO.sogliaMinima, astaDTO.tipoAsta);
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

        //Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(utenteAutenticazioneDTO.mail,utenteAutenticazioneDTO.password));


        String jwsToken;



        int status = controller.controllerUtenti.aggiungiUtenteDAO(utenteAutenticazioneDTO.mail, utenteAutenticazioneDTO.password, utenteAutenticazioneDTO.nome, utenteAutenticazioneDTO.cognome, utenteAutenticazioneDTO.codiceFiscale, utenteAutenticazioneDTO.nazione, utenteAutenticazioneDTO.numeroCellulare, dataNascita, contatti, "", immagineProdotto, indirizzoVuoto, indirizzoVuoto);
        if(status==1){ //Utente creato con successo
            try {
                jwsToken = Jwts.builder()
                        .setIssuer("Stormpath")
                        .setSubject("msilverman")
                        .claim("mail", utenteAutenticazioneDTO.mail)
                        .claim("password", utenteAutenticazioneDTO.password)
                        // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
                        .setIssuedAt(Date.from(Instant.now()))
                        // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
                        .setExpiration(Date.from(Instant.now().plusSeconds(120)))
                        .signWith(
                                SignatureAlgorithm.HS256,
                                "secretMagnificoBellissmoDelMondoCheVerraGiuroSuMioZio".getBytes("UTF-8")
                        )
                        .compact();
            } catch (UnsupportedEncodingException e) {
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
        System.out.println("Sono Tornato dal Dao! Lo status e': "+status);
        String jwsToken;
        if(status==1){ //Utente verificato con successo
            try {

                jwsToken = Jwts.builder()
                        .setIssuer("Stormpath")
                        .setSubject("msilverman")
                        .claim("mail", utenteAutenticazioneDTO.mail)
                        .claim("password", utenteAutenticazioneDTO.password)
                        // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
                        .setIssuedAt(Date.from(Instant.now()))
                        // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
                        .setExpiration(Date.from(Instant.now().plusSeconds(120)))
                        .signWith(
                                SignatureAlgorithm.HS256,
                                "secretMagnificoBellissmoDelMondoCheVerraGiuroSuMioZio".getBytes("UTF-8")
                        )
                        .compact();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            Claims claims = JwtUtil.decodeJWT(jwsToken);
            System.out.println(claims);


            MyToken myToken = new MyToken(jwsToken);
            System.out.println(myToken.getToken());
            return myToken;
        }
        else{ //errore durante l'autenticazione dell'utente
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore durante la registrazione!");

        }
    }







    @GetMapping ("/getAsteUtente")
    public ArrayList<AstaDTO> getAsteHome(@RequestParam String email){
        System.out.println("Chiamata get aste per email utente intercettata, email: " + email);
        return controller.getAsteByEmailUtente(email); }

}
