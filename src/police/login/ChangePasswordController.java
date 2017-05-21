package police.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import police.Main;
import police.datebase.DatebaseManager;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by admin on 2017-03-14.
 */
public class ChangePasswordController implements Initializable {

    @FXML
    JFXPasswordField newPassword;
    @FXML
    JFXPasswordField oldPassword;
    @FXML
    JFXPasswordField repeatPassword;

    @FXML
    JFXButton changePasswordButton;

    @FXML
    JFXButton cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void backToMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) cancelButton.getScene().getWindow());
    }

    public void changePassword(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        if(checkPasswords()) {
            changePasswordOnDatabase();
        }
    }

    private void changePasswordOnDatabase() {
        try {

            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute( "ALTER ROLE session_user WITH password '" + newPassword.getText() + "';" );
            backToMenu(null);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private boolean checkPasswords()  {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            String pass = oldPassword.getText() + DatebaseManager.getCurrentUserLogin();
            md.update(pass.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = "md5" + bigInt.toString(16);
            if (!hashtext.equals(DatebaseManager.getCurrentUserPasswordHash())) {
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (!newPassword.getText().equals(repeatPassword.getText())){
            return false;
        }

        return true;
    }


}
