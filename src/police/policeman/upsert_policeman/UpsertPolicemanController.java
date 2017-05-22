package police.policeman.upsert_policeman;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import police.datebase.DatebaseManager;
import police.news.NewsData;
import police.news.NewsPanel;
import police.policeman.PolicemanData;
import police.policeman.PolicemanPanel;

import java.net.URL;
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

    private PolicemanData policemanData;

    @FXML
    private Button backButton;



    public void backToPoliceman(ActionEvent actionEvent) throws Exception {
        PolicemanPanel policemanPanel = new PolicemanPanel();
        policemanPanel.start((Stage) backButton.getScene().getWindow());
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
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute( "INSERT INTO policeman as p (id, name, birth) \n" +
                                        " VALUES (" + policemanIdText.getText() + ", '" + policemanNameText.getText() + "', '" + policemanDateText.getText() + "'::date) \n" +
                                        "    ON CONFLICT (id) DO UPDATE\n" +
                                        "    SET name = '" + policemanNameText.getText() + "', birth = '" + policemanDateText.getText() + "'::date \n" +
                                        "    WHERE p.id = " + policemanIdText.getText() + ";" );
            backToPoliceman(actionEvent);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }


}
