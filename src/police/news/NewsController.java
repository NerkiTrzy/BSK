package police.news;

import com.jfoenix.controls.JFXDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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

        newsTableView.setEditable(true);

        loadDataToGrid();
    }

    public void backToMainMenu(ActionEvent actionEvent) throws Exception {
        Main main = new Main();
        main.start((Stage) backButton.getScene().getWindow());
    }

    private void loadDataToGrid() {
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM announcement;" );

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

    public void addNewNews(ActionEvent actionEvent) throws Exception {
        int id = 0;
        try {
            Statement statement = DatebaseManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery( "SELECT max(id) + 1 as id FROM announcement;" );

            while (rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        UpsertNewsPanel upsertNewsPanel = new UpsertNewsPanel();
        upsertNewsPanel.setNewsData(new NewsData(id,"","31-12-9999"));
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
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            }
        }
    }
}
