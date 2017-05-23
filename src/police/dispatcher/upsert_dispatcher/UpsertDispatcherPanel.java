package police.dispatcher.upsert_dispatcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import police.dispatcher.DispatcherData;
import police.news.NewsData;
import police.news.upsert_news.UpsertNewsController;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertDispatcherPanel extends Application {
    private FXMLLoader root = null;
    private DispatcherData dispatcherData = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new FXMLLoader(getClass().getResource("upsertDispatcherWindow.fxml"));

        primaryStage.setTitle("Edycja dyspozycji.");

        primaryStage.setScene(new Scene(root.load(), 1024, 768));
        ((UpsertDispatcherController)root.getController()).setDispatcherData(this.dispatcherData);
        primaryStage.show();
    }

    public void setDispatcherData(DispatcherData dispatcherData){
        this.dispatcherData = dispatcherData;
    }
}