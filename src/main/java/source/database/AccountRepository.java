package source.database;

import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.exception.AccStorageException;

import java.util.List;

public interface AccountRepository {
    Account add(Account account) throws AccStorageException;

    Account get(int id) throws AccStorageException;

    Account get(String email) throws AccStorageException;

    boolean exist(String email) throws AccStorageException;

    boolean confirmPass(String email, String pass) throws AccStorageException;

    List<Account> getAll() throws AccStorageException;

    void addFriend(int userId, int friendId) throws AccStorageException;

    void deleteFriend(int userId, int friendId) throws AccStorageException;

    boolean isFriend(int userId, int friendId) throws AccStorageException;

    List<Account> getFriends(int userId) throws AccStorageException;

    void addPost(Post post, int userId) throws AccStorageException;
}
