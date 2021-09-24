package source.service;

import source.controllers.entity.Post;
import source.controllers.entity.chat.Message;
import source.exception.AccStorageException;

public interface MessagingClient {
    void sendMessageToChat(String destination, Message message) throws AccStorageException;

    void sendPostToUsers(Post post, int activeUserId) throws AccStorageException;
}
