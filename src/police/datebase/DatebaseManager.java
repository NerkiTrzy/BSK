package police.datebase;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.ini4j.InvalidFileFormatException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * Created by Przemysław on 2017-04-01.
 */
public class DatebaseManager {

    static private Connection connection;
    private Statement statement;

    public DatebaseManager(){
        connection = null;
        this.statement = null;
    }

    public boolean connectToServer(String login, String password){
        String ip;
        String port;
        try {
            Ini ini = new Ini(new File( System.getProperty("user.dir") + "/db.ini"));
            ip = ini.get("connection", "adres");
            port = ini.get("connection", "port");
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
            ip = "localhost";
            port = "5432";
        } catch (IOException e) {
            e.printStackTrace();
            ip = "localhost";
            port = "5432";
        }

        ip = "localhost";
        port = "5432";
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager
                    .getConnection("jdbc:postgresql://" + ip + ":" + port + "/policjaBSK",
                            login, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return false;
        }
        System.out.println("Opened database successfully");
        return  true;
    }

    static public Connection getConnection(){
        return connection;
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;

        try {
            this.statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return resultSet;
    }

    public static String getCurrentUserLogin() {
        String login = "";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT current_user AS login;");
            while (resultSet.next()){
                login = resultSet.getString("login");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return login;
    }

    public static String getCurrentUserPasswordHash() {
        String passwordHash = "";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT rolpassword\n" +
                    "FROM pg_authid \n" +
                    "WHERE rolname = current_user;");
            while (resultSet.next()){
                passwordHash = resultSet.getString("rolpassword");
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return passwordHash;
    }
}
