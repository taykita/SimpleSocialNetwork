package source.database;

import source.verification.Account;

import java.util.List;

public interface AccountStorage {
    Account add(Account account);

    Account get(int id);

    Account get(String email);

    boolean exist(String email);

    boolean confirmPass(String email, String pass);

    List<Account> getAll();

}
