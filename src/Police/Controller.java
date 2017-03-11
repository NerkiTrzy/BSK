package Police;


import Police.Admin.AdminPanel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

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

    public void showAdminPanel(ActionEvent actionEvent) throws Exception {
        System.out.printf("Zarządzam uprawnieniami\n");
        AdminPanel adminPanel = new AdminPanel();
        adminPanel.start((Stage) policemanButton.getScene().getWindow());
    }

    public void showNewsPanel(ActionEvent actionEvent) {
        System.out.printf("Nowości w Policji!\n");
    }
}
