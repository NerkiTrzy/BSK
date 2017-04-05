package Police.Policeman;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Przemysław on 2017-04-05.
 */
public class PolicemanData {
    public PolicemanData(int id, String name, String date){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.date =  new SimpleStringProperty(date);
    }

    public PolicemanData(){
        this.id = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty("Imie");
        this.date =  new SimpleStringProperty("00-00-0000");
    }

    public int getId() {return this.id.get();}
    public IntegerProperty getIdProperty() {return this.id;}
    public void setId(int id){this.id.set(id);}

    public String getName() {return this.name.get();}
    public StringProperty getNameProperty() {return this.name;}
    public void setName(String name){this.name.set(name);}

    public String getDate() {return this.date.get();}
    public StringProperty getDateProperty() {return this.date;}
    public void setDate(String date){this.date.set(date);}

    private IntegerProperty id;
    private StringProperty name;
    private StringProperty date;

}
