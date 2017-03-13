package Police.Policeman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class PolicemanPanel extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("policemanWindow.fxml"));
        primaryStage.setTitle("Panel Policjanta");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
}
