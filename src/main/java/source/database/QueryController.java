package source.database;

import org.apache.commons.dbcp2.BasicDataSource;
import source.exception.AccStorageException;
import source.verification.entity.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class QueryController {
    public QueryController(BasicDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    private BasicDataSource connectionPool;

    public int queryAdd(String mainQuery, String childQuery) throws AccStorageException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                ResultSet rs = statement.executeQuery(mainQuery);
                rs.next();
                int id = rs.getInt("id");
                statement.execute(childQuery);

                return id;
            }
        } catch (SQLException e) {
            throw new AccStorageException("DataBase.add error. Query: " + mainQuery + "\nError:", e);
        }
    }

    public Account getQuery(String query) throws AccStorageException {
        Account account;
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                ResultSet rs = statement.executeQuery(query);
                rs.next();
                account = new Account(rs.getString("email"), rs.getString("pass"),
                        rs.getString("name"), rs.getInt("id"));
                return account;
            }
        } catch (SQLException e) {
            throw new AccStorageException("DataBase.get error. Query: " + query + "\nError:", e);
        }
    }

    public boolean existQuery(String query) throws AccStorageException {
        boolean exists;
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                ResultSet rs = statement.executeQuery(query);
                rs.next();
                exists = rs.getBoolean("exists");
                return exists;
            }
        } catch (SQLException e) {
            throw new AccStorageException("DataBase.exist error. Query: " + query + "\nError:", e);
        }
    }

    public List<Account> getAllQuery(String query) throws AccStorageException {
        List<Account> accounts;
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                ResultSet rs = statement.executeQuery(query);
                accounts = new ArrayList<>();

                while (rs.next()) {
                    Account account = new Account(rs.getString("email"), rs.getString("pass"),
                            rs.getString("name"), rs.getInt("id"));
                    accounts.add(account);
                }
                return accounts;
            }
        } catch (SQLException e) {
            throw new AccStorageException("DataBase.getAll error. Query: " + query + "\nError:", e);
        }
    }

    public void addLinkQuery(String query) throws AccStorageException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            }
        } catch (SQLException e) {
            throw new AccStorageException("DataBase.addLink error. Query: " + query + "\nError:", e);
        }
    }
}