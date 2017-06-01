package police.news;

import com.jfoenix.controls.JFXDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import police.Controller;
import police.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import police.datebase.DatebaseManager;
import police.news.upsert_news.UpsertNewsPanel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Przemysław on 2017-03-13.
 */
public class NewsController implements Initializable {
    public Button editNewsButton;
    public Button deleteNewsButton;

    public TextField idFilter;
    public TextField nameFilter;
    public TextField dateFilter;
    public TextField limitFilter;
    public Button refreshButton;
    public Button addNewsButton;

    @FXML
    private Button backButton;
    @FXML
    TableView<NewsData> newsTableView;

    @FXML
    TableColumn<NewsData, Integer> newsIdColumn;
    @FXML
    TableColumn<NewsData, String> newsAnnouncementColumn;
    @FXML
    TableColumn<NewsData, String> newsDateColumn;

    @FXML
    AnchorPane anchorPane;

    private ObservableList<NewsData> newsDataSets = FXCollections.observableArrayList();

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

    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());
    }

    private void loadDataToGrid() {
        newsTableView.getItems().clear();
        newsDataSets.clear();
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( getNewsQuery() );

            List<NewsData> newsDataList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String announcement = rs.getString("announcement");
                String date = rs.getDate("announce_date").toString();

                newsDataList.add(new NewsData(id, announcement, date));

            }
            newsDataSets.addAll(newsDataList);
            newsTableView.setItems(newsDataSets);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private String getNewsQuery() {
        String query = "";
        query += "SELECT * FROM announcement WHERE true ";

        if (idFilter.getText().length() > 0) { query += " AND id = " + idFilter.getText() + " "; }
        if (nameFilter.getText().length() > 0) {  query += " AND announcement ILIKE '%" + nameFilter.getText() + "%' "; }
        if (dateFilter.getText().length() > 0) { query += " AND announce_date = '" + dateFilter.getText() + "'::date "; }
        query += "ORDER BY id ";
        if (limitFilter.getText().length() > 0) { query += " LIMIT " + limitFilter.getText() + "; "; }

        return query;
    }

    public void addNewNews(ActionEvent actionEvent) throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT last_value + 1 as id FROM announcement_id_seq;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        UpsertNewsPanel upsertNewsPanel = new UpsertNewsPanel();
        upsertNewsPanel.setNewsData(new NewsData(id,"","9999-12-31"));
        upsertNewsPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void editNews(ActionEvent actionEvent) throws Exception {
        UpsertNewsPanel upsertNewsPanel = new UpsertNewsPanel();
        upsertNewsPanel.setNewsData(newsDataSets.get(newsTableView.getSelectionModel().getFocusedIndex()));
        upsertNewsPanel.start((Stage) backButton.getScene().getWindow());
    }

    public void deleteNews(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuniecie wpisu");
        alert.setHeaderText("Potwierdz usuniecie");
        alert.setContentText("Czy na pewno chcesz usunac?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                int id = newsDataSets.get(newsTableView.getSelectionModel().getFocusedIndex()).getId();
                Statement statement = DatebaseManager.getConnection().createStatement();
                statement.execute( "DELETE FROM announcement WHERE id = " + id + ";" );
                loadDataToGrid();
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            }
        }
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
                    "WHERE tl.table_name = 'announcement'");
            rs.next();
            tableLabel = rs.getInt("value");

            if (userLabel > tableLabel) {
                editNewsButton.setDisable(true);
                deleteNewsButton.setDisable(true);
                addNewsButton.setDisable(true);
                refreshButton.setDisable(false);
            }
            else if(userLabel == tableLabel){
                editNewsButton.setDisable(false);
                deleteNewsButton.setDisable(false);
                addNewsButton.setDisable(false);
                refreshButton.setDisable(false);
            }
            else{
                editNewsButton.setDisable(true);
                deleteNewsButton.setDisable(true);
                addNewsButton.setDisable(false);
                refreshButton.setDisable(true);
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
