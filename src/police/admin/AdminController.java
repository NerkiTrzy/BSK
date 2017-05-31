package police.admin;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import police.Controller;
import police.Main;
import police.admin.newbie.NewbieController;
import police.admin.newbie.NewbiePanel;
import police.datebase.DatebaseManager;
import police.model.Table;
import police.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import police.policeman.PolicemanData;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Przemysław on 2017-03-11.
 */
public class AdminController implements Initializable{
    public Button saveButton;

    public Button deleteUserButton;
    public Button refreshTablesButton;
    public Button refreshUsersButton;
    @FXML
    private Button backButton;
    @FXML
    private Button newUserButton;

    @FXML
    TableView<User> userTableView;



    @FXML
    TableColumn<User, String> userNameColumn;

    @FXML
    TableColumn<User, String> valueColumn;


    @FXML
    TableView<Table> tablesTableView;
    @FXML
    TableColumn<Table, String> tablesNameColumn;
    @FXML
    TableColumn<Table, String> tablesValueColumn;

    private List<String> changedList;


    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Table> tables = FXCollections.observableArrayList();
    private ObservableList<Integer> labelValues = FXCollections.observableArrayList();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changedList = new ArrayList<>();
        changedList.clear();
        userTableView.setEditable(true);

        valueColumn.setEditable(true);
        //valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

       // tablesValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        //roleNameColumn.setEditable(false);


        valueColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> event) {
                int index = labelValues.indexOf(event.getNewValue());
                int row = event.getTablePosition().getRow();
                User user = userTableView.getItems().get(row);
                Integer newValue = labelValues.get(index);
                user.setValue(newValue);
               changedList.add(user.getUserName());
            }
        });

        checkLabelsForUser();
    }

    private void checkLabelsForUser() {
        int userLabel = 0;
        int tableLabel = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(Controller.userAccessesQuery);
            rs.next();
            userLabel = rs.getInt("value");

            rs = statement.executeQuery("SELECT tl.label_value as value\n" +
                    "FROM tables_labels tl\n" +
                    "WHERE tl.table_name = 'tables_labels'");
            rs.next();
            tableLabel = rs.getInt("value");

            if (userLabel > tableLabel) {
                saveButton.setDisable(true);
                deleteUserButton.setDisable(true);
                refreshTablesButton.setDisable(false);
                refreshUsersButton.setDisable(false);
                newUserButton.setDisable(true);

            }
            else if(userLabel == tableLabel){
                saveButton.setDisable(false);
                deleteUserButton.setDisable(false);
                refreshTablesButton.setDisable(false);
                refreshUsersButton.setDisable(false);
                newUserButton.setDisable(false);
            }
            else{
                saveButton.setDisable(true);
                deleteUserButton.setDisable(true);
                refreshTablesButton.setDisable(true);
                refreshUsersButton.setDisable(true);
                newUserButton.setDisable(false);
            }

        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }


    public void backToMainMenu(ActionEvent actionEvent) throws Exception {

        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }
    public void addNewUser(ActionEvent actionEvent) throws Exception {
        NewbiePanel newbiePanel = new NewbiePanel(this);
        newbiePanel.start((Stage) backButton.getScene().getWindow());
        //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


    private static final String usersQuery = "SELECT ul.user_name,\n" +
            "\tul.label_value as \"value\" \n" +
            "FROM pg_roles pr\n" +
            "JOIN public.user_label ul ON ul.user_name = pr.rolname\n" +
            "ORDER BY 2 DESC";

    private static final String tablesQuery = "SELECT tl.table_name,\n" +
            "\ttl.label_value as \"value\" \n" +
            "FROM information_schema.tables t\n" +
            "JOIN public.tables_labels tl ON tl.table_name = t.table_name\n" +
            "WHERE t.table_schema = 'public' \n" +
            "AND tl.table_name NOT IN ('tables_labels','user_labels') \n" +
            "ORDER BY 2 DESC";


    public void deleteUser(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuniecie wpisu");
        alert.setHeaderText("Potwierdz usuniecie");
        alert.setContentText("Czy na pewno chcesz usunac?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                String userName = users.get(userTableView.getSelectionModel().getFocusedIndex()).getUserName();


                Statement statement = DatebaseManager.getConnection().createStatement();
                statement.execute( "REVOKE ALL ON policeman FROM " + userName + ";" );
                statement.execute( "REVOKE ALL ON accountant FROM " + userName + ";" );
                statement.execute( "REVOKE ALL ON commander FROM " + userName + ";" );
                statement.execute( "REVOKE ALL ON dispatcher FROM " + userName + ";" );
                statement.execute( "REVOKE ALL ON announcement FROM " + userName + ";" );
                statement.execute( "REVOKE ALL ON tables_labels FROM " + userName + ";" );
                statement.execute( "REVOKE ALL ON user_label FROM " + userName + ";" );

                statement.execute( "DELETE FROM user_label WHERE user_name = '" + userName + "';" );
                statement.execute( "DROP ROLE \"" + userName + "\";" );
                refreshUsers(actionEvent);
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            }
        }
    }

    public void saveGrid(ActionEvent actionEvent) {
        for (String userName : changedList) {
            for (int i = 0 ; i < userTableView.getItems().size() ; i++){
                if (userTableView.getItems().get(i).getUserName().equals(userName)){
                    updateUser(userTableView.getItems().get(i));
                }
            }
        }
    }

    private void updateUser(User user) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute( "UPDATE user_label\n" +
                                    "SET label_value = " + String.valueOf(user.getValue()) + "\n" +
                                    "WHERE user_name = '" + user.getUserName() + "'" );
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        NewbieController.giveGrantsToUser(user.getUserName(), user.getValue());
    }

    public void refreshUsers(ActionEvent actionEvent) {
        userTableView.getItems().clear();

        users.clear();

        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( usersQuery );

            List<User> userList = new ArrayList<>();
            while (rs.next()) {

                String userName = rs.getString("user_name");
                int value = rs.getInt("value");

                userList.add(new User(userName, value));

            }
            users.addAll(userList);
            userTableView.setItems(users);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    public void refreshTables(ActionEvent actionEvent) {
        tablesTableView.getItems().clear();
        tables.clear();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( tablesQuery );
            List<Table> tableList = new ArrayList<>();
            while (rs.next()) {

                String tableName = rs.getString("table_name");
                int value = rs.getInt("value");

                tableList.add(new Table(tableName, value));

            }
            tables.addAll(tableList);
            tablesTableView.setItems(tables);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }
}
