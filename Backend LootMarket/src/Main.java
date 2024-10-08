import Controller.Controller;
import Modelli.Asta;
import Modelli.Contatti;
import Modelli.Indirizzo;

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
        controller.databaseUtenti.get(0);
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
            input = scanner.nextInt();
            if(input == 1){
                controller.creaAsta("danilo@mail.it", "Action Figure Naruto", "Action Figure", 50.00, LocalDateTime.now().plusDays(6), "Action figure bellissimissima di Naruto Uzumaki", immagineProdotto, 50.00, 0, "Asta Inversa");
            }else if(input == 2){
                controller.aggiungiUtenteDAO("danilo@mail.it", "danilo", "Danilo", "Pellecchia", "DLLLDD44", "Italia", "3785t39", LocalDate.of(1997, 12, 30), contatti, "Sono Danilo e voglio diventare scrittore", immagineProdotto, indirizzo, indirizzo);
            }else if(input == 3){
                controller.iscrizione("danilo@mail.it", 0);
            }else if(input == 4){
                int x = controller.nuovaOfferta("danilo@mail.it", 18, 20.00);
                if(x==1){
                    System.out.println("Tuuttok!");
                }else{
                    System.out.println("OOOOPS!");
                }

            }else if(input == 5){
                ArrayList<Asta> arrayAste = new ArrayList<>();
                arrayAste = controller.recuperaAsteHome(0);
                for(int i = 0; i < arrayAste.size(); i++){
                    System.out.println(arrayAste.get(i).getIdAsta());
                }
            }else if(input == 6){
                controller.concludiAstaDAO(15, "danilo@mail.it", 100.00);
            }else{
                System.out.println("Ciao!");
            }
        }



    }
}