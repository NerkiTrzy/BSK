package police.accountant.upsert_accountant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import police.accountant.AccountantData;
import police.dispatcher.DispatcherData;
import police.dispatcher.upsert_dispatcher.UpsertDispatcherController;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertAccountantPanel extends Application {
    private FXMLLoader root = null;
    private AccountantData accountantData = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new FXMLLoader(getClass().getResource("upsertAccountantWindow.fxml"));

        primaryStage.setTitle("Edycja dokumentu.");

        primaryStage.setScene(new Scene(root.load(), 1024, 768));
        ((UpsertAccountantController)root.getController()).setAccountantData(this.accountantData);
        primaryStage.show();
    }

    public void setAccountantData(AccountantData accountantData){
        this.accountantData = accountantData;
    }
}