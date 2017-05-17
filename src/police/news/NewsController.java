package police.news;

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
}
