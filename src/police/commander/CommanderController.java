package police.commander;

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
}
