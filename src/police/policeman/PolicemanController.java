package police.policeman;

import javafx.scene.control.TableColumn;
import police.datebase.DatebaseManager;
import police.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import police.model.User;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class PolicemanController implements Initializable {
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

        policemanTableView.setEditable(true);

        loadDataToGrid();
    }

    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }

    private void loadDataToGrid() {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM policeman;" );

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
}
