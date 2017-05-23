package police.commander.upsert_commander;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import police.commander.CommanderData;
import police.commander.CommanderPanel;
import police.datebase.DatebaseManager;
import police.news.NewsData;
import police.news.NewsPanel;

import java.net.URL;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertCommanderController implements Initializable {

    private CommanderData commanderData;

    @FXML
    private Button backButton;
    @FXML
    private JFXTextField workerIdText;
    @FXML
    private JFXTextField workerText;
    @FXML
    private JFXTextField workerDateText;


    public void backToCommander(ActionEvent actionEvent) throws Exception {
        CommanderPanel commanderPanel = new CommanderPanel();
        commanderPanel.start((Stage) backButton.getScene().getWindow());
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
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute("INSERT INTO commander as a (id, worker, employment_date) \n" +
                    " VALUES (" + workerIdText.getText() + ", '" + workerText.getText() + "', '" + workerDateText.getText() + "'::date) \n" +
                    "    ON CONFLICT (id) DO UPDATE\n" +
                    "    SET worker = '" + workerText.getText() + "', employment_date = '" + workerDateText.getText() + "'::date \n" +
                    "    WHERE a.id = " + workerIdText.getText() + ";");
            backToCommander(actionEvent);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
