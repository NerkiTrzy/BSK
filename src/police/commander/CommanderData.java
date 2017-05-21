package police.commander;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Przemysław on 2017-05-21.
 */
public class CommanderData {
    public CommanderData(int id, String worker, String date){
        this.id = new SimpleIntegerProperty(id);
        this.worker = new SimpleStringProperty(worker);
        this.date =  new SimpleStringProperty(date);
    }

    public CommanderData(){
        this.id = new SimpleIntegerProperty(0);
        this.worker = new SimpleStringProperty("Pracownik");
        this.date =  new SimpleStringProperty("00-00-0000");
    }

    public int getId() {return this.id.get();}
    public IntegerProperty getIdProperty() {return this.id;}
    public void setId(int id){this.id.set(id);}

    public String getWorker() {return this.worker.get();}
    public StringProperty getWorkerProperty() {return this.worker;}
    public void setWorker(String worker){this.worker.set(worker);}

    public String getDate() {return this.date.get();}
    public StringProperty getDateProperty() {return this.date;}
    public void setDate(String date){this.date.set(date);}


    private IntegerProperty id;
    private StringProperty worker;
    private StringProperty date;
}
