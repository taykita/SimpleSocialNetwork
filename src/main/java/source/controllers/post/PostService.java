package source.controllers.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Service
public class PostService {
    @Autowired
    public PostService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    @Value("${post.pagination.count}")
    private int postCount;

    public Account getAccount(int id) throws AccStorageException {
        return accountRepository.getAccount(id);
    }

    public Post addPost(Post post, Account account) throws AccStorageException {
        post = accountRepository.addPost(post, account);

        post.setUserName(account.getName());
        return post;
    }

    public List<Account> getFriends(int userId) throws AccStorageException {
        return accountRepository.getFriends(userId);
    }

    public void deletePost(int id) throws AccStorageException {
        accountRepository.deletePost(accountRepository.getPost(id));
    }

    public void updatePost(Post post, int id) throws AccStorageException {
        accountRepository.updatePost(accountRepository.getPost(id), post);
    }

    public Post getPost(int id) throws AccStorageException {
        return accountRepository.getPost(id);
    }

    public List<Post> getPosts(int id, int firstPostId) throws AccStorageException {
        return accountRepository.getFriendsPosts(accountRepository.getAccount(id), firstPostId, postCount);
    }
}
