package source.controllers.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.chat.Chat;
import source.controllers.entity.chat.ChatType;
import source.controllers.entity.chat.Message;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.exception.AccStorageException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    public ChatService(AccountRepository accountRepository, SimpMessagingTemplate messagingTemplate,
                       ChatRepository chatRepository) {
        this.accountRepository = accountRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AccountRepository accountRepository;

    public List<Account> getFriends(User activeUser) throws AccStorageException {
        return accountRepository.getFriends(activeUser.getId());
    }

    public void addChat(User activeUser, Integer[] accIds, String name) throws AccStorageException {
        List<Integer> ids;
        if (accIds == null) {
            ids = new ArrayList<>();
        } else {
            ids = new ArrayList<>(Arrays.asList(accIds));
        }
        ids.add(activeUser.getId());
        chatRepository.addChat(ids, name, ChatType.PUBLIC);
    }

    public Chat getChat(int id, Account account) throws AccStorageException {
        Chat chat = chatRepository.getChat(id);
        fixPrivateChatName(account, chat);
        return chat;
    }

    public Account getAccount(User activeUser) throws AccStorageException {
        return accountRepository.getAccount(activeUser.getId());
    }

    public Message addMessage(Message message) throws AccStorageException {
        return chatRepository.addMessage(message);
    }

    public void sendMessageToUsers(Message message) throws AccStorageException {
        for (String user : chatRepository.getUsersEmail(message.getChatId())) {
            messagingTemplate.convertAndSendToUser(user,
                    "/queue/chat/" + message.getChatId(), message);
        }
    }

    public List<Message> getMessages(int chatId, int firstMessageId, int count) throws AccStorageException {
        return chatRepository.getMessages(chatId, firstMessageId, 10);
    }

    public String redirectToChat(int friendId, int userId) throws AccStorageException {
        if (chatRepository.existPrivateChat(userId, friendId)) {
            Chat privateChat = chatRepository.getPrivateChat(userId, friendId);
            return "redirect:chat?id=" + privateChat.getId();
        } else {
            String name = accountRepository.getAccount(userId).getName() + "-" + accountRepository.getAccount(friendId).getName();
            Chat chat = chatRepository.addPrivateChat(userId, friendId, name);
            return "redirect:chat?id=" + chat.getId();
        }
    }

    public List<Account> getAllUsersFromChat(int chatId) throws AccStorageException {
        return chatRepository.getUsersFromChat(chatId);
    }

    public List<Account> getOtherFriends() throws AccStorageException {
        return accountRepository.getAllAccounts();
    }

    public void deleteChatUser(int id, int chatId) throws AccStorageException {
        chatRepository.deleteChatUser(id, chatId);
    }

    public void addChatUser(int id, int chatId) throws AccStorageException {
        chatRepository.addChatUser(id, chatId);
    }

    public List<Chat> getChats(User activeUser) throws AccStorageException {
        Account userAccount = accountRepository.getAccount(activeUser.getId());
        List<Chat> chats = chatRepository.getChats(activeUser.getId());
        for (Chat chat : chats) {
            fixPrivateChatName(userAccount, chat);
        }
        return chats;
    }

    public boolean authUser(int chatId, int accId) throws AccStorageException {
        return chatRepository.authChatUser(chatId, accId);
    }

    private void fixPrivateChatName(Account account, Chat chat) {
        if (chat.getType() == ChatType.PRIVATE.ordinal() + 1) {
            if (account.getId() == chat.getOwnerId()) {
                chat.setName(chat.getName().substring(account.getName().length() + 1, chat.getName().length()));
            } else {
                chat.setName(chat.getName().substring(0, account.getName().length()));
            }
        }
    }

}
