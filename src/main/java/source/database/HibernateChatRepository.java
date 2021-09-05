package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import source.controllers.entity.Chat;
import source.controllers.entity.Message;
import source.exception.AccStorageException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class HibernateChatRepository implements ChatRepository{

    @Autowired
    public HibernateChatRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final SessionFactory sessionFactory;

    @Override
    public Chat addChat(List<Integer> ids, String name) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Chat chat = new Chat();
                chat.setName(name);

                int id = (Integer) session.save(chat);
                for (int accId: ids) {
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
        try (Session session = sessionFactory.openSession()){
            return session.get(Message.class, id);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getMessage Error");
        }
    }

    @Override
    public List<Message> getMessages(int chatId, int firstMessageId, int maxCount) throws AccStorageException {
        try (Session session = sessionFactory.openSession()){

            Iterator result = session.createSQLQuery("SELECT m.text, m.date, a.user_name, m.id FROM Message AS m JOIN Accounts_Chat AS ac ON m.chat_id = ac.chat_id AND m.acc_id = ac.acc_id JOIN Accounts AS a ON m.acc_id = a.id " +
                    "WHERE ac.chat_id = :chatId AND m.id < :firstMessageId ORDER BY m.id DESC LIMIT :maxCount")
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
        try (Session session = sessionFactory.openSession()){

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
        try (Session session = sessionFactory.openSession()){

            Iterator result = session.createSQLQuery("SELECT a.email FROM Accounts as a JOIN Accounts_Chat as ac ON a.id = ac.acc_id WHERE ac.chat_id = :chatId")
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

}
