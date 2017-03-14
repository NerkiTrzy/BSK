package Police.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import Police.Main;
/**
 * Created by Admin on 2017-03-14.
 */
public class LoginController implements Initializable {

    @FXML
    JFXTextField login;

    @FXML
    JFXPasswordField password;

    @FXML
    JFXButton loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void checkAndLogin() {
        if(login.getText().equals("login") && password.getText().equals("pass")) {
            Main main = new Main();
            try {
                main.start((Stage) login.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
