package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import source.controllers.entity.Account;
import source.controllers.entity.Chat;
import source.controllers.entity.Message;
import source.exception.AccStorageException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class HibernateChatRepository implements ChatRepository {

    @Autowired
    public HibernateChatRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final SessionFactory sessionFactory;

    @Override
    public Chat addChat(List<Integer> ids, String name, boolean isPrivate) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int id = (Integer) session.createSQLQuery("INSERT INTO Chat(NAME, IS_PRIVATE, OWNER_ID) VALUES(:name, :isPrivate, :ownerId) RETURNING ID")
                        .setParameter("name", name)
                        .setParameter("isPrivate", isPrivate)
                        .setParameter("ownerId", ids.get(ids.size() - 1))
                        .getSingleResult();
                for (int accId : ids) {
                    session.createSQLQuery("INSERT INTO Accounts_chat (CHAT_ID, ACC_ID) VALUES (:chatId, :accId);")
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
                session.createSQLQuery("DELETE FROM Accounts_chat WHERE chat_id = :chatId AND acc_id = :accId")
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
                session.createSQLQuery("INSERT INTO Accounts_Chat(CHAT_ID, ACC_ID) VALUES(:chatId, :accId)")
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
            return session.get(Chat.class, id);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChat Error.", e);
        }
    }

    @Override
    public boolean existChat(int accId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.createSQLQuery("SELECT * FROM Accounts_Chat WHERE ACC_ID = :accId")
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
                Timestamp date = (Timestamp) session.createSQLQuery("INSERT INTO Message (TEXT, DATE, CHAT_ID, ACC_ID) VALUES (:text, now(), :chatId, :accId) RETURNING DATE;")
                        .setParameter("text", message.getText())
                        .setParameter("chatId", message.getChatId())
                        .setParameter("accId", message.getAccId())
                        .getSingleResult();
                message.setDate(date.toString());
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

            Iterator result = session.createSQLQuery("SELECT m.text, m.date, a.user_name, m.id FROM Message AS m LEFT OUTER JOIN Accounts_Chat AS ac ON m.chat_id = ac.chat_id AND m.acc_id = ac.acc_id LEFT OUTER JOIN Accounts_PrivateChat as ap ON m.chat_id = ap.chat_id LEFT OUTER JOIN Accounts AS a ON m.acc_id = a.id " +
                    "WHERE (ac.chat_id = :chatId OR ap.chat_id = :chatId) AND m.id < :firstMessageId ORDER BY m.id DESC LIMIT :maxCount")
                    .setParameter("chatId", chatId)
                    .setParameter("firstMessageId", firstMessageId)
                    .setParameter("maxCount", maxCount)
                    .getResultList()
                    .iterator();

            List<Message> messages = new ArrayList<>();

            while (result.hasNext()) {
                Object[] row = (Object[]) result.next();
                Message message = new Message();
                message.setText((String) row[0]);
                Timestamp timestamp = (Timestamp) row[1];
                message.setDate(timestamp.toString());
                message.setName((String) row[2]);
                messages.add(message);
                message.setId((Integer) row[3]);
            }

            return messages;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getMessages Error");
        }
    }

    @Override
    public List<Chat> getChats(int userId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            Iterator result = session.createSQLQuery("SELECT c.id, c.name FROM Accounts_Chat as ac JOIN Chat AS c ON ac.chat_id=c.id WHERE ac.ACC_id=:userId")
                    .setParameter("userId", userId)
                    .getResultList()
                    .iterator();
            List<Chat> chats = new ArrayList<>();

            while (result.hasNext()) {
                Object[] row = (Object[]) result.next();
                Chat chat = new Chat();
                chat.setId((Integer) row[0]);
                chat.setName((String) row[1]);
                chats.add(chat);
            }

            return chats;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChats Error");
        }
    }

    @Override
    public List<String> getUsersEmail(int chatId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            Iterator result = session.createSQLQuery("SELECT a.email FROM Accounts as a LEFT OUTER JOIN Accounts_Chat as ac ON a.id = ac.acc_id LEFT OUTER JOIN Accounts_PrivateChat as ap ON a.id = ap.user_id WHERE ac.chat_id = :chatId OR ap.chat_id = :chatId")
                    .setParameter("chatId", chatId)
                    .getResultList()
                    .iterator();

            List<String> usersEmail = new ArrayList<>();

            while (result.hasNext()) {
                usersEmail.add((String) result.next());
            }

            return usersEmail;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChats Error");
        }
    }

    @Override
    public Chat addPrivateChat(int userId, int friendId, String name) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int id = (Integer) session.createSQLQuery("INSERT INTO Chat(NAME, IS_PRIVATE, OWNER_ID) VALUES(:name, :isPrivate, :ownerId) RETURNING ID")
                        .setParameter("name", name)
                        .setParameter("isPrivate", true)
                        .setParameter("ownerId", userId)
                        .getSingleResult();

                session.createSQLQuery("INSERT INTO Accounts_PrivateChat (CHAT_ID, USER_ID, FRIEND_ID) VALUES (:chatId, :userId, :friendId);")
                        .setParameter("chatId", id)
                        .setParameter("userId", userId)
                        .setParameter("friendId", friendId)
                        .executeUpdate();

                session.createSQLQuery("INSERT INTO Accounts_PrivateChat (CHAT_ID, USER_ID, FRIEND_ID) VALUES (:chatId, :userId, :friendId);")
                        .setParameter("chatId", id)
                        .setParameter("userId", friendId)
                        .setParameter("friendId", userId)
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
            return (Chat) session.createSQLQuery("SELECT c.id, c.name FROM Chat as c JOIN Accounts_PrivateChat as ap ON c.id = ap.chat_id WHERE user_id = :userId AND friend_id = :friendId")
                    .setParameter("userId", userId)
                    .setParameter("friendId", friendId)
                    .addEntity(Chat.class)
                    .getSingleResult();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChat Error.", e);
        }
    }

    @Override
    public boolean existPrivateChat(int userId, int friendId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.createSQLQuery("SELECT * FROM Accounts_PrivateChat WHERE USER_ID = :userId AND FRIEND_ID = :friendId")
                    .setParameter("userId", userId)
                    .setParameter("friendId", friendId)
                    .uniqueResult() != null;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChat Error.", e);
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
            throw new AccStorageException("Hibernate getPost Error.", e);
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
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

}
