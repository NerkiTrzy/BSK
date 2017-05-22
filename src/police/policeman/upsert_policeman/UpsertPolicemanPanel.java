package police.policeman.upsert_policeman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import police.policeman.PolicemanData;

/**
 * Created by Przemysław on 2017-05-21.
 */
public class UpsertPolicemanPanel extends Application {
    private FXMLLoader root = null;
    private PolicemanData policemanData = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new FXMLLoader(getClass().getResource("upsertPolicemanWindow.fxml"));

        primaryStage.setTitle("Edycja policjanta.");

        primaryStage.setScene(new Scene(root.load(), 1024, 768));
        ((UpsertPolicemanController)root.getController()).setPolicemanData(this.policemanData);
        primaryStage.show();
    }

    public void setPolicemanData(PolicemanData policemanData){
        this.policemanData = policemanData;
    }
}
