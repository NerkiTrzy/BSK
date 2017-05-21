package police.news.upsert_news;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import police.news.NewsData;

/**
 * Created by Przemysław on 2017-05-21.
 */
public class UpsertNewsPanel extends Application {
    private FXMLLoader root = null;
    private NewsData newsData = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new FXMLLoader(getClass().getResource("upsertNewsWindow.fxml"));

        primaryStage.setTitle("Edycja zdarzenia.");

        primaryStage.setScene(new Scene(root.load(), 1024, 768));
        ((UpsertNewsController)root.getController()).setNewsData(this.newsData);
        primaryStage.show();
    }

    public void setNewsData(NewsData newsData){
        this.newsData = newsData;
    }
}
