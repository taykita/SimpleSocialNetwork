package DataBase;

import SignLogIn.Account;

import java.util.*;

public class DataBase {
    private Map<String, String> emailPassDB = new HashMap<>();
    private Map<String, Account> accounts = new HashMap<>();

    public void add(Account account) {
        accounts.put(account.getEmail(), account);
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
}
