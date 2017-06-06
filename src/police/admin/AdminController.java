package police.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.converter.IntegerStringConverter;
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

import javax.swing.*;
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

    public TextField labelBottomFilter;
    public TextField labelTopFilter;
    public Label currentUserLabel;

    public TextField labelBottomTableFilter;
    public TextField labelTopTableFilter;
    public TextField labelTableName;

    public TextField labelUsername;
    public TextField userLimit;
    public TextField tableLimit;

    @FXML
    private Button backButton;
    @FXML
    private Button newUserButton;

    @FXML
    TableView<User> userTableView;



    @FXML
    TableColumn<User, String> userNameColumn;

    @FXML
    TableColumn<User, Integer> valueColumn;


    @FXML
    TableView<Table> tablesTableView;
    @FXML
    TableColumn<Table, String> tablesNameColumn;
    @FXML
    TableColumn<Table, Integer> tablesValueColumn;

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

        tablesTableView.setEditable(true);
        tablesValueColumn.setEditable(true);
        //tablesValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //tablesValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        tablesValueColumn.setCellFactory(TextFieldTableCell.<Table, Integer>forTableColumn(new IntegerStringConverter()));

        valueColumn.setEditable(true);
        valueColumn.setCellFactory(TextFieldTableCell.<User, Integer>forTableColumn(new IntegerStringConverter()));
        //tablesValueColumn.setCellFactory(param -> );
//        tablesValueColumn.setCellValueFactory(param -> param.getValue().valueProperty().asObject()));
//        valueColumn.setCellValueFactory(param -> param.getValue().valueProperty().asObject());
        //roleNameColumn.setEditable(false);

        allowOnlyNumbersForLabelFilters();

//        valueColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
//            @Override
//            public void handle(TableColumn.CellEditEvent<User, String> event) {
//                int index = labelValues.indexOf(event.getNewValue());
//                int row = event.getTablePosition().getRow();
//                User user = userTableView.getItems().get(row);
//                Integer newValue = labelValues.get(index);
//                user.setValue(newValue);
//               changedList.add(user.getUserName());
//            }
//        });

        checkLabelsForUser();

        setCurrentUserLabel();

    }

    private void setCurrentUserLabel() {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(Controller.userAccessesQuery);
            rs.next();
            currentUserLabel.setText(currentUserLabel.getText() + String.valueOf(rs.getInt("value")));
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private void allowOnlyNumbersForLabelFilters() {
        labelBottomFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    labelBottomFilter.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        labelTopFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    labelTopFilter.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        labelBottomTableFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    labelBottomTableFilter.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        labelTopTableFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    labelTopTableFilter.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        userLimit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    userLimit.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        tableLimit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tableLimit.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
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
                tablesValueColumn.setEditable(false);
                valueColumn.setEditable(false);

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
                tablesValueColumn.setEditable(false);
                valueColumn.setEditable(false);
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
            " ORDER BY 2 DESC";

    private static final String tablesQuery = "SELECT tl.table_name,\n" +
            "\ttl.label_value as \"value\" \n" +
            "FROM information_schema.tables t\n" +
            "JOIN public.tables_labels tl ON tl.table_name = t.table_name\n" +
            "WHERE t.table_schema = 'public' \n" +
            "AND tl.table_name NOT IN ('tables_labels', 'user_label') \n" +
            " ORDER BY 2 DESC";


    private String getUsersQuery() {
        String query = "SELECT ul.user_name,\n" +
                "\tul.label_value as \"value\" \n" +
                "FROM pg_roles pr\n" +
                "JOIN public.user_label ul ON ul.user_name = pr.rolname\n";
        query += " WHERE true";
        if(labelBottomFilter.getText().length() > 0) {
            query += " AND ul.label_value >= " + labelBottomFilter.getText();
        }
        if (labelTopFilter.getText().length() > 0) {
            query += " AND ul.label_value <= " + labelTopFilter.getText();
        }
        if (labelUsername.getText().length() > 0){
            query += " AND ul.user_name ILIKE '%" + labelUsername.getText() + "%' ";
        }
        query += " ORDER BY 2 DESC ";
        if (userLimit.getText().length() > 0){
            query += " LIMIT " + userLimit.getText();
        }
        return query;
    }

    private String getTablesQuery() {
        String query = "SELECT tl.table_name,\n" +
                "\ttl.label_value as \"value\" \n" +
                "FROM information_schema.tables t\n" +
                "JOIN public.tables_labels tl ON tl.table_name = t.table_name\n" +
                "WHERE t.table_schema = 'public' \n" +
                "AND tl.table_name NOT IN ('tables_labels','user_label') \n";

        if (labelBottomTableFilter.getText().length() > 0) {
            query += " AND tl.label_value >= " + labelBottomTableFilter.getText();
        }
        if (labelTopTableFilter.getText().length() > 0) {
            query += " AND tl.label_value <= " + labelTopTableFilter.getText();
        }
        if (labelTableName.getText().length() > 0) {
            query += " AND tl.table_name ILIKE '%" + labelTableName.getText() + "%'";
        }
        query += " ORDER BY 2 DESC ";
        if (tableLimit.getText().length() > 0){
            query += " LIMIT " + tableLimit.getText();
        }
        return query;
    }

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
                ResultSet rs = statement.executeQuery("SELECT COUNT(*) as v FROM user_label WHERE '" + userName + "' = current_user::text;\n ");

                rs.next();
                if (rs.getInt("v") > 0){
                    JOptionPane.showMessageDialog(null, "Nie można usunąć siebie");
                    return;
                }

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
//        for (String userName : changedList) {
//            for (int i = 0 ; i < userTableView.getItems().size() ; i++){
//                if (userTableView.getItems().get(i).getUserName().equals(userName)){
//                    updateUser(userTableView.getItems().get(i));
//                }
//            }
//        }
        for (int i = 0 ; i < userTableView.getItems().size() ; i++) {
            updateUser(userTableView.getItems().get(i));
        }

        for (int i = 0 ; i < tablesTableView.getItems().size() ; i++) {
            updateTable(tablesTableView.getItems().get(i));
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

    private void updateTable(Table table) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute( "UPDATE tables_labels\n" +
                    "SET label_value = " + String.valueOf(table.getValue()) + "\n" +
                    "WHERE table_name = '" + table.getTableName() + "'" );

            statement.execute("SELECT change_users_label( '" + table.getTableName() + "' , " + String.valueOf(table.getValue()) + ");");
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    public void refreshUsers(ActionEvent actionEvent) {
        userTableView.getItems().clear();

        users.clear();

        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( getUsersQuery() );

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
            ResultSet rs = statement.executeQuery( getTablesQuery() );
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
