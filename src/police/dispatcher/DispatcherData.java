package police.dispatcher;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Przemysław on 2017-04-18.
 */
public class DispatcherData {
    public DispatcherData(int id, String place, String date, String patrol){
        this.id = new SimpleIntegerProperty(id);
        this.place = new SimpleStringProperty(place);
        this.date =  new SimpleStringProperty(date);
        this.patrol = new SimpleStringProperty(patrol);
    }

    public DispatcherData(){
        this.id = new SimpleIntegerProperty(0);
        this.place = new SimpleStringProperty("Miejsce nieznane");
        this.date =  new SimpleStringProperty("00-00-0000");
        this.patrol = new SimpleStringProperty("Patrol numer 1");
    }

    public int getId() {return this.id.get();}
    public IntegerProperty getIdProperty() {return this.id;}
    public void setId(int id){this.id.set(id);}

    public String getPlace() {return this.place.get();}
    public StringProperty getPlaceProperty() {return this.place;}
    public void setPlace(String place){this.place.set(place);}

    public String getDate() {return this.date.get();}
    public StringProperty getDateProperty() {return this.date;}
    public void setDate(String date){this.date.set(date);}

    public String getPatrol() {return this.patrol.get();}
    public StringProperty getPatrolProperty() {return this.patrol;}
    public void setPatrol(String patrol){this.patrol.set(patrol);}

    private IntegerProperty id;
    private StringProperty place;
    private StringProperty date;
    private StringProperty patrol;
}
