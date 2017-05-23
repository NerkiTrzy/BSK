package police.admin.newbie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import police.admin.AdminController;

/**
 * Created by Przemysław on 2017-04-18.
 */
public class NewbiePanel extends Application {

    AdminController adminController;

    public NewbiePanel(AdminController adminController){
        this.adminController = adminController;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("newbieWindow.fxml"));
        primaryStage.setTitle("Tworzenie nowego konta");
        primaryStage.setScene(new Scene(root,400,300));
        primaryStage.show();
    }
}
