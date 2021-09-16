package source.controllers.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Service
public class PostService {
    @Autowired
    public PostService(AccountRepository accountRepository, SimpMessagingTemplate messagingTemplate) {
        this.accountRepository = accountRepository;
        this.messagingTemplate = messagingTemplate;
    }

    private final SimpMessagingTemplate messagingTemplate;
    private final AccountRepository accountRepository;

    public void createPostAndSendToUsers(Post post, User activeUser) throws AccStorageException {
        Account account = accountRepository.getAccount(activeUser.getId());
        post = accountRepository.addPost(post, account);

        post.setUserName(account.getName());
        List<Account> friends = accountRepository.getFriends(account.getId());
        for (Account friend: friends) {
            messagingTemplate.convertAndSendToUser(friend.getEmail(), "/queue/feed", post);
        }
        messagingTemplate.convertAndSend("/queue/user-page/" + activeUser.getId(), post);
    }

    public void deletePost(int id) throws AccStorageException {
        accountRepository.deletePost(accountRepository.getPost(id));
    }

    public void updatePost(Post post, int id) throws AccStorageException{
        accountRepository.updatePost(accountRepository.getPost(id), post);
    }

    public Post getPost(int id) throws AccStorageException {
        return accountRepository.getPost(id);
    }

    public List<Post> getPosts(User activeUser, int firstPostId) throws AccStorageException {
        int id = activeUser.getId();
        return accountRepository.getFriendsPosts(accountRepository.getAccount(id), firstPostId, 10);
    }
}
