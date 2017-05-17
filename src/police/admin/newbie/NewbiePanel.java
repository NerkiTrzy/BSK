package police.admin.newbie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Przemysław on 2017-04-18.
 */
public class NewbiePanel extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("newbieWindow.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Tworzenie nowego konta");
        stage.setScene(new Scene(root,400,300));
        stage.show();
    }
}
