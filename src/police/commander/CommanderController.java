package police.commander;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

        loadDataToGrid();
    }

    private void loadDataToGrid() {
        commanderTableView.getItems().clear();
        commanderDataSets.clear();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM commander ORDER BY id;" );

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
}
