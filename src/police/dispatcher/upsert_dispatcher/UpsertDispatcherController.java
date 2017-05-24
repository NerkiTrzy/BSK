package police.dispatcher.upsert_dispatcher;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import police.Main;
import police.datebase.DatebaseManager;
import police.dispatcher.DispatcherData;
import police.dispatcher.DispatcherPanel;
import police.news.NewsData;
import police.news.NewsPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertDispatcherController implements Initializable {

    public Label dispatcherIdLabel;
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
    private int value;


    public void backToDispatcher(ActionEvent actionEvent) throws Exception {
        if (this.value == 0){
            backToMain();
        }
        else {
            DispatcherPanel dispatcherPanel = new DispatcherPanel();
            dispatcherPanel.start((Stage) backButton.getScene().getWindow());
        }
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
        value = 1;
        if (this.dispatcherData.getPlace().equals("")){
            value = 0;
            dispatcherIdText.setVisible(false);
            dispatcherIdLabel.setVisible(false);
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT " + dispatcherIdText.getText() + " BETWEEN start_value AND last_value as old FROM dispatcher_id_seq;");
            rs.next();
            if (!rs.getBoolean("old"))  {this.value = 0; }

            if (value == 0) {
                statement.execute("INSERT INTO dispatcher ( place, intervention_date, patrol) \n" +
                        " VALUES ('" + dispatcherPlaceText.getText() + "', '" + dispatcherDateText.getText() + "'::date, '" + dispatcherPatrolNameText.getText() + "') \n");
                        backToMain();

            }
            else{
                statement.execute("UPDATE dispatcher\n" +
                        "    SET place = '" + dispatcherPlaceText.getText() + "', intervention_date = '" + dispatcherDateText.getText() + "'::date" + ", patrol = '" + dispatcherPatrolNameText.getText() + "'\n" +
                        "    WHERE id = " + dispatcherIdText.getText() + ";");
                backToDispatcher(actionEvent);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    private void backToMain() throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());
    }
}