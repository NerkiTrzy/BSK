package Police;


import Police.Accountant.AccountantPanel;
import Police.Admin.AdminPanel;
import Police.Commander.CommanderPanel;
import Police.Datebase.DatebaseManager;
import Police.Distributor.DispatcherPanel;
import Police.News.NewsPanel;
import Police.Policeman.PolicemanPanel;
import Police.login.Login;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private Button policemanButton;
    @FXML
    private Button adminButton;
    @FXML
    private Button newsButton;
    @FXML
    private Button dispatcherButton;
    @FXML
    private Button accountantButton;
    @FXML
    private Button commanderButton;
    @FXML
    private Button logoutButton;

    public Controller() {

    }

    public void showPolicemanPanel(ActionEvent actionEvent) throws  Exception{
        System.out.printf("Stać Policja!\n");
        PolicemanPanel policemanPanel = new PolicemanPanel();
        policemanPanel.start((Stage) policemanButton.getScene().getWindow());
    }

    public void showDispatcherPanel(ActionEvent actionEvent) throws Exception {
        System.out.printf("Rozsyłam patrole!\n");
        DispatcherPanel dispatcherPanel = new DispatcherPanel();
        dispatcherPanel.start((Stage) dispatcherButton.getScene().getWindow());
    }

    public void showAccountantPanel(ActionEvent actionEvent) throws Exception {
        System.out.printf("Rozliczam obywateli\n");
        AccountantPanel accountantPanel = new AccountantPanel();
        accountantPanel.start((Stage) accountantButton.getScene().getWindow());
    }

    public void showCommanderPanel(ActionEvent actionEvent) throws Exception {
        System.out.printf("Rozkazuję podwładnym\n");
        CommanderPanel commanderPanel = new CommanderPanel();
        commanderPanel.start((Stage) commanderButton.getScene().getWindow());
    }

    public void showAdminPanel(ActionEvent actionEvent) throws Exception {
        System.out.printf("Zarządzam uprawnieniami\n");
        AdminPanel adminPanel = new AdminPanel();
        adminPanel.start((Stage) adminButton.getScene().getWindow());
    }

    public void showNewsPanel(ActionEvent actionEvent) throws Exception{
        System.out.printf("Nowości w Policji!\n");
        NewsPanel newsPanel = new NewsPanel();
        newsPanel.start((Stage) newsButton.getScene().getWindow());
    }

    public void logout(ActionEvent actionEvent) throws Exception {
        Login login = new Login();
        login.start((Stage) logoutButton.getScene().getWindow());
    }


}
