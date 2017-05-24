package police.admin.newbie;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import police.Main;
import police.admin.AdminController;
import police.admin.AdminPanel;
import police.datebase.DatebaseManager;
import police.model.User;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by Przemysław on 2017-04-18.
 */
public class NewbieController implements Initializable{
    @FXML
    private Button cancelButton;
    @FXML
    JFXTextField loginTextField;

    @FXML
    JFXPasswordField passwordTextField;
    @FXML
    JFXPasswordField passwordRepeatTextField;

    @FXML
    ComboBox<String> labelComboBox;
    private ObservableList<String> labels = FXCollections.observableArrayList();
    private Map<String, Integer> valuesOfLabels;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        valuesOfLabels = new HashMap<>();
        valuesOfLabels.clear();
        labels.addAll(getLabelList());
        labelComboBox.setItems(labels);
    }

    private List<String> getLabelList() {
        String labelListQuery = "SELECT name || ' ' || value as name,\n" +
                "value as value \n" +
                "FROM public.security_label sl\n" +
                "ORDER BY value;";

        List<String > labelList = new ArrayList<>();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( labelListQuery );

            while (rs.next()) {
                String labelName = rs.getString("name");
                int value = rs.getInt("value");
                labelList.add(labelName);
                valuesOfLabels.put(labelName,value);
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return labelList;
    }


    public void cancel(ActionEvent actionEvent) throws Exception {;
        AdminPanel adminPanel = new AdminPanel();
        adminPanel.start((Stage) cancelButton.getScene().getWindow());
    }

    public void confirm(ActionEvent actionEvent) throws Exception {
        if (checkNewUserConditions()){
            insertNewUser();
            cancel(actionEvent);
        }
    }

    private void insertNewUser() {
        String insertNewUserQuery = "CREATE ROLE " +
                loginTextField.getText()
                + " WITH PASSWORD '" +
                passwordTextField.getText()
                + "' LOGIN ;";

        String addLabelToNewUserQuery = "INSERT INTO public.user_label\n" +
                "(id, user_name, security_label_id)\n" +
                "SELECT\n" +
                "\t(SELECT max(id) + 1 FROM user_label),\n" +
                "\tpr.rolname,\n" +
                "\tsl.id\n" +
                "FROM pg_roles pr, public.security_label sl\n" +
                "WHERE pr.rolname = '" + loginTextField.getText() + "'\n" +
                "AND sl.name = '" + labelComboBox.valueProperty().get().split(" ")[0] + "';";

        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute( insertNewUserQuery );
            statement.execute( addLabelToNewUserQuery );

            giveGrantsToUser(loginTextField.getText(), valuesOfLabels.get(labelComboBox.valueProperty().get()));

        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private boolean checkNewUserConditions() {
        return (checkValidLogin() && checkValidPassword());
    }

    private boolean checkValidLogin() {
        String message = "";

        if (loginTextField.getText().length() < 4){
            message += "Podany login za krótki! \nWymagane conajmniej 4 znaki\n";
        }
        if (!checkLoginUniqueness()){
            message += "Podany login już istnieje\n";
        }
        if (!message.equals("")){
            JOptionPane.showMessageDialog(null, message);
            return false;
        }
        return true;
    }

    private boolean checkLoginUniqueness() {
        String checkLoginUniquenessQuery = "SELECT *\n" +
                                            "FROM pg_roles\n" +
                                            "WHERE rolname = '" + loginTextField.getText() + "';";
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( checkLoginUniquenessQuery );
            if (rs.getFetchSize() > 0) {
                return false;
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            return false;
        }
        return true;
    }

    private boolean checkValidPassword() {
        String message = "";

        if (passwordTextField.getText().length() < 5){
            message += "Podane hasło jest zbyt krótkie!\nWymagane conajmniej 5 znaków.\n";
        }
        if (!passwordTextField.getText().equals(passwordRepeatTextField.getText())){
            message += "Podane hasła się różnią!\n";
        }

        if (!message.equals("")){
            JOptionPane.showMessageDialog(null, message);
            return false;
        }
        return true;
    }

    public static void giveGrantsToUser(String userLogin, int userLabelValue){
        List<String> grants = new ArrayList<>();
        grants.clear();
        grants.add("REVOKE ALL ON policeman FROM " + userLogin + ";");
        grants.add("REVOKE ALL ON announcement FROM " + userLogin + ";");
        grants.add("REVOKE ALL ON dispatcher FROM " + userLogin + ";");
        grants.add("REVOKE ALL ON commander FROM " + userLogin + ";");
        grants.add("REVOKE ALL ON accountant FROM " + userLogin + ";");

        switch (userLabelValue) {
            case 50 :
                grants.add("GRANT ALL ON commander TO " + userLogin + ";");
                grants.add("GRANT ALL ON accountant TO " + userLogin + ";");
                grants.add("GRANT ALL ON dispatcher TO " + userLogin + ";");
                grants.add("GRANT ALL ON policeman TO " + userLogin + ";");
                grants.add("GRANT ALL ON announcement TO " + userLogin + ";");
            case 40 :
                grants.add("GRANT ALL ON commander TO " + userLogin + ";");
                grants.add("GRANT SELECT ON accountant TO " + userLogin + ";");
                grants.add("GRANT SELECT ON dispatcher TO " + userLogin + ";");
                grants.add("GRANT SELECT ON policeman TO " + userLogin + ";");
                grants.add("GRANT SELECT ON announcement TO " + userLogin + ";");
                break;
            case 30 :
                grants.add("GRANT INSERT ON commander TO " + userLogin + ";");
                grants.add("GRANT ALL ON accountant TO " + userLogin + ";");
                grants.add("GRANT SELECT ON dispatcher TO " + userLogin + ";");
                grants.add("GRANT SELECT ON policeman TO " + userLogin + ";");
                grants.add("GRANT SELECT ON announcement TO " + userLogin + ";");
                break;
            case 20 :
                grants.add("GRANT INSERT ON commander TO " + userLogin + ";");
                grants.add("GRANT INSERT ON accountant TO " + userLogin + ";");
                grants.add("GRANT ALL ON dispatcher TO " + userLogin + ";");
                grants.add("GRANT SELECT ON policeman TO " + userLogin + ";");
                grants.add("GRANT SELECT ON announcement TO " + userLogin + ";");
                break;

            case 10 :
                grants.add("GRANT INSERT ON commander TO " + userLogin + ";");
                grants.add("GRANT INSERT ON accountant TO " + userLogin + ";");
                grants.add("GRANT INSERT ON dispatcher TO " + userLogin + ";");
                grants.add("GRANT ALL ON policeman TO " + userLogin + ";");
                grants.add("GRANT SELECT ON announcement TO " + userLogin + ";");
                break;

            case 0 :
                grants.add("GRANT INSERT ON commander TO " + userLogin + ";");
                grants.add("GRANT INSERT ON accountant TO " + userLogin + ";");
                grants.add("GRANT INSERT ON dispatcher TO " + userLogin + ";");
                grants.add("GRANT INSERT ON policeman TO " + userLogin + ";");
                grants.add("GRANT ALL ON announcement TO " + userLogin + ";");
                break;
            default:
                break;
        }
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            for (String query : grants) {
                statement.execute(query);
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

}
