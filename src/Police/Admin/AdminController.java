package Police.Admin;

import Police.Main;
import Police.model.User;
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

import java.net.URL;
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
    TableView<User> userTableView;

    @FXML
    TableColumn<User, String> userFirstNameColumn;

    @FXML
    TableColumn<User, String> userLastNameColumn;

    @FXML
    TableColumn<User, Boolean> adminColumn;


    private ObservableList<User> users = FXCollections.observableArrayList();
    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTableView.setEditable(true);
        adminColumn.setCellValueFactory(param -> param.getValue().adminProperty());
        adminColumn.setCellFactory(CheckBoxTableCell.forTableColumn(adminColumn));

        List<User> userList = new ArrayList<>();
        userList.add(new User("Jan", "Kowalski", true));
        userList.add(new User("Bozena", "Dykier", false));

        users.addAll(userList);
        userTableView.setItems(users);

    }
}
