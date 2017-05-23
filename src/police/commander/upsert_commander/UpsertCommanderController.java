package police.commander.upsert_commander;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import police.Main;
import police.commander.CommanderData;
import police.commander.CommanderPanel;
import police.datebase.DatebaseManager;
import police.news.NewsData;
import police.news.NewsPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertCommanderController implements Initializable {

    public Label newsIdLabel;
    private CommanderData commanderData;

    @FXML
    private Button backButton;
    @FXML
    private JFXTextField workerIdText;
    @FXML
    private JFXTextField workerText;
    @FXML
    private JFXTextField workerDateText;
    private int value;


    public void backToCommander(ActionEvent actionEvent) throws Exception {
        if (this.value == 0){
            backToMain();
        }
        else {
            CommanderPanel commanderPanel = new CommanderPanel();
            commanderPanel.start((Stage) backButton.getScene().getWindow());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workerIdText.setDisable(true);
    }

    public void setCommanderData(CommanderData commanderData) {
        this.commanderData = commanderData;
        writeToTexts();
    }

    private void writeToTexts() {
        workerIdText.setText(String.valueOf(this.commanderData.getId()));
        workerText.setText(this.commanderData.getWorker());
        workerDateText.setText(this.commanderData.getDate());
        value = 1;
        if (this.commanderData.getWorker().equals("")){
            value = 0;
            workerIdText.setVisible(false);
            newsIdLabel.setVisible(false);
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT COUNT(id) as id FROM commander WHERE id = " + workerIdText.getText() + ";");
            rs.next();
            this.value = rs.getInt("id");
            statement.execute("INSERT INTO commander as a (id, worker, employment_date) \n" +
                    " VALUES (" + workerIdText.getText() + ", '" + workerText.getText() + "', '" + workerDateText.getText() + "'::date) \n" +
                    "    ON CONFLICT (id) DO UPDATE\n" +
                    "    SET worker = '" + workerText.getText() + "', employment_date = '" + workerDateText.getText() + "'::date \n" +
                    "    WHERE a.id = " + workerIdText.getText() + ";");
            if (value == 0) {
                backToMain();
            }
            else{
                backToCommander(actionEvent);
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
