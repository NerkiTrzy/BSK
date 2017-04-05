package Police.Policeman;

import Police.Datebase.DatebaseManager;
import Police.Main;
import Police.model.User;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class PolicemanController implements Initializable{
    @FXML
    private Button backButton;
    @FXML
    TableView<PolicemanData> policemanTableView;

    private ObservableList<PolicemanData> policemanDataSets = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        policemanTableView.setEditable(true);

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
    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }
}
