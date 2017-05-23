package police.news.upsert_news;

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

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Przemysław on 2017-05-21.
 */
public class UpsertNewsController implements Initializable {

    public Label newsIdLabel;
    private NewsData newsData;

    @FXML
    private Button backButton;
    @FXML
    private JFXTextField newsIdText;
    @FXML
    private JFXTextField newsAnnouncementText;
    @FXML
    private JFXTextField newsDateText;

    private int value;

    public void backToNews(ActionEvent actionEvent) throws Exception {
        if (this.value == 0){
            backToMain();
        }
        else {
            NewsPanel newsPanel = new NewsPanel();
            newsPanel.start((Stage) backButton.getScene().getWindow());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newsIdText.setDisable(true);
    }

    public void setNewsData(NewsData newsData){
        this.newsData = newsData;
        writeToTexts();
    }

    private void writeToTexts() {
        newsIdText.setText(String.valueOf(this.newsData.getId()));
        newsAnnouncementText.setText(this.newsData.getAnnouncement());
        newsDateText.setText(this.newsData.getDate());
        value = 1;
        if (this.newsData.getAnnouncement().equals("")){
            value = 0;
            newsIdText.setVisible(false);
            newsIdLabel.setVisible(false);
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();

            ResultSet rs = statement.executeQuery( "SELECT COUNT(id) as id FROM announcement WHERE id = " + newsIdText.getText() + ";");
            rs.next();
            this.value = rs.getInt("id");
            statement.execute( "INSERT INTO announcement as a (id, announcement, announce_date) \n" +
                                        " VALUES (" + newsIdText.getText() + ", '" + newsAnnouncementText.getText() + "', '" + newsDateText.getText() + "'::date) \n" +
                                        "    ON CONFLICT (id) DO UPDATE\n" +
                                        "    SET announcement = '" + newsAnnouncementText.getText() + "', announce_date = '" + newsDateText.getText() + "'::date \n" +
                                        "    WHERE a.id = " + newsIdText.getText() + ";" );
            if (value == 0) {
                backToMain();
            }
            else{
                backToNews(actionEvent);
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
