package police.accountant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import police.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import police.datebase.DatebaseManager;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class AccountantController implements Initializable{
    @FXML
    private Button backButton;

    @FXML
    TableView<AccountantData> accountantTableView;

    @FXML
    TableColumn<AccountantData, Integer> accountantIdColumn;
    @FXML
    TableColumn<AccountantData, String> accountantAccountingDocumentColumn;
    @FXML
    TableColumn<AccountantData, String> accountantDateColumn;

    private ObservableList<AccountantData> accountantDataSets = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadDataToGrid();
    }

    private void loadDataToGrid() {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM accountant;" );

            List<AccountantData> accountantDataList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String accounting_document = rs.getString("accounting_document");
                String date = rs.getDate("fiscal_date").toString();

                accountantDataList.add(new AccountantData(id, accounting_document, date));

            }
            accountantDataSets.addAll(accountantDataList);
            accountantTableView.setItems(accountantDataSets);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }


    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }
}
