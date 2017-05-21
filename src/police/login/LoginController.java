package police.login;

import javafx.scene.input.KeyCode;
import police.datebase.DatebaseManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;
import police.Main;
/**
 * Created by admin on 2017-03-14.
 */
public class LoginController implements Initializable {

    @FXML
    JFXTextField login;

    @FXML
    JFXPasswordField password;

    @FXML
    JFXButton loginButton;

    @FXML
    javafx.scene.control.Label loginMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       /* login.getScene().addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.ENTER) {
                System.out.println("You pressed enter");
            }
        });*/
    }

    public void checkAndLogin() {
        DatebaseManager db = new DatebaseManager();
        if (db.connectToServer(login.getText(),password.getText()) ){
            Main main = new Main();
            try {
                main.start((Stage) login.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            this.loginMessage.setText("Błąd logowania");
        }
    }

}
