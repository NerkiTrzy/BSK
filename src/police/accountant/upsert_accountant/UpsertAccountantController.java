package police.accountant.upsert_accountant;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import police.accountant.AccountantData;
import police.accountant.AccountantPanel;
import police.datebase.DatebaseManager;
import police.news.NewsData;
import police.news.NewsPanel;

import java.net.URL;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Admin on 2017-05-23.
 */
public class UpsertAccountantController implements Initializable {

    private AccountantData accountantData;

    @FXML
    private Button backButton;
    @FXML
    private JFXTextField accountantIdText;
    @FXML
    private JFXTextField accountingDocumentText;
    @FXML
    private JFXTextField fiscalDateText;


    public void backToAccountant(ActionEvent actionEvent) throws Exception {
        AccountantPanel accountantPanel = new AccountantPanel();
        accountantPanel.start((Stage) backButton.getScene().getWindow());
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
    }

    public void save(ActionEvent actionEvent) {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            statement.execute("INSERT INTO accountant as a (id, accounting_document, fiscal_date) \n" +
                    " VALUES (" + accountantIdText.getText() + ", '" + accountingDocumentText.getText() + "', '" + fiscalDateText.getText() + "'::date) \n" +
                    "    ON CONFLICT (id) DO UPDATE\n" +
                    "    SET accounting_document = '" + accountingDocumentText.getText() + "', fiscal_date = '" + fiscalDateText.getText() + "'::date \n" +
                    "    WHERE a.id = " + accountantIdText.getText() + ";");
            backToAccountant(actionEvent);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}