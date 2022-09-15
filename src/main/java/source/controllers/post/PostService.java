package source.controllers.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;
import source.service.query.QueryClient;
import source.service.query.entity.AnalysisDTO;

import java.util.List;

import static source.configuration.Constants.MONITORING_QUEUE;

@Service
public class PostService {
    @Autowired
    public PostService(AccountRepository accountRepository, QueryClient queryClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.queryClient = queryClient;
        this.accountRepository = accountRepository;
    }

    private final ObjectMapper objectMapper;
    private final QueryClient queryClient;
    private final AccountRepository accountRepository;

    @Value("${post.pagination.count}")
    private int postCount;

    public Account getAccount(int id) throws AccStorageException {
        return accountRepository.getAccount(id);
    }

    public Post addPost(Post post, int userId) throws AccStorageException, JsonProcessingException {
        Account account = accountRepository.getAccount(userId);

        post = accountRepository.addPost(post, account);
        post.setUserName(account.getName());

        sendMessageToQuery(post);

        return post;
    }

    private void sendMessageToQuery(Post post) throws JsonProcessingException {
        AnalysisDTO analysisDTO = new AnalysisDTO("Post", AnalysisDTO.Action.CREATED, post);
        queryClient.sendMessage(objectMapper.writeValueAsString(analysisDTO), MONITORING_QUEUE);
    }

    public List<Account> getFriends(int userId) throws AccStorageException {
        return accountRepository.getFriends(userId);
    }

    public void deletePost(int id) throws AccStorageException {
        accountRepository.deletePost(accountRepository.getPost(id));
    }

    public void updatePost(String postText, int id) throws AccStorageException {
        accountRepository.updatePost(id, postText);
    }

    public Post getPost(int id) throws AccStorageException {
        return accountRepository.getPost(id);
    }

    public List<Post> getPosts(int id, int firstPostId) throws AccStorageException {
        return accountRepository.getFriendsPosts(accountRepository.getAccount(id), firstPostId, postCount);
    }
}
