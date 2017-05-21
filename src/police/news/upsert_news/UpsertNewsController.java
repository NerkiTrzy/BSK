package police.news.upsert_news;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import police.Main;
import police.news.NewsData;
import police.news.NewsPanel;

import java.net.URL;
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

    }
}
