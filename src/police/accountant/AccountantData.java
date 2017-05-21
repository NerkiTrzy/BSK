package police.accountant;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Przemysław on 2017-05-21.
 */
public class AccountantData {
    public AccountantData(int id, String accountingDocument, String date){
        this.id = new SimpleIntegerProperty(id);
        this.accountingDocument = new SimpleStringProperty(accountingDocument);
        this.date =  new SimpleStringProperty(date);
    }

    public AccountantData(){
        this.id = new SimpleIntegerProperty(0);
        this.accountingDocument = new SimpleStringProperty("Dokument Księgowy");
        this.date =  new SimpleStringProperty("00-00-0000");
    }

    public int getId() {return this.id.get();}
    public IntegerProperty getIdProperty() {return this.id;}
    public void setId(int id){this.id.set(id);}

    public String getAccountingDocument() {return this.accountingDocument.get();}
    public StringProperty getAccountingDocumentProperty() {return this.accountingDocument;}
    public void setAccountingDocument(String accountingDocument){this.accountingDocument.set(accountingDocument);}

    public String getDate() {return this.date.get();}
    public StringProperty getDateProperty() {return this.date;}
    public void setDate(String date){this.date.set(date);}


    private IntegerProperty id;
    private StringProperty accountingDocument;
    private StringProperty date;
}
