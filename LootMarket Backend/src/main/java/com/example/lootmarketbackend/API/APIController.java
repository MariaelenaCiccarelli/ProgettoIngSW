package com.example.lootmarketbackend.API;

import com.example.lootmarketbackend.Controller.Controller;
import com.example.lootmarketbackend.Modelli.Asta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class APIController {

    @Autowired
    public APIController(Controller controller){
        this.controller = controller;
    }

    private Controller controller;

    @GetMapping ("/getAsteHome")
    public ArrayList<Asta> getAsteHome(@RequestParam Integer indice){
        return controller.recuperaAsteHome(indice);
    }

}
