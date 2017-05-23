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

            ResultSet rs = statement.executeQuery( "SELECT COUNT(id) as id FROM policeman WHERE id = " + policemanIdText.getText() + ";");
            rs.next();
            this.value = rs.getInt("id");
            statement.execute( "INSERT INTO policeman as p (id, name, birth) \n" +
                                        " VALUES (" + policemanIdText.getText() + ", '" + policemanNameText.getText() + "', '" + policemanDateText.getText() + "'::date) \n" +
                                        "    ON CONFLICT (id) DO UPDATE\n" +
                                        "    SET name = '" + policemanNameText.getText() + "', birth = '" + policemanDateText.getText() + "'::date \n" +
                                        "    WHERE p.id = " + policemanIdText.getText() + ";" );
            if (value == 0) {
                backToMain();
            }
            else{
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
