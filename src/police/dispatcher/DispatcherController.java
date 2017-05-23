package police.dispatcher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

        dispatcherTableView.setEditable(true);

        loadDataToGrid();
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
            ResultSet rs = statement.executeQuery( "SELECT * FROM dispatcher ORDER BY id;" );

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

    public void addNewDispatcher(ActionEvent actionEvent) throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT max(id) + 1 as id FROM dispatcher ORDER BY id;" );

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
}
