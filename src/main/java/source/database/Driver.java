package source.database;

import source.exception.DriverException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver {
    public Driver() {
        connectPSQLDriver();
    }

    public Connection getPSQLConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://78.24.220.161/booknetwork_db",
                    "admin", "admin");
    }

    private void connectPSQLDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DriverException("connect PSQL driver error");
        }
    }
}
