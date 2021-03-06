package source.database;

import source.controllers.entity.Account;
import source.controllers.entity.chat.Chat;
import source.controllers.entity.chat.ChatType;
import source.controllers.entity.chat.Message;
import source.exception.AccStorageException;

import java.util.List;

public interface ChatRepository {
    Chat addChat(List<Integer> ids, String name, ChatType type) throws AccStorageException;

    void updateChat(Chat chat) throws AccStorageException;

    void deleteChatUser(int accId, int chatId) throws AccStorageException;

    void addChatUser(int accId, int chatId) throws AccStorageException;
    
    Chat getChat(int id) throws AccStorageException;

    boolean existChat(int id) throws AccStorageException;

    Message addMessage(Message message) throws AccStorageException;

    List<Message> getMessages(int chatId, int firstMessageId, int maxCount) throws AccStorageException;

    List<Chat> getChats(int userId) throws AccStorageException;

    List<String> getUsersEmail(int chatId) throws AccStorageException;

    Chat addPrivateChat(int userId, int friendId, String name) throws AccStorageException;

    Chat getPrivateChat(int userId, int friendId) throws AccStorageException;

    boolean existPrivateChat(int userId, int friendId) throws AccStorageException;

    List<Account> getUsersFromChat(int chatId) throws AccStorageException;

    List<Account> getOtherUsersFromChat(int chatId, int userId) throws AccStorageException;

    boolean authChatUser(int chatId, int accId) throws AccStorageException;
}
