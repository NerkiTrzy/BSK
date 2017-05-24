package police.admin;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import police.Main;
import police.admin.newbie.NewbiePanel;
import police.datebase.DatebaseManager;
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
    TableColumn<User, String> roleNameColumn;


    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Integer> labelValues = FXCollections.observableArrayList();
    private ObservableList<String> labelNames = FXCollections.observableArrayList();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTableView.setEditable(true);
        labelValues = getLabelValues();
        labelNames = getLabelNames();

        valueColumn.setCellValueFactory(param -> param.getValue().valueProperty().asObject());
        valueColumn.setCellFactory(ComboBoxTableCell.forTableColumn(labelValues));

        roleNameColumn.setCellValueFactory(param -> param.getValue().roleNameProperty());
        roleNameColumn.setCellFactory(ComboBoxTableCell.forTableColumn(labelNames));

        userTableView.getColumns().addListener(new ListChangeListener<TableColumn<User, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<User, ?>> c) {
                if (c.next()){
                    System.out.println(c.toString());
                }
            }
        });

        loadDataToGrid();
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



    private void loadDataToGrid() {
        userTableView.getItems().clear();
        users.clear();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( usersQuery );

            List<User> userList = new ArrayList<>();
            while (rs.next()) {

                String userName = rs.getString("user_name");
                int value = rs.getInt("value");
                String roleName = rs.getString("role_name");

                userList.add(new User(userName, value, roleName));

            }
            users.addAll(userList);
            userTableView.setItems(users);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private ObservableList<String> getLabelNames() {
        ObservableList<String> labelNames = FXCollections.observableArrayList();
        List<String> labelNameList = new ArrayList<>();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( labelNameQuery );

            while (rs.next()) {
                String labelName = rs.getString("name");

                labelNameList.add(labelName);
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        labelNames.setAll(labelNameList);
        return labelNames;
    }

    private ObservableList<Integer> getLabelValues() {
        ObservableList<Integer> labelValues = FXCollections.observableArrayList();
        List<Integer> labelList = new ArrayList<>();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( valuesQuery );

            while (rs.next()) {
                int value = rs.getInt("value");

                labelList.add(value);
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        labelValues.setAll(labelList);
        return labelValues;
    }

    private static final String usersQuery = "SELECT ul.user_name,\n" +
            "\tsl.value,\n" +
            "\tsl.name AS role_name\n" +
            "FROM pg_roles pr\n" +
            "JOIN public.user_label ul ON ul.user_name = pr.rolname\n" +
            "JOIN public.security_label sl ON sl.id = ul.security_label_id;";


    private static final String valuesQuery = "SELECT value\n" +
            "FROM public.security_label\n" +
            "ORDER BY value;";

    private static final String labelNameQuery = "SELECT name\n" +
            "FROM public.security_label\n" +
            "ORDER BY value;";

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
                statement.execute( "DELETE FROM user_label WHERE user_name = '" + userName + "';" );
                statement.execute( "DROP ROLE \"" + userName + "\";" );
                loadDataToGrid();
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            }
        }
    }

    public void saveGrid(ActionEvent actionEvent) {

    }
}
