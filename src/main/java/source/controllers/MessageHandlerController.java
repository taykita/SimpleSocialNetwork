package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import source.controllers.entity.Account;
import source.controllers.entity.Message;
import source.controllers.entity.Post;
import source.database.AccountRepository;

import java.util.List;

@Controller
public class MessageHandlerController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AccountRepository accountRepository;

    @MessageMapping("/hello")
    public void greeting(Message message) throws Exception {
        Thread.sleep(1000);

        Account user = accountRepository.get(message.getId());
        List<Account> friends = accountRepository.getFriends(user);

        List<Post> posts = accountRepository.getPosts(user.getId());
        Post post = posts.get(0);

        for (Account account: friends) {
            messagingTemplate.convertAndSend("/news/" + account.getId(), post);
        }
    }

}