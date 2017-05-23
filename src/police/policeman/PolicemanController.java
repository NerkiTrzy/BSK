package police.policeman;

import javafx.scene.control.*;
import police.Controller;
import police.datebase.DatebaseManager;
import police.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import police.model.User;
import police.news.NewsData;
import police.news.upsert_news.UpsertNewsPanel;
import police.policeman.upsert_policeman.UpsertPolicemanPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class PolicemanController implements Initializable {
    public Button editPolicemanButton;
    public Button deletePolicemanButton;
    @FXML
    private Button backButton;
    @FXML
    TableView<PolicemanData> policemanTableView;

    @FXML
    TableColumn<PolicemanData, Integer> policemanIdColumn;
    @FXML
    TableColumn<PolicemanData, String> policemanNameColumn;
    @FXML
    TableColumn<PolicemanData, String> policemanBirthDateColumn;

    private ObservableList<PolicemanData> policemanDataSets = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        checkLabelsForUser();

        policemanTableView.setEditable(true);

        loadDataToGrid();
    }



    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }

    private void loadDataToGrid() {
        policemanTableView.getItems().clear();
        policemanDataSets.clear();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM policeman ORDER BY id;" );

            List<PolicemanData> policemanDataList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String date = rs.getDate("birth").toString();

                policemanDataList.add(new PolicemanData(id, name, date));

            }
            policemanDataSets.addAll(policemanDataList);
            policemanTableView.setItems(policemanDataSets);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    public void addNewPoliceman(ActionEvent actionEvent) throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT max(id) + 1 as id FROM policeman;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        UpsertPolicemanPanel upsertPolicemanPanel = new UpsertPolicemanPanel();
        upsertPolicemanPanel.setPolicemanData(new PolicemanData(id,"","9999-12-31"));
        upsertPolicemanPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void editPoliceman(ActionEvent actionEvent) throws Exception {
        UpsertPolicemanPanel upsertPolicemanPanel = new UpsertPolicemanPanel();
        upsertPolicemanPanel.setPolicemanData(policemanTableView.getSelectionModel().getSelectedItem());
        upsertPolicemanPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void deletePoliceman(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuniecie wpisu");
        alert.setHeaderText("Potwierdz usuniecie");
        alert.setContentText("Czy na pewno chcesz usunac?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                int id = policemanDataSets.get(policemanTableView.getSelectionModel().getFocusedIndex()).getId();
                Statement statement = DatebaseManager.getConnection().createStatement();
                statement.execute( "DELETE FROM policeman WHERE id = " + id + ";" );
                loadDataToGrid();
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            }
        }
    }

    private void checkLabelsForUser() {
        int userLabel = 0;
        int tableLabel = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(Controller.userAccessesQuery);
            rs.next();
            userLabel = rs.getInt("value");

            rs = statement.executeQuery("SELECT sl.value\n" +
                        "FROM tables_labels tl\n" +
                        "JOIN security_label sl ON sl.id = tl.security_label_id\n" +
                        "WHERE tl.table_name = 'policeman'");
            rs.next();
            tableLabel = rs.getInt("value");

            if (userLabel > tableLabel) {
                editPolicemanButton.setDisable(true);
                deletePolicemanButton.setDisable(true);
            }
            else if(userLabel == tableLabel){
                editPolicemanButton.setDisable(false);
                deletePolicemanButton.setDisable(false);
            }
            else{
                editPolicemanButton.setDisable(true);
                deletePolicemanButton.setDisable(true);
            }

        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }
}
