package com.example.lootmarketbackend;

import com.example.lootmarketbackend.Controller.Controller;
import com.example.lootmarketbackend.Modelli.Asta;
import com.example.lootmarketbackend.Modelli.Contatti;
import com.example.lootmarketbackend.Modelli.Indirizzo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Controller controller = new Controller();

        controller.controllerUtenti.getUtenteDatabase(0);
        System.out.printf("Hello and welcome!\n");
        int input = 1;
        byte[] immagineProdotto = {1, 0, 1, 0, 0, 0, 0, 1};
        Contatti contatti = new Contatti("sitodanilo.it", "danilofb", "daniloig");
        Indirizzo indirizzo = new Indirizzo("Via Claudio", "Napoli", "NA", "80125");
        while(input != 0){
            System.out.printf("Cosa vuoi fare?\n");
            System.out.println("0) Chiudi");
            System.out.println("1) Aggiungi un'asta");
            System.out.println("2) Aggiungi un utente");
            System.out.println("3) Iscriviti ad un'asta");
            System.out.println("4) Presenta un'offerta ad un'asta");
            System.out.println("5) Recupera 10 aste");
            System.out.println("6) Concludi Asta");
            System.out.println("7) Modifica utente");
            System.out.println("8) Passaggio utente a Business");
            input = scanner.nextInt();
            if(input == 1){
                controller.creaAsta("danilo@mail.it", "Action Figure Naruto", "Action Figure", 50.00, LocalDateTime.now().plusDays(6), "Action figure bellissimissima di Naruto Uzumaki", immagineProdotto, 50.00, 0, "Asta Inversa");
            }else if(input == 2){
                controller.controllerUtenti.aggiungiUtenteDAO("danilo@mail.it", "danilo", "Danilo", "Pellecchia", "DLLLDD44", "Italia", "3785t39", LocalDate.of(1997, 12, 30), contatti, "Sono Danilo e voglio diventare scrittore", immagineProdotto, indirizzo, indirizzo);
            }else if(input == 3){
                controller.iscrizione("danilo@mail.it", 0);
            }else if(input == 4){
                int x = controller.nuovaOfferta("danilo@mail.it", 18, 10.00);
                if(x==1){
                    System.out.println("Tuuttok!");
                }else{
                    System.out.println("OOOOPS!");
                }

            }else if(input == 5){
                //ArrayList<Asta> arrayAste = new ArrayList<>();
                //arrayAste = controller.recuperaAsteHome(0);
                //for(int i = 0; i < arrayAste.size(); i++){
                  //  System.out.println(arrayAste.get(i).getIdAsta());
                //}
            }else if(input == 6){
                controller.controllerAste.concludiAstaDAO(15, "danilo@mail.it", 100.00);
            }else if(input == 7){
                controller.controllerUtenti.modificaUtenteDAO("mariaelena@mail.com", "Germania", "0000", contatti, "nuova bio", immagineProdotto, indirizzo, indirizzo, "01939492841");
            }else if(input == 8){
                controller.controllerUtenti.upgradeUtenteDAO("danilo@mail.it", "ragione sociale 1", "00001", "42893595");
            }else{
                System.out.println("Ciao!");
            }
        }

    }
}