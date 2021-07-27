package source.database;

import org.apache.commons.dbcp2.BasicDataSource;
import source.exception.DBException;
import source.verification.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//TODO Разобраться с закрыванием statement при эксэпшене
public class QueryController {
    public QueryController() {
        createPull();
    }
//TODO Разобраться с property
    private void createPull() {
        connectionPull = new BasicDataSource();
        connectionPull.setDriverClassName("org.postgresql.Driver");
        connectionPull.setUrl("jdbc:postgresql://78.24.220.161/booknetwork_db");
        connectionPull.setUsername("admin");
        connectionPull.setPassword("admin");
    }

    private BasicDataSource connectionPull;

    public int queryAdd(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);
            rs.next();
            int id = rs.getInt("id");

            statement.close();
            connection.close();
            return id;
        } catch (SQLException e) {
            throw new DBException("DataBase.add error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public Account getQuery(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);
            rs.next();
            Account account = new Account(rs.getString("email"), rs.getString("pass"),
                    rs.getString("name"), rs.getInt("id"));

            statement.close();
            connection.close();
            return account;
        } catch (SQLException e) {
            throw new DBException("DataBase.get error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public boolean existQuery(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);
            rs.next();
            boolean exists = rs.getBoolean("exists");

            statement.close();
            connection.close();
            return exists;
        } catch (SQLException e) {
            throw new DBException("DataBase.exist error. \nQuery: " + query + "\nError:" + e.getMessage());
        }
    }

    public List<Account> getAllQuery(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);
            List<Account> accounts = new ArrayList<>();

            while (rs.next()) {
                Account account = new Account(rs.getString("email"), rs.getString("pass"),
                        rs.getString("name"), rs.getInt("id"));
                accounts.add(account);
            }

            statement.close();
            connection.close();
            return accounts;
        } catch (SQLException e) {
            throw new DBException("DataBase.getAll error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public int getCountQuery(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);
            rs.next();
            int count = rs.getInt("count");

            statement.close();
            connection.close();
            return count;
        } catch (SQLException e) {
            throw new DBException("DataBase.getCount error. Query: " + query + "\nError:" + e.getMessage());
        }
    }
}