package police.dispatcher.upsert_dispatcher;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import police.datebase.DatebaseManager;
import police.dispatcher.DispatcherData;
import police.dispatcher.DispatcherPanel;
import police.news.NewsData;
import police.news.NewsPanel;

import java.net.URL;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertDispatcherController implements Initializable {

    private DispatcherData dispatcherData;

    @FXML
    private Button backButton;
    @FXML
    private JFXTextField dispatcherIdText;
    @FXML
    private JFXTextField dispatcherPlaceText;
    @FXML
    private JFXTextField dispatcherDateText;
    @FXML
    private JFXTextField dispatcherPatrolNameText;


    public void backToDispatcher(ActionEvent actionEvent) throws Exception {
        DispatcherPanel dispatcherPanel = new DispatcherPanel();
        dispatcherPanel.start((Stage) backButton.getScene().getWindow());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dispatcherIdText.setDisable(true);
    }

    public void setDispatcherData(DispatcherData dispatcherData) {
        this.dispatcherData = dispatcherData;
        writeToTexts();
    }

    private void writeToTexts() {
        dispatcherIdText.setText(String.valueOf(this.dispatcherData.getId()));
        dispatcherPlaceText.setText(this.dispatcherData.getPlace());
        dispatcherDateText.setText(this.dispatcherData.getDate());
        dispatcherPatrolNameText.setText(this.dispatcherData.getPatrol());
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute("INSERT INTO dispatcher as a (id, place, intervention_date, patrol) \n" +
                    " VALUES (" + dispatcherIdText.getText() + ", '" + dispatcherPlaceText.getText() + "', '" + dispatcherDateText.getText() + "'::date, '" + dispatcherPatrolNameText.getText() + "') \n" +
                    "    ON CONFLICT (id) DO UPDATE\n" +
                    "    SET place = '" + dispatcherPlaceText.getText() + "', intervention_date = '" + dispatcherDateText.getText() + "'::date" + ", patrol = '" + dispatcherPatrolNameText.getText() + "'\n" +
                    "    WHERE a.id = " + dispatcherIdText.getText() + ";");
            backToDispatcher(actionEvent);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}