package source.database;

import source.exception.DBException;
import source.verification.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QueryController {
    public QueryController(Statement statement) {
        this.statement = statement;
    }

    Statement statement;

    public void query(String query) {
        try {
            statement.execute(query);
        } catch (SQLException e) {
            throw new DBException("DataBase.add error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public Account getQuery(String query) {
        try {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            return new Account(rs.getString("email"), rs.getString("pass"),
                    rs.getString("name"), rs.getInt("id"));
        } catch (SQLException e) {
            throw new DBException("DataBase.get error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public boolean existQuery(String query) {
        try {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            return rs.getBoolean("exists");
        } catch (SQLException e) {
            throw new DBException("DataBase.exist error. \nQuery: " + query + "\nError:" + e.getMessage());
        }
    }

    public List<Account> getAllQuery(String query) {
        try {
            List<Account> accounts = new ArrayList<>();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Account account = new Account(rs.getString("email"), rs.getString("pass"),
                        rs.getString("name"), rs.getInt("id"));
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            throw new DBException("DataBase.getAll error. Query: " + query + "\nError:" + e.getMessage());
        }
    }

    public int getCountQuery(String query) {
        try {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            return rs.getInt("count");
        } catch (SQLException e) {
            throw new DBException("DataBase.getCount error. Query: " + query + "\nError:" + e.getMessage());
        }
    }
}