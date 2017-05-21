package police.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by admin on 2017-03-14.
 */
public class ChangePasswordPanel extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("changePassword.fxml"));
        primaryStage.setTitle("Zmiana hasła");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
}