package source.database;

import source.exception.DriverException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Driver {
    public Driver() {
        connectPSQLDriver();
        try {
            this.connection = getPSQLConnection();
        } catch (SQLException e) {
            throw new DriverException("get PSQLConnection error");
        }
    }

    Connection connection;

    public Statement getStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new DriverException("create statement error");
        }
    }

    private Connection getPSQLConnection() throws SQLException {
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
