package police.commander;

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
import police.commander.upsert_commander.UpsertCommanderPanel;
import police.datebase.DatebaseManager;
import police.news.NewsData;
import police.news.upsert_news.UpsertNewsPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class CommanderController implements Initializable{
    public Button editWorkerButton;
    public Button deleteWorkerButton;
    public TextField idFilter;
    public TextField nameFilter;
    public TextField dateFilter;
    public TextField limitFilter;
    public Button addCommanderButton;
    public Button refreshButton;
    @FXML
    private Button backButton;

    @FXML
    TableView<CommanderData> commanderTableView;

    @FXML
    TableColumn<CommanderData, Integer> commanderIdColumn;
    @FXML
    TableColumn<CommanderData, String> commanderWorkerColumn;
    @FXML
    TableColumn<CommanderData, String> commanderDateColumn;

    private ObservableList<CommanderData> commanderDataSets = FXCollections.observableArrayList();

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

    private void loadDataToGrid() {
        commanderTableView.getItems().clear();
        commanderDataSets.clear();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(getCommanderQuery());

            List<CommanderData> commanderDataList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String worker = rs.getString("worker");
                String date = rs.getDate("employment_date").toString();

                commanderDataList.add(new CommanderData(id, worker, date));

            }
            commanderDataSets.addAll(commanderDataList);
            commanderTableView.setItems(commanderDataSets);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private String getCommanderQuery() {
        String query = "";
        query += "SELECT * FROM commander WHERE true ";

        if (idFilter.getText().length() > 0) { query += " AND id = " + idFilter.getText() + " "; }
        if (nameFilter.getText().length() > 0) {  query += " AND worker ILIKE '%" + nameFilter.getText() + "%' "; }
        if (dateFilter.getText().length() > 0) { query += " AND employment_date = '" + dateFilter.getText() + "'::date "; }
        query += "ORDER BY id ";
        if (limitFilter.getText().length() > 0) { query += " LIMIT " + limitFilter.getText() + "; "; }

        return query;
    }

    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }
    public void addNewWorker(ActionEvent actionEvent) throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT max(id) + 1 as id FROM commander ORDER BY id;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        String date = getFormattedDate();
        UpsertCommanderPanel upsertCommanderPanel = new UpsertCommanderPanel();
        upsertCommanderPanel.setCommanderData(new CommanderData(id,"",date));
        upsertCommanderPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void editWorker(ActionEvent actionEvent) throws Exception {
        UpsertCommanderPanel upsertCommanderPanel = new UpsertCommanderPanel();
        upsertCommanderPanel.setCommanderData(commanderDataSets.get(commanderTableView.getSelectionModel().getFocusedIndex()));
        upsertCommanderPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void deleteWorker(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuniecie wpisu");
        alert.setHeaderText("Potwierdz usuniecie");
        alert.setContentText("Czy na pewno chcesz usunac?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                int id = commanderDataSets.get(commanderTableView.getSelectionModel().getFocusedIndex()).getId();
                Statement statement = DatebaseManager.getConnection().createStatement();
                statement.execute( "DELETE FROM commander WHERE id = " + id + ";" );
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
                    "WHERE tl.table_name = 'commander'");
            rs.next();
            tableLabel = rs.getInt("value");

            if (userLabel > tableLabel) {
                editWorkerButton.setDisable(true);
                deleteWorkerButton.setDisable(true);
                addCommanderButton.setDisable(true);
                refreshButton.setDisable(false);
            }
            else if(userLabel == tableLabel){
                editWorkerButton.setDisable(false);
                deleteWorkerButton.setDisable(false);
                addCommanderButton.setDisable(false);
                refreshButton.setDisable(false);
            }
            else{
                editWorkerButton.setDisable(true);
                deleteWorkerButton.setDisable(true);
                addCommanderButton.setDisable(false);
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
