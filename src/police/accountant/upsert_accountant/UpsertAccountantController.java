package police.accountant.upsert_accountant;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import police.Main;
import police.accountant.AccountantData;
import police.accountant.AccountantPanel;
import police.datebase.DatebaseManager;
import police.news.NewsData;
import police.news.NewsPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertAccountantController implements Initializable {

    public Label accountantIdLabel;
    private AccountantData accountantData;

    @FXML
    private Button backButton;
    @FXML
    private JFXTextField accountantIdText;
    @FXML
    private JFXTextField accountingDocumentText;
    @FXML
    private JFXTextField fiscalDateText;

    private int value;

    public void backToAccountant(ActionEvent actionEvent) throws Exception {
        if (this.value == 0){
            backToMain();
        }
        else {
            AccountantPanel accountantPanel = new AccountantPanel();
            accountantPanel.start((Stage) backButton.getScene().getWindow());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountantIdText.setDisable(true);
    }

    public void setAccountantData(AccountantData accountantData) {
        this.accountantData = accountantData;
        writeToTexts();
    }

    private void writeToTexts() {
        accountantIdText.setText(String.valueOf(this.accountantData.getId()));
        accountingDocumentText.setText(this.accountantData.getAccountingDocument());
        fiscalDateText.setText(this.accountantData.getDate());
        value = 1;
        if (this.accountantData.getAccountingDocument().equals("")){
            value = 0;
            accountantIdText.setVisible(false);
            accountantIdLabel.setVisible(false);
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT " + accountantIdText.getText() + " BETWEEN start_value AND last_value as old FROM accountant_id_seq;");
            rs.next();
            if (!rs.getBoolean("old"))  {this.value = 0; }

            if (value == 0) {
                statement.execute("INSERT INTO accountant ( accounting_document, fiscal_date) \n" +
                        " VALUES ( '" + accountingDocumentText.getText() + "', '" + fiscalDateText.getText() + "'::date);");
                backToMain();

            }
            else{
                statement.execute("UPDATE accountant\n" +
                        "    SET accounting_document = '" + accountingDocumentText.getText() + "', fiscal_date = '" + fiscalDateText.getText() + "'::date \n" +
                        "    WHERE id = " + accountantIdText.getText() + ";");
                backToAccountant(actionEvent);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    private void backToMain() throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());
    }
}