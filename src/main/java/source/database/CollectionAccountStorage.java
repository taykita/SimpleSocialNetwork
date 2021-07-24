package source.database;

import source.verification.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionAccountStorage implements AccountStorage {
    private Map<Integer, String> idEmail = new HashMap<>();
    private Map<String, Account> accounts = new HashMap<>();

    private int countUsers;

    @Override
    public Account add(Account account) {

        countUsers++;
        accounts.put(account.getEmail(), account);
        idEmail.put(account.getId(), account.getEmail());

        return account;
    }

    @Override
    public Account get(int id) {
        return accounts.get(idEmail.get(id));
    }

    @Override
    public Account get(String email) {
        return accounts.get(email);
    }

    @Override
    public boolean exist(String email) {
        return accounts.containsKey(email);
    }

    @Override
    public boolean confirmPass(String email, String pass) {
        return accounts.get(email).getPass().equals(pass);
    }

    @Override
    public List<Account> getAll() {
        return new ArrayList<Account>(accounts.values());
    }

    @Override
    public int getCountUsers() {
        return countUsers;
    }

    public void setCountUsers(int countUsers) {
        this.countUsers = countUsers;
    }
}
