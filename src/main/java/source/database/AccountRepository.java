package source.database;

import source.controllers.entity.Account;
import source.controllers.entity.Message;
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

    void addFriend(Account user, Account friend) throws AccStorageException;

    void deleteFriend(Account user, Account friend) throws AccStorageException;

    boolean isFriend(Account user, Account friend) throws AccStorageException;

    List<Account> getFriends(Account user) throws AccStorageException;

    Post addPost(Post post, Account user) throws AccStorageException;

    void deletePost(Post post) throws AccStorageException;

    void updatePost(Post oldPost, Post newPost) throws AccStorageException;

    Post getPost(int postId) throws AccStorageException;

    List<Post> getPosts(int userId) throws AccStorageException;

    List<Post> getPosts(int userId, int firstPostId, int maxCount) throws AccStorageException;

    List<Post> getFriendsPosts(Account user, int firstPostId, int maxCount) throws AccStorageException;

    void addMessage(Message message) throws AccStorageException;

    Message getMessage(int id) throws AccStorageException;
}
