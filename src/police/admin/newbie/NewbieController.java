package police.admin.newbie;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import police.Controller;
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
    public TextField labelTextField;
    @FXML
    private Button cancelButton;
    @FXML
    JFXTextField loginTextField;

    @FXML
    JFXPasswordField passwordTextField;
    @FXML
    JFXPasswordField passwordRepeatTextField;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        labelTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    labelTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
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
                "(id, user_name, label_value)\n" +
                "SELECT\n" +
                "\t(SELECT max(id) + 1 FROM user_label),\n" +
                "\tpr.rolname,\n" +
                "\t " + labelTextField.getText() + "\n" +
                "FROM pg_roles pr \n" +
                "WHERE pr.rolname = '" + loginTextField.getText() + "';\n";
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute( insertNewUserQuery );
            statement.execute( addLabelToNewUserQuery );

            giveGrantsToUser(loginTextField.getText(),Integer.parseInt( labelTextField.getText()));

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

        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT tl.table_name, tl.label_value as value\n" +
                    "FROM tables_labels tl; \n");

            while (rs.next()){
                String tableName = rs.getString("table_name");
                int tableValue = rs.getInt("value");

                if (tableValue > userLabelValue){
                    grants.add("REVOKE SELECT, UPDATE, DELETE ON " + tableName + " FROM " + userLogin + ";");
                    grants.add("GRANT INSERT ON " + tableName + " TO " + userLogin + ";");
                }
                else if (tableValue == userLabelValue) {
                    grants.add("GRANT ALL ON " + tableName + " TO " + userLogin + ";");
                }
                else{
                    grants.add("REVOKE INSERT, UPDATE, DELETE ON " + tableName + " FROM " + userLogin + ";");
                    grants.add("GRANT SELECT ON " + tableName + " TO " + userLogin + ";");
                }

            }

        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
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
