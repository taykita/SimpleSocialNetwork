package source.database;

import source.verification.Account;

import java.sql.Statement;
import java.util.List;

public class DataBaseAccountStorage implements AccountStorage{

    public DataBaseAccountStorage() {
        this.statement = new Driver().getStatement();
        this.queryController = new QueryController(statement);
    }

    Statement statement;
    private final QueryController queryController;

    @Override
    public Account add(Account account) {
        queryController.query("INSERT INTO accounts (email, name, pass) " +
                "VALUES ('" + account.getEmail() + "', '" + account.getUserName() + "', '" + account.getPass() + "');");
        return account;
    }

    @Override
    public Account get(int id) {
        return queryController.getQuery("SELECT * FROM accounts WHERE id =" + id + ";");
    }

    @Override
    public Account get(String email) {
        return queryController.getQuery("SELECT * FROM accounts WHERE email ='" + email + "';");
    }

    @Override
    public boolean exist(String email) {
        return queryController.existQuery("SELECT EXISTS(SELECT email FROM accounts WHERE email='" + email + "');");
    }

    @Override
    public boolean confirmPass(String email, String pass) {
        return get(email).getPass().equals(pass);
    }

    @Override
    public List<Account> getAll() {
        return queryController.getAllQuery("SELECT * FROM accounts;");
    }

    @Override
    public int getCountUsers() {
        return queryController.getCountQuery("SELECT MAX(id) AS count FROM accounts");
    }
}
