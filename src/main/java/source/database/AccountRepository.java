package source.database;

import source.exception.AccStorageException;
import source.verification.entity.Account;

import java.util.List;

public interface AccountRepository {
    Account add(Account account) throws AccStorageException;

    Account get(int id) throws AccStorageException;

    Account get(String email) throws AccStorageException;

    boolean exist(String email) throws AccStorageException;

    boolean confirmPass(String email, String pass) throws AccStorageException;

    List<Account> getAll() throws AccStorageException;

    void addFriend(int userId, int friendId) throws AccStorageException;

    boolean isFriend(int userId, int friendId) throws AccStorageException;

    List<Account> getFriends(int userId) throws AccStorageException;
}
