package police.news.upsert_news;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    private NewsData newsData;

    @FXML
    private Button backButton;
    @FXML
    private JFXTextField newsIdText;
    @FXML
    private JFXTextField newsAnnouncementText;
    @FXML
    private JFXTextField newsDateText;


    public void backToNews(ActionEvent actionEvent) throws Exception {
        NewsPanel newsPanel = new NewsPanel();
        newsPanel.start((Stage) backButton.getScene().getWindow());
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
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute( "INSERT INTO announcement as a (id, announcement, announce_date) \n" +
                                        " VALUES (" + newsIdText.getText() + ", '" + newsAnnouncementText.getText() + "', '" + newsDateText.getText() + "'::date) \n" +
                                        "    ON CONFLICT (id) DO UPDATE\n" +
                                        "    SET announcement = '" + newsAnnouncementText.getText() + "', announce_date = '" + newsDateText.getText() + "'::date \n" +
                                        "    WHERE a.id = " + newsIdText.getText() + ";" );
            backToNews(actionEvent);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }
}
