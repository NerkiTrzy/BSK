package police.policeman;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    public Button addPolicemanButton;

    public TextField idFilter;
    public TextField nameFilter;
    public TextField dateFilter;
    public TextField limitFilter;
    public Button refreshButton;

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

        idFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    idFilter.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        limitFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    limitFilter.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
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
            ResultSet rs = statement.executeQuery( getPolicemanQuery() );

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

    private String getPolicemanQuery() {
        String query = "";
        query += "SELECT * FROM policeman WHERE true ";

        if (idFilter.getText().length() > 0) { query += " AND id = " + idFilter.getText() + " "; }
        if (nameFilter.getText().length() > 0) {  query += " AND name ILIKE '%" + nameFilter.getText() + "%' "; }
        if (dateFilter.getText().length() > 0) { query += " AND birth = '" + dateFilter.getText() + "'::date "; }
        query += "ORDER BY id ";
        if (limitFilter.getText().length() > 0) { query += " LIMIT " + limitFilter.getText() + "; "; }

        return query;
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

            rs = statement.executeQuery("SELECT tl.label_value as value\n" +
                        "FROM tables_labels tl\n" +
                        "WHERE tl.table_name = 'policeman'");
            rs.next();
            tableLabel = rs.getInt("value");

            if (userLabel > tableLabel) {
                editPolicemanButton.setDisable(true);
                deletePolicemanButton.setDisable(true);
                addPolicemanButton.setDisable(true);
                refreshButton.setDisable(false);
            }
            else if(userLabel == tableLabel){
                editPolicemanButton.setDisable(false);
                deletePolicemanButton.setDisable(false);
                addPolicemanButton.setDisable(false);
                refreshButton.setDisable(false);
            }
            else{
                editPolicemanButton.setDisable(true);
                deletePolicemanButton.setDisable(true);
                addPolicemanButton.setDisable(false);
                refreshButton.setDisable(true);
            }

        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    public void refresh(ActionEvent actionEvent) {
        loadDataToGrid();
    }
}
