package source.database;

import source.exception.DBException;
import source.verification.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//TODO Разобраться с закрыванием statement при эксэпшене
public class QueryController {
    public QueryController() {
        this.connectionPull = new ConnectionPull();
    }

    ConnectionPull connectionPull;

    public void query(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.get().createStatement();

            statement.execute(query);

            statement.close();
            connection.setFree(true);
        } catch (SQLException e) {
            throw new DBException("DataBase.add error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public Account getQuery(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.get().createStatement();

            ResultSet rs = statement.executeQuery(query);
            rs.next();
            Account account = new Account(rs.getString("email"), rs.getString("pass"),
                    rs.getString("name"), rs.getInt("id"));

            statement.close();
            connection.setFree(true);
            return account;
        } catch (SQLException e) {
            throw new DBException("DataBase.get error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public boolean existQuery(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.get().createStatement();

            ResultSet rs = statement.executeQuery(query);
            rs.next();
            boolean exists = rs.getBoolean("exists");

            statement.close();
            connection.setFree(true);
            return exists;
        } catch (SQLException e) {
            throw new DBException("DataBase.exist error. \nQuery: " + query + "\nError:" + e.getMessage());
        }
    }

    public List<Account> getAllQuery(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.get().createStatement();

            ResultSet rs = statement.executeQuery(query);
            List<Account> accounts = new ArrayList<>();

            while (rs.next()) {
                Account account = new Account(rs.getString("email"), rs.getString("pass"),
                        rs.getString("name"), rs.getInt("id"));
                accounts.add(account);
            }

            statement.close();
            connection.setFree(true);
            return accounts;
        } catch (SQLException e) {
            throw new DBException("DataBase.getAll error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public int getCountQuery(String query) {
        try {
            Connection connection = connectionPull.getConnection();
            Statement statement = connection.get().createStatement();

            ResultSet rs = statement.executeQuery(query);
            rs.next();
            int count = rs.getInt("count");

            statement.close();
            connection.setFree(true);
            return count;
        } catch (SQLException e) {
            throw new DBException("DataBase.getCount error. Query: " + query + "\nError:" + e.getMessage());
        }
    }
}