package police.admin;

import police.Main;
import police.datebase.DatebaseManager;
import police.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import police.policeman.PolicemanData;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    TableColumn<User, String> userFirstNameColumn;

    @FXML
    TableColumn<User, String> userLastNameColumn;

    @FXML
    TableColumn<User, Boolean> adminColumn;


    private ObservableList<User> users = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTableView.setEditable(true);

        // adminColumn.setCellValueFactory(param -> param.getValue().valueProperty());
        // adminColumn.setCellFactory(CheckBoxTableCell.forTableColumn(adminColumn));

        loadDataToGrid();
    }


    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }
    public void addNewUser(ActionEvent actionEvent) throws Exception {

    }





    private void loadDataToGrid() {
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


    private static final String usersQuery = "SELECT ul.user_name,\n" +
            "\tsl.value,\n" +
            "\tsl.name AS role_name\n" +
            "FROM pg_roles pr\n" +
            "JOIN public.user_label ul ON ul.user_name = pr.rolname\n" +
            "JOIN public.security_label sl ON sl.id = ul.security_label_id;";

}
