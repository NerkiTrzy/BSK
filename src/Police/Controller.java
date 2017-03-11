package Police;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class Controller {

    @FXML
    private Button policemanButton;


    public void showPolicemanPanel(ActionEvent actionEvent) {
        System.out.printf("Stać Policja!\n");
    }

    public void showDistributorPanel(ActionEvent actionEvent) {
        System.out.printf("Rozsyłam patrole!\n");
    }

    public void showAccountantPanel(ActionEvent actionEvent) {
        System.out.printf("Rozliczam obywateli\n");
    }

    public void showCommanderPanel(ActionEvent actionEvent) {
        System.out.printf("Rozkazuję podwładnym\n");
    }

    public void showAdminPanel(ActionEvent actionEvent) {
        System.out.printf("Zarządzam uprawnieniami\n");
    }

    public void showNewsPanel(ActionEvent actionEvent) {
        System.out.printf("Nowości w Policji!\n");
    }
}
