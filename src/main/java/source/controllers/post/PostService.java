package source.controllers.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Service
public class PostService {
    @Autowired
    public PostService(AccountRepository accountRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.accountRepository = accountRepository;
    }
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AccountRepository accountRepository;

    @Value("${post.pagination.count}")
    private int postCount;

    public Account getAccount(int id) throws AccStorageException {
        return accountRepository.getAccount(id);
    }

    private void sendMessage(String message) {

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send("booknetwork-topic", message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public Post addPost(Post post, Account account) throws AccStorageException {
        sendMessage("Post-Created-accID=" + account.getId() + "-postID=" + post.getId());
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
