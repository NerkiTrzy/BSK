package police;


import javafx.fxml.Initializable;
import police.accountant.AccountantPanel;
import police.admin.AdminPanel;
import police.commander.CommanderPanel;
import police.datebase.DatebaseManager;
import police.dispatcher.DispatcherPanel;
import police.news.NewsPanel;
import police.policeman.PolicemanPanel;
import police.login.Login;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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
    @FXML
    private  Button changePasswordButton;

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
        DatebaseManager.getConnection().close();
        Login login = new Login();
        login.start((Stage) logoutButton.getScene().getWindow());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAccesses();
    }

    private void loadAccesses() {
        disableAllButtons();


        int value = getUserValue();
        //switch specjalnie bez "break;" aby w odblokowywało się więcej przycisków
        switch (value)
        {
            case 50 :
                adminButton.setDisable(false);
            case 40 :
                commanderButton.setDisable(false);
            case 30 :
                accountantButton.setDisable(false);
            case 20 :
                dispatcherButton.setDisable(false);
            case 10 :
                policemanButton.setDisable(false);
            case 0 :
                newsButton.setDisable(false);
            default:
                logoutButton.setDisable(false);
                changePasswordButton.setDisable(false);
        }

    }

    private void disableAllButtons() {
        adminButton.setDisable(true);
        commanderButton.setDisable(true);
        accountantButton.setDisable(true);
        dispatcherButton.setDisable(true);
        policemanButton.setDisable(true);
        newsButton.setDisable(true);
        logoutButton.setDisable(true);
        changePasswordButton.setDisable(true);
    }

    private int getUserValue() {
        int value = -10;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( userAccessesQuery );
            rs.next();
            value = rs.getInt("value");
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return value;
    }

    private static final String userAccessesQuery = "SELECT sl.value \n" +
            "FROM public.user_label ul\n" +
            "JOIN public.security_label sl ON sl.id = ul.security_label_id\n" +
            "WHERE user_name = session_user;";

    public void changePassword(ActionEvent actionEvent) {
        System.out.println("zmiena hasła");
    }
}
