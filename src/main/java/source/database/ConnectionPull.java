package source.database;

import java.util.HashMap;

public class ConnectionPull extends HashMap<Integer, Connection> {
    public ConnectionPull() {
        Driver driver = new Driver();
        for (int i = 0; i < PULL_LENGTH; i++) {
            this.put(i, new Connection(driver));
        }
    }

    private static final int PULL_LENGTH = 10;

    public Connection getConnection() {
        for (int i = 0; i < PULL_LENGTH; i++) {
            if (this.get(i).isFree()) {
                Connection connection = this.get(i);
                connection.setFree(false);
                return connection;
            }
        }
        return null;
    }

}

