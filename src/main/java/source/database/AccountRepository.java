package source.database;

import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.exception.AccStorageException;

import java.util.List;

public interface AccountRepository {
    Account addAccount(Account account) throws AccStorageException;

    Account updateAccount(Account account) throws AccStorageException;

    Account getAccount(int id) throws AccStorageException;

    Account getAccount(String email) throws AccStorageException;

    boolean existAccount(String email) throws AccStorageException;

    boolean confirmPass(String email, String pass) throws AccStorageException;

    List<Account> getAllAccounts() throws AccStorageException;

    void addFriend(Account user, Account friend) throws AccStorageException;

    void deleteFriend(Account user, Account friend) throws AccStorageException;

    boolean isFriend(Account user, Account friend) throws AccStorageException;

    List<Account> getFriends(int userId) throws AccStorageException;

    Post addPost(Post post, Account user) throws AccStorageException;

    void deletePost(Post post) throws AccStorageException;

    void updatePost(int oldPostId, String newPostText) throws AccStorageException;

    Post getPost(int postId) throws AccStorageException;

    List<Post> getPosts(int userId) throws AccStorageException;

    List<Post> getPosts(int userId, int firstPostId, int maxCount) throws AccStorageException;

    List<Post> getFriendsPosts(Account user, int firstPostId, int maxCount) throws AccStorageException;
}
