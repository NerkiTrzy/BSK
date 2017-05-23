package police.commander.upsert_commander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import police.commander.CommanderData;
import police.dispatcher.DispatcherData;
import police.dispatcher.upsert_dispatcher.UpsertDispatcherController;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertCommanderPanel extends Application {
    private FXMLLoader root = null;
    private CommanderData commanderData = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new FXMLLoader(getClass().getResource("upsertCommanderWindow.fxml"));

        primaryStage.setTitle("Edycja pracowników.");

        primaryStage.setScene(new Scene(root.load(), 1024, 768));
        ((UpsertCommanderController)root.getController()).setCommanderData(this.commanderData);
        primaryStage.show();
    }

    public void setCommanderData(CommanderData commanderData){
        this.commanderData = commanderData;
    }
}