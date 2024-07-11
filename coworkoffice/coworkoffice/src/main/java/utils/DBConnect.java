package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handle the connection to the MySQL database that stores our tasks.
 */
public class DBConnect {
    // the DB location (on file, inside the project)
    static private final String user = "root";
    static private final String password = "Tm22311MAp;08La";
    static private final String connString = "jdbc:mysql://localhost/coworkoffice";

    static private DBConnect instance = null;
    private DBConnect() {
        instance = this;
    }

    public static DBConnect getInstance() {
        if (instance == null)
            return new DBConnect();
        else {
            return instance;
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            // return the connection instance
            return DriverManager.getConnection(connString, user, password);
        } catch (SQLException e) {
            throw new SQLException("Cannot get connection to " + connString, e);
        }
    }

}