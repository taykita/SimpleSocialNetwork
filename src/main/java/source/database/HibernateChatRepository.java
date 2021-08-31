package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import source.controllers.entity.Account;
import source.controllers.entity.Chat;
import source.controllers.entity.Message;
import source.exception.AccStorageException;

import java.util.List;

public class HibernateChatRepository implements ChatRepository{

    @Autowired
    public HibernateChatRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final SessionFactory sessionFactory;

    @Override
    public Chat add(List<Integer> ids, String name) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Chat chat = new Chat();
            chat.setName(name);

            int id = (Integer) session.save(chat);
            String queryString = "INSERT INTO Chat (CHAT_ID, ACC_ID) VALUES (" + id + ", :accId);";
            for (int accId: ids) {
                session.createQuery(queryString)
                    .setParameter("accId", accId);
            }

            session.getTransaction().commit();

            return get(id);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addChat Error.", e);
        }
    }

    @Override
    public Chat get(int id) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Chat.class, id);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getChat Error.", e);
        }
    }

    @Override
    public boolean exist(int id) throws AccStorageException {
        return get(id) != null;
    }

    @Override
    public Message addMessage(Message message, int chatId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            int id = (int) session.createSQLQuery("INSERT INTO Message (TEXT, DATE, CHAT_ID) VALUES (:text, now(), :chat_id) RETURNING ID;")
                    .setParameter("text", message.getText())
                    .setParameter("chat_id", chatId)
                    .getSingleResult();

            session.getTransaction().commit();
            return getMessage(id);
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
    public List<Message> getMessages(int chatId) throws AccStorageException {
        return null;
    }

}
