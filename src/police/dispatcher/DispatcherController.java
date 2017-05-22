package police.dispatcher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import police.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import police.datebase.DatebaseManager;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
}
