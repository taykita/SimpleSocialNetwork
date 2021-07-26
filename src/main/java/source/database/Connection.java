package source.database;

import source.exception.DriverException;

import java.sql.SQLException;

public class Connection {
    public Connection(Driver driver) {
        try {
            this.connection = driver.getPSQLConnection();
        } catch (SQLException e) {
            throw new DriverException("Connection pull init error\n" + e.getMessage());
        }
        this.isFree = true;
    }
    private final java.sql.Connection connection;
    private boolean isFree;

    public java.sql.Connection get() {
        return connection;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}
