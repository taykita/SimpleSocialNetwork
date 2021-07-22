package source.database;

import source.verification.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private Map<Integer, String> idEmail = new HashMap<>();
    private Map<String, Account> accounts = new HashMap<>();

    private int countUsers;

    public Account add(Account account) {

        countUsers++;
        accounts.put(account.getEmail(), account);
        idEmail.put(account.getId(), account.getEmail());

        return account;
    }

    public Account get(int id) {
        return accounts.get(idEmail.get(id));
    }

    public Account get(String email) {
        return accounts.get(email);
    }

    public boolean exist(String email) {
        return accounts.containsKey(email);
    }

    public boolean confirmPass(String email, String pass) {
        return accounts.get(email).getPass().equals(pass);
    }

    public List<Account> getAll() {
        return new ArrayList<Account>(accounts.values());
    }

    public int getCountUsers() {
        return countUsers;
    }

    public void setCountUsers(int countUsers) {
        this.countUsers = countUsers;
    }
}
