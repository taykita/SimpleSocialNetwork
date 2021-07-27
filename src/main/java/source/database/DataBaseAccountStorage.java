package source.database;

import source.verification.Account;

import java.util.List;

public class DataBaseAccountStorage implements AccountStorage {

    public DataBaseAccountStorage() {
        this.queryController = new QueryController();
    }

    private final QueryController queryController;

    @Override
    public Account add(Account account) {
        int id = queryController.queryAdd("INSERT INTO accounts (email, name, pass) " +
                "VALUES ('" + account.getEmail() + "', '" + account.getUserName() + "', '" + account.getPass() + "') " +
                "RETURNING id;");
        account.setId(id);
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

}

