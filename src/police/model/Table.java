package police.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Przemysław on 2017-05-30.
 */
public class Table {
    private StringProperty tableName;
    private IntegerProperty value;

    public Table(String tableName, int value) {
        this.tableName = new SimpleStringProperty(tableName);
        this.value = new SimpleIntegerProperty(value);
    }

    public Table() {
        this.tableName = new SimpleStringProperty("Table");
        this.value = new SimpleIntegerProperty(0);
    }
    public String getTableName() {
        return tableName.get();
    }

    public StringProperty userTableProperty() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName.set(tableName);
    }

    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }
}
