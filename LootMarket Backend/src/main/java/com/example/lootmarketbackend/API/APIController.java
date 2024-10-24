package com.example.lootmarketbackend.API;

import com.example.lootmarketbackend.Controller.Controller;
import com.example.lootmarketbackend.Modelli.Asta;
import com.example.lootmarketbackend.dto.AstaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    @PostMapping("/postCreaAsta")
    public void postAsta(@RequestBody AstaDTO astaDTO){
        byte[] immagineProdotto = {1, 0, 1, 0, 0, 0, 0, 1};
        System.out.println("Chiamata post asta intercettata, titolo asta: "+ astaDTO.titolo);
        LocalDateTime dataScadenza = LocalDateTime.of(astaDTO.anno, astaDTO.mese, astaDTO.giorno, 23, 59, 59);
        controller.creaAsta(astaDTO.emailCreatore, astaDTO.titolo, astaDTO.categoria, astaDTO.prezzoPartenza, dataScadenza, astaDTO.descrizione, immagineProdotto, astaDTO.ultimaOfferta, astaDTO.sogliaMinima, astaDTO.tipoAsta);
    }

    @GetMapping ("/getAsteUtente")
    public ArrayList<AstaDTO> getAsteHome(@RequestParam String email){
        System.out.println("Chiamata get aste per email utente intercettata, email: " + email);
        return controller.getAsteByEmailUtente(email); }

}
