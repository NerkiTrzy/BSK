package police.accountant;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import police.Controller;
import police.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import police.accountant.upsert_accountant.UpsertAccountantController;
import police.accountant.upsert_accountant.UpsertAccountantPanel;
import police.datebase.DatebaseManager;
import police.dispatcher.DispatcherData;
import police.dispatcher.upsert_dispatcher.UpsertDispatcherPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class AccountantController implements Initializable{
    public Button editAccountantButton;
    public Button deleteAccountantButton;
    public TextField idFilter;
    public TextField nameFilter;
    public TextField dateFilter;
    public TextField limitFilter;
    public Button refreshButton;
    public Button addAccountantButton;
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

        checkLabelsForUser();

        idFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    idFilter.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        limitFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    limitFilter.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void loadDataToGrid() {
        accountantTableView.getItems().clear();
        accountantDataSets.clear();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( getAccountantQuery() );

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

    private String getAccountantQuery() {
        String query = "";
        query += "SELECT * FROM accountant WHERE true ";

        if (idFilter.getText().length() > 0) { query += " AND id = " + idFilter.getText() + " "; }
        if (nameFilter.getText().length() > 0) {  query += " AND accounting_document ILIKE '%" + nameFilter.getText() + "%' "; }
        if (dateFilter.getText().length() > 0) { query += " AND fiscal_date = '" + dateFilter.getText() + "'::date "; }
        query += "ORDER BY id ";
        if (limitFilter.getText().length() > 0) { query += " LIMIT " + limitFilter.getText() + "; "; }

        return query;
    }

    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());

    }

    public void addNewAccountant(ActionEvent actionEvent) throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT last_value + 1 as id FROM accountant_id_seq;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        String date = getFormatedDate();
        UpsertAccountantPanel upsertAccountantPanel = new UpsertAccountantPanel();
        upsertAccountantPanel.setAccountantData(new AccountantData(id,"", date));
        upsertAccountantPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void editAccountant(ActionEvent actionEvent) throws Exception {
        UpsertAccountantPanel upsertAccountantPanel = new UpsertAccountantPanel();
        upsertAccountantPanel.setAccountantData(accountantDataSets.get(accountantTableView.getSelectionModel().getFocusedIndex()));
        upsertAccountantPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void deleteAccountant(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuniecie wpisu");
        alert.setHeaderText("Potwierdz usuniecie");
        alert.setContentText("Czy na pewno chcesz usunac?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                int id = accountantDataSets.get(accountantTableView.getSelectionModel().getFocusedIndex()).getId();
                Statement statement = DatebaseManager.getConnection().createStatement();
                statement.execute( "DELETE FROM accountant WHERE id = " + id + ";" );
                loadDataToGrid();
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            }
        }
    }

    public String getFormatedDate(){
        Date date = new Date();
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return stringDate;
    }

    private void checkLabelsForUser() {
        int userLabel = 0;
        int tableLabel = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(Controller.userAccessesQuery);
            rs.next();
            userLabel = rs.getInt("value");

            rs = statement.executeQuery("SELECT tl.label_value as value\n" +
                    "FROM tables_labels tl\n" +
                    "WHERE tl.table_name = 'accountant'");
            rs.next();
            tableLabel = rs.getInt("value");

            if (userLabel > tableLabel) {
                editAccountantButton.setDisable(true);
                deleteAccountantButton.setDisable(true);
                refreshButton.setDisable(false);
                addAccountantButton.setDisable(true);
            }
            else if(userLabel == tableLabel){
                editAccountantButton.setDisable(false);
                deleteAccountantButton.setDisable(false);
                refreshButton.setDisable(false);
                addAccountantButton.setDisable(false);
            }
            else{
                editAccountantButton.setDisable(true);
                deleteAccountantButton.setDisable(true);
                refreshButton.setDisable(true);
                addAccountantButton.setDisable(false);
            }

        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    public void refresh(ActionEvent actionEvent) {
        loadDataToGrid();
    }
}
