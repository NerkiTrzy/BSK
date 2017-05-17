package police.model;

import javafx.beans.property.*;

/**
 * Created by admin on 2017-03-11.
 */
public class User {
    private StringProperty userName;
    private IntegerProperty value;
    private StringProperty roleName;

    public User(String userName, int value, String roleName) {
        this.userName = new SimpleStringProperty(userName);
        this.value = new SimpleIntegerProperty(value);
        this.roleName = new SimpleStringProperty(roleName);
    }

    public User() {
        this.userName = new SimpleStringProperty("User");
        this.value = new SimpleIntegerProperty(0);
        this.roleName = new SimpleStringProperty("Rola");
    }
    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
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

    public String getRoleName() {
        return roleName.get();
    }

    public StringProperty roleNameProperty() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName.set(roleName);
    }


}
