package source.database;

import source.controllers.entity.Account;
import source.controllers.entity.Chat;
import source.controllers.entity.Message;
import source.exception.AccStorageException;

import java.util.List;

public interface ChatRepository {
    Chat addChat(List<Integer> ids, String name) throws AccStorageException;

    Chat getChat(int id) throws AccStorageException;

    boolean existChat(int id) throws AccStorageException;

    Message addMessage(Message message) throws AccStorageException;

    Message getMessage(int id) throws AccStorageException;

    List<Message> getMessages(int chatId, int firstMessageId, int maxCount) throws AccStorageException;

    List<Chat> getChats(int userId) throws AccStorageException;

    List<String> getUsersEmail(int chatId) throws AccStorageException;
}