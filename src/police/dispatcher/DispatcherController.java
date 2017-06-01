package police.dispatcher;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import police.Controller;
import police.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import police.datebase.DatebaseManager;
import police.dispatcher.upsert_dispatcher.UpsertDispatcherPanel;
import police.news.NewsData;
import police.news.upsert_news.UpsertNewsPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class DispatcherController implements Initializable{
    public Button editDispatcherButton;
    public Button deleteDispatcherButton;

    public TextField idFilter;
    public TextField nameFilter;
    public TextField dateFilter;
    public TextField placeFilter;
    public TextField limitFilter;
    public Button refreshButton;
    public Button addDispatcherButton;

    @FXML
    private Button backButton;

    @FXML
    TableView<DispatcherData> dispatcherTableView;

    @FXML
    TableColumn<DispatcherData, Integer> dispatcherIdColumn;
    @FXML
    TableColumn<DispatcherData, String> dispatcherPlaceColumn;
    @FXML
    TableColumn<DispatcherData, String> dispatcherDateColumn;
    @FXML
    TableColumn<DispatcherData, String> dispatcherPatrolColumn;

    private ObservableList<DispatcherData> dispatcherDataSets = FXCollections.observableArrayList();

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
        dispatcherTableView.getItems().clear();
        dispatcherDataSets.clear();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( getDispatcherQuery() );

            List<DispatcherData> dispatcherDataList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String place = rs.getString("place");
                String date = rs.getDate("intervention_date").toString();
                String patrol = rs.getString("patrol");

                dispatcherDataList.add(new DispatcherData(id, place, date, patrol));

            }
            dispatcherDataSets.addAll(dispatcherDataList);
            dispatcherTableView.setItems(dispatcherDataSets);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private String getDispatcherQuery() {
        String query = "";
        query += "SELECT * FROM dispatcher WHERE true ";

        if (idFilter.getText().length() > 0) { query += " AND id = " + idFilter.getText() + " "; }
        if (placeFilter.getText().length() > 0) {  query += " AND place ILIKE '%" + placeFilter.getText() + "%' "; }
        if (nameFilter.getText().length() > 0) {  query += " AND patrol ILIKE '%" + nameFilter.getText() + "%' "; }
        if (dateFilter.getText().length() > 0) { query += " AND intervention_date = '" + dateFilter.getText() + "'::date "; }
        query += "ORDER BY id ";
        if (limitFilter.getText().length() > 0) { query += " LIMIT " + limitFilter.getText() + "; "; }

        return query;
    }

    public void addNewDispatcher(ActionEvent actionEvent) throws Exception {
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
        upsertDispatcherPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void editDispatcher(ActionEvent actionEvent) throws Exception {
        UpsertDispatcherPanel upsertDispatcherPanel = new UpsertDispatcherPanel();
        upsertDispatcherPanel.setDispatcherData(dispatcherDataSets.get(dispatcherTableView.getSelectionModel().getFocusedIndex()));
        upsertDispatcherPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void deleteDispatcher(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuniecie wpisu");
        alert.setHeaderText("Potwierdz usuniecie");
        alert.setContentText("Czy na pewno chcesz usunac?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                int id = dispatcherDataSets.get(dispatcherTableView.getSelectionModel().getFocusedIndex()).getId();
                Statement statement = DatebaseManager.getConnection().createStatement();
                statement.execute( "DELETE FROM dispatcher WHERE id = " + id + ";" );
                loadDataToGrid();
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            }
        }
    }

    public String getFormattedDate(){
        Date date = new Date();
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return stringDate;
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
                    "WHERE tl.table_name = 'dispatcher'");
            rs.next();
            tableLabel = rs.getInt("value");

            if (userLabel > tableLabel) {
                editDispatcherButton.setDisable(true);
                deleteDispatcherButton.setDisable(true);
                addDispatcherButton.setDisable(true);
                refreshButton.setDisable(false);
            }
            else if(userLabel == tableLabel){
                editDispatcherButton.setDisable(false);
                deleteDispatcherButton.setDisable(false);
                addDispatcherButton.setDisable(false);
                refreshButton.setDisable(false);
            }
            else{
                editDispatcherButton.setDisable(true);
                deleteDispatcherButton.setDisable(true);
                addDispatcherButton.setDisable(false);
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
