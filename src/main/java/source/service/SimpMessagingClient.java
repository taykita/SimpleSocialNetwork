package source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.controllers.entity.chat.Message;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.exception.AccStorageException;

import java.util.List;

@Service
public class SimpMessagingClient implements MessagingClient {
    @Autowired
    public SimpMessagingClient(ChatRepository chatRepository, SimpMessagingTemplate messagingTemplate,
                               AccountRepository accountRepository) {
        this.chatRepository = chatRepository;
        this.messagingTemplate = messagingTemplate;
        this.accountRepository = accountRepository;
    }

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AccountRepository accountRepository;

    @Override
    public void sendMessageToChat(String destination, Message message) throws AccStorageException {
        message = chatRepository.addMessage(message);

        for (String user : chatRepository.getUsersEmail(message.getChatId())) {
            messagingTemplate.convertAndSendToUser(user,
                    destination + message.getChatId(), message);
        }
    }

    public void sendPostToUsers(Post post, int activeUserId) throws AccStorageException {
        List<Account> friends = accountRepository.getFriends(activeUserId);

        for (Account friend: friends) {
            messagingTemplate.convertAndSendToUser(friend.getEmail(), "/queue/feed", post);
        }
        messagingTemplate.convertAndSend("/queue/user-page/" + activeUserId, post);
    }


}
