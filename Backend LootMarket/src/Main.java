import Controller.Controller;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Controller controller = new Controller();
        controller.databaseUtenti.get(0);
        System.out.printf("Hello and welcome!\n");
        System.out.printf(controller.databaseUtenti.get(0).getEmail());


    }
}