package police.policeman.upsert_policeman;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import police.Main;
import police.datebase.DatebaseManager;
import police.news.NewsData;
import police.news.NewsPanel;
import police.policeman.PolicemanData;
import police.policeman.PolicemanPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Przemysław on 2017-05-21.
 */
public class UpsertPolicemanController implements Initializable {

    @FXML
    public JFXTextField policemanIdText;
    @FXML
    public JFXTextField policemanNameText;
    @FXML
    public JFXTextField policemanDateText;
    public Label policemanIdLabel;

    private PolicemanData policemanData;

    @FXML
    private Button backButton;

    private int value;


    public void backToPoliceman(ActionEvent actionEvent) throws Exception {
        if (this.value == 0){
            backToMain();
        }
        else{
            PolicemanPanel policemanPanel = new PolicemanPanel();
            policemanPanel.start((Stage) backButton.getScene().getWindow());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        policemanIdText.setDisable(true);
    }

    public void setPolicemanData(PolicemanData policemanData){
        this.policemanData = policemanData;
        writeToTexts();
    }

    private void writeToTexts() {
        policemanIdText.setText(String.valueOf(this.policemanData.getId()));
        policemanNameText.setText(this.policemanData.getName());
        policemanDateText.setText(this.policemanData.getDate());
        value = 1;
        if (this.policemanData.getName().equals("")){
            value = 0;
            policemanIdText.setVisible(false);
            policemanIdLabel.setVisible(false);
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT " + policemanIdText.getText() + " BETWEEN start_value AND last_value as old FROM policeman_id_seq;");
            rs.next();
            if (!rs.getBoolean("old"))  {this.value = 0; }

            if (value == 0) {
                statement.execute("INSERT INTO policeman ( name, birth) \n" +
                        " VALUES ('" + policemanNameText.getText() + "', '" + policemanDateText.getText() + "'::date) \n");
                backToMain();

            }
            else{
                statement.execute("UPDATE policeman\n" +
                        "    SET name = '" + policemanNameText.getText() + "', birth = '" + policemanDateText.getText() + "'::date \n" +
                        "    WHERE id = " + policemanIdText.getText() + ";" );
                backToPoliceman(actionEvent);
            }

        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private void backToMain() throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());
    }

}
