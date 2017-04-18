package police.news;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Przemysław on 2017-04-18.
 */
public class NewsData {
    public NewsData(int id, String announcement, String date){
        this.id = new SimpleIntegerProperty(id);
        this.announcement = new SimpleStringProperty(announcement);
        this.date =  new SimpleStringProperty(date);
    }

    public NewsData(){
        this.id = new SimpleIntegerProperty(0);
        this.announcement = new SimpleStringProperty("Ogłoszenie");
        this.date =  new SimpleStringProperty("00-00-0000");
    }

    public int getId() {return this.id.get();}
    public IntegerProperty getIdProperty() {return this.id;}
    public void setId(int id){this.id.set(id);}

    public String getAnnouncement() {return this.announcement.get();}
    public StringProperty getAnnouncementProperty() {return this.announcement;}
    public void setAnnouncement(String announcement){this.announcement.set(announcement);}

    public String getDate() {return this.date.get();}
    public StringProperty getDateProperty() {return this.date;}
    public void setDate(String date){this.date.set(date);}

    private IntegerProperty id;
    private StringProperty announcement;
    private StringProperty date;
}
