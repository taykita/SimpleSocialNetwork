package source.controllers.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.controllers.entity.chat.Chat;
import source.controllers.entity.chat.ChatType;
import source.controllers.entity.chat.Message;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.exception.AccStorageException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    public ChatService(AccountRepository accountRepository,
                       ChatRepository chatRepository) {
        this.accountRepository = accountRepository;
        this.chatRepository = chatRepository;
    }

    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;

    @Value("${message.pagination.count}")
    private int messageCount;

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

    public List<Message> getMessages(int chatId, int firstMessageId) throws AccStorageException {
        return chatRepository.getMessages(chatId, firstMessageId, messageCount);
    }

    public int redirectToChat(int friendId, int userId) throws AccStorageException {
        if (chatRepository.existPrivateChat(userId, friendId)) {
            Chat privateChat = chatRepository.getPrivateChat(userId, friendId);
            return privateChat.getId();
        } else {
            String name = accountRepository.getAccount(userId).getName() + "-" + accountRepository.getAccount(friendId).getName();
            Chat chat = chatRepository.addPrivateChat(userId, friendId, name);
            return chat.getId();
        }
    }

    public List<Account> getAllUsersFromChat(int chatId) throws AccStorageException {
        return chatRepository.getUsersFromChat(chatId);
    }

    public List<Account> getOtherFriends(int chatId, int userId) throws AccStorageException {
        return chatRepository.getOtherUsersFromChat(chatId, userId);
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


    private void fixPrivateChatName(Account account, Chat chat) {
        if (chat.getType() == ChatType.PRIVATE.ordinal() + 1) {
            if (account.getId() == chat.getOwnerId()) {
                chat.setName(chat.getName().substring(account.getName().length() + 1));
            } else {
                chat.setName(chat.getName().substring(0, account.getName().length()));
            }
        }
    }

}
