package police;


import javafx.fxml.Initializable;
import police.accountant.AccountantData;
import police.accountant.AccountantPanel;
import police.accountant.upsert_accountant.UpsertAccountantPanel;
import police.admin.AdminPanel;
import police.commander.CommanderData;
import police.commander.CommanderPanel;
import police.commander.upsert_commander.UpsertCommanderPanel;
import police.datebase.DatebaseManager;
import police.dispatcher.DispatcherData;
import police.dispatcher.DispatcherPanel;
import police.dispatcher.upsert_dispatcher.UpsertDispatcherPanel;
import police.login.ChangePasswordPanel;
import police.news.NewsData;
import police.news.NewsPanel;
import police.news.upsert_news.UpsertNewsPanel;
import police.policeman.PolicemanData;
import police.policeman.PolicemanPanel;
import police.login.Login;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import police.policeman.upsert_policeman.UpsertPolicemanPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button addPolicemanButton;
    public Button addDispatcherButton;
    public Button addAccountantButton;
    public Button addCommanderButton;
    public Button addNewsButton;

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
                addNewsButton.setDisable(false);
                addPolicemanButton.setDisable(false);
                addDispatcherButton.setDisable(false);
                addAccountantButton.setDisable(false);
                addCommanderButton.setDisable(false);
                commanderButton.setDisable(false);
                accountantButton.setDisable(false);
                dispatcherButton.setDisable(false);
                policemanButton.setDisable(false);
                newsButton.setDisable(false);
                logoutButton.setDisable(false);
                changePasswordButton.setDisable(false);
                break;
            case 40 :
                commanderButton.setDisable(false);
                addNewsButton.setDisable(true);
                addPolicemanButton.setDisable(true);
                addDispatcherButton.setDisable(true);
                addAccountantButton.setDisable(true);
            case 30 :
                accountantButton.setDisable(false);
                addNewsButton.setDisable(true);
                addPolicemanButton.setDisable(true);
                addDispatcherButton.setDisable(true);
            case 20 :
                dispatcherButton.setDisable(false);
                addNewsButton.setDisable(true);
                addPolicemanButton.setDisable(true);
            case 10 :
                policemanButton.setDisable(false);
                addNewsButton.setDisable(true);
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

    public static final String userAccessesQuery = "SELECT sl.value \n" +
            "FROM public.user_label ul\n" +
            "JOIN public.security_label sl ON sl.id = ul.security_label_id\n" +
            "WHERE user_name = session_user;";

    public void changePassword(ActionEvent actionEvent) throws Exception {
        System.out.println("zmiena hasła");
        ChangePasswordPanel changePasswordPanel = new ChangePasswordPanel();
        changePasswordPanel.start((Stage) logoutButton.getScene().getWindow());
    }

    public void addPoliceman(ActionEvent actionEvent)  throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT last_value + 1 as id FROM policeman_id_seq;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        UpsertPolicemanPanel upsertPolicemanPanel = new UpsertPolicemanPanel();
        upsertPolicemanPanel.setPolicemanData(new PolicemanData(id,"","9999-12-31"));
        upsertPolicemanPanel.start((Stage) logoutButton.getScene().getWindow());
    }

    public void addDispatcher(ActionEvent actionEvent)  throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT last_value + 1 as id FROM dispatcher_id_seq;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        String date = getFormattedDate();
        UpsertDispatcherPanel upsertDispatcherPanel = new UpsertDispatcherPanel();
        upsertDispatcherPanel.setDispatcherData(new DispatcherData(id,"", date, ""));
        upsertDispatcherPanel.start((Stage) logoutButton.getScene().getWindow());
    }

    public void addAccountant(ActionEvent actionEvent)  throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT last_value + 1 as id FROM accountant_id_seq;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        String date = getFormattedDate();
        UpsertAccountantPanel upsertAccountantPanel = new UpsertAccountantPanel();
        upsertAccountantPanel.setAccountantData(new AccountantData(id,"", date));
        upsertAccountantPanel.start((Stage) logoutButton.getScene().getWindow());
    }

    public void addCommander(ActionEvent actionEvent)  throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT last_value + 1 as id FROM commander_id_seq;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        String date = getFormattedDate();
        UpsertCommanderPanel upsertCommanderPanel = new UpsertCommanderPanel();
        upsertCommanderPanel.setCommanderData(new CommanderData(id,"",date));
        upsertCommanderPanel.start((Stage) logoutButton.getScene().getWindow());
    }

    public void addNews(ActionEvent actionEvent) throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT last_value + 1 as id FROM announcement_id_seq;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        UpsertNewsPanel upsertNewsPanel = new UpsertNewsPanel();
        upsertNewsPanel.setNewsData(new NewsData(id,"","9999-12-31"));
        upsertNewsPanel.start((Stage) logoutButton.getScene().getWindow());
    }
    public String getFormattedDate(){
        Date date = new Date();
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return stringDate;
    }
}
