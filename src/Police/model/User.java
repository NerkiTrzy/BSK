package police.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by admin on 2017-03-11.
 */
public class User {
    private StringProperty firstName;
    private StringProperty lastName;
    private BooleanProperty admin;

    public User(String firstName, String lastName, boolean admin) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.admin = new SimpleBooleanProperty(admin);
    }

    public User() {
        this.firstName = new SimpleStringProperty("da");
        this.lastName = new SimpleStringProperty("dfs");
        this.admin = new SimpleBooleanProperty(false);
    }
    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public boolean isAdmin() {
        return admin.get();
    }

    public BooleanProperty adminProperty() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin.set(admin);
    }


}
