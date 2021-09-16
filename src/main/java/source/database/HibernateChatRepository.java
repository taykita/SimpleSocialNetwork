package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import source.controllers.entity.Account;
import source.controllers.entity.chat.Chat;
import source.controllers.entity.chat.ChatType;
import source.controllers.entity.chat.Message;
import source.exception.AccStorageException;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class HibernateChatRepository implements ChatRepository {

    @Autowired
    public HibernateChatRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final SessionFactory sessionFactory;

    @Override
    public Chat addChat(List<Integer> ids, String name, ChatType type) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int id = (Integer) session.getNamedNativeQuery("Chat.addChat")
                        .setParameter("name", name)
                        .setParameter("type", type.ordinal() + 1)
                        .setParameter("ownerId", ids.get(ids.size() - 1))
                        .getSingleResult();
                for (int accId : ids) {
                    session.getNamedNativeQuery("Chat.addUserInChat")
                            .setParameter("chatId", id)
                            .setParameter("accId", accId)
                            .executeUpdate();
                }

                transaction.commit();

                return getChat(id);
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addChat Error.", e);
        }
    }

    @Override
    public void updateChat(Chat chat) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(chat);
                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addChat Error.", e);
        }
    }

    @Override
    public void deleteChatUser(int accId, int chatId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.getNamedNativeQuery("Chat.deleteChatUser")
                        .setParameter("chatId", chatId)
                        .setParameter("accId", accId)
                        .executeUpdate();

                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate deleteChatUser Error.", e);
        }
    }

    @Override
    public void addChatUser(int accId, int chatId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.getNamedNativeQuery("Chat.addChatUser")
                        .setParameter("chatId", chatId)
                        .setParameter("accId", accId)
                        .executeUpdate();

                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addChatUser Error.", e);
        }
    }

    @Override
    public Chat getChat(int id) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (Chat) session.getNamedNativeQuery("Chat.getChat")
                    .setParameter("chatId", id)
                    .addEntity(Chat.class)
                    .getSingleResult();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChat Error.", e);
        }
    }

    @Override
    public boolean existChat(int accId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.getNamedNativeQuery("Chat.existChat")
                    .setParameter("accId", accId)
                    .uniqueResult() != null;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChat Error.", e);
        }
    }

    @Override
    public Message addMessage(Message message) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Timestamp date = (Timestamp) session.getNamedNativeQuery("Chat.addMessage")
                        .setParameter("text", message.getText())
                        .setParameter("chatId", message.getChatId())
                        .setParameter("accId", message.getAccId())
                        .getSingleResult();
                message.setDate(date);
                transaction.commit();
                return message;
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }

        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addMessage Error.", e);
        }
    }

    @Override
    public Message getMessage(int id) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Message.class, id);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getMessage Error");
        }
    }

    @Override
    public List<Message> getMessages(int chatId, int firstMessageId, int maxCount) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            return session.getNamedNativeQuery("Chat.getMessages")
                    .setParameter("chatId", chatId)
                    .setParameter("firstMessageId", firstMessageId)
                    .setParameter("maxCount", maxCount)
                    .addEntity(Message.class)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getMessages Error");
        }
    }

    @Override
    public List<Chat> getChats(int userId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            return session.getNamedNativeQuery("Chat.getChats")
                    .setParameter("userId", userId)
                    .addEntity(Chat.class)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChats Error");
        }
    }

    @Override
    public List<String> getUsersEmail(int chatId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            return session.createSQLQuery("SELECT a.email FROM Accounts as a JOIN Accounts_Chat as ac ON a.id = ac.acc_id WHERE ac.chat_id = :chatId")
                    .setParameter("chatId", chatId)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChats Error");
        }
    }

    @Override
    public Chat addPrivateChat(int userId, int friendId, String name) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int id = (Integer) session.createSQLQuery("INSERT INTO Chat(NAME, TYPE, OWNER_ID) VALUES(:name, :type, :ownerId) RETURNING ID")
                        .setParameter("name", name)
                        .setParameter("type", 2)
                        .setParameter("ownerId", userId)
                        .getSingleResult();

                session.createSQLQuery("INSERT INTO Accounts_Chat (CHAT_ID, ACC_ID) VALUES (:chatId, :accId);")
                        .setParameter("chatId", id)
                        .setParameter("accId", userId)
                        .executeUpdate();

                session.createSQLQuery("INSERT INTO Accounts_Chat (CHAT_ID, ACC_ID) VALUES (:chatId, :accId);")
                        .setParameter("chatId", id)
                        .setParameter("accId", friendId)
                        .executeUpdate();

                transaction.commit();

                return getChat(id);
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addPrivateChat Error.", e);
        }
    }

    @Override
    public Chat getPrivateChat(int userId, int friendId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (Chat) session.createSQLQuery("SELECT DISTINCT c.id, c.name, c.type, c.owner_id FROM Accounts_Chat as ac1 INNER JOIN Accounts_Chat as ac2 ON ac1.chat_id = ac2.chat_id and ac2.acc_id != :userId " +
                    "INNER JOIN Chat as c ON ac1.chat_id = c.id WHERE c.type = 2 AND ac1.acc_id = :userId AND ac2.acc_id = :friendId")
                    .setParameter("userId", userId)
                    .setParameter("friendId", friendId)
                    .addEntity(Chat.class)
                    .getSingleResult();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPrivateChat Error.", e);
        }
    }

    @Override
    public boolean existPrivateChat(int userId, int friendId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return !session.createSQLQuery("SELECT ac1.chat_id as chat1_id, ac2.chat_id as chat2_id FROM Accounts_Chat as ac1 INNER JOIN Accounts_Chat as ac2 ON ac1.chat_id = ac2.chat_id and ac2.acc_id != :userId WHERE ac1.acc_id = :userId AND ac2.acc_id = :friendId")
                    .setParameter("userId", userId)
                    .setParameter("friendId", friendId)
                    .getResultList()
                    .isEmpty();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate existPrivateChat Error.", e);
        }
    }

    @Override
    public List<Account> getUsersFromChat(int chatId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            List<Account> users = session.createSQLQuery("SELECT a.id, a.email, a.pass, a.user_name FROM Accounts as a JOIN Accounts_Chat as ac ON a.id = ac.acc_id WHERE ac.chat_id = :chatId")
                    .setParameter("chatId", chatId)
                    .addEntity(Account.class)
                    .getResultList();

            return users;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getUsersFromChat Error.", e);
        }
    }

    @Override
    public List<Account> getOtherUsersFromChat(int chatId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            List<Account> users = session.createSQLQuery("SELECT a.id, a.email, a.pass, a.user_name FROM Accounts as a JOIN Accounts_Chat as ac ON a.id = ac.acc_id WHERE ac.chat_id <> :chatId")
                    .setParameter("chatId", chatId)
                    .addEntity(Account.class)
                    .getResultList();

            return users;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getOtherUsersFromChat Error.", e);
        }
    }

    @Override
    public boolean authChatUser(int chatId, int accId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.createSQLQuery("SELECT a.id FROM Accounts as a JOIN Accounts_Chat as ac ON a.id = ac.acc_id WHERE ac.chat_id = :chatId AND a.id = :accId")
                    .setParameter("chatId", chatId)
                    .setParameter("accId", accId)
                    .uniqueResult() != null;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate authUser Error");
        }
    }

}
