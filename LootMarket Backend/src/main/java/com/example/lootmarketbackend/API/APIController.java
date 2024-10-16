package com.example.lootmarketbackend.API;

import com.example.lootmarketbackend.Controller.Controller;
import com.example.lootmarketbackend.Modelli.Asta;
import com.example.lootmarketbackend.dto.AstaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class APIController {

    @Autowired
    public APIController(Controller controller){
        this.controller = controller;
    }

    private Controller controller;

    @GetMapping ("/getAsteHome")
    public ArrayList<AstaDTO> getAsteHome(@RequestParam Integer indice){
        return controller.recuperaAsteHome(indice);
    }

    @GetMapping("/postCreaAsta")
    public void postAsta(@RequestParam String emailCreatore,
                         @RequestParam String titolo,
                         @RequestParam String categoria,
                         @RequestParam double prezzoPartenza,
                         @RequestParam int anno,
                         @RequestParam int mese,
                         @RequestParam int giorno,
                         @RequestParam String descrizione,
                         @RequestParam byte[] immagineProdotto,
                         @RequestParam double ultimaOfferta,
                         @RequestParam double sogliaMinima,
                         @RequestParam String tipoAsta ){
        LocalDateTime dataScadenza = LocalDateTime.of(anno, mese, giorno, 23, 59, 59);
        controller.creaAsta(emailCreatore, titolo, categoria, prezzoPartenza, dataScadenza, descrizione, immagineProdotto, ultimaOfferta, sogliaMinima, tipoAsta);
    }

}
