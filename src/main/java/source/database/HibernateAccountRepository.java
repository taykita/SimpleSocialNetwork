package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import source.controllers.entity.Account;
import source.controllers.entity.Message;
import source.controllers.entity.Post;
import source.exception.AccStorageException;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Repository
public class HibernateAccountRepository implements AccountRepository {

    @Autowired
    public HibernateAccountRepository(SessionFactory sessionFactory, PasswordEncoder passwordEncoder) {
        this.sessionFactory = sessionFactory;
        this.passwordEncoder = passwordEncoder;
    }

    private final PasswordEncoder passwordEncoder;

    private final SessionFactory sessionFactory;

    @Override
    public Account add(Account account) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            int id = (Integer) session.save(account);
            account.setPass(passwordEncoder.encode(account.getPass()));
            account.setId(id);

            session.getTransaction().commit();
            return account;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate add Error.", e);
        }
    }

    @Override
    public Account get(int id) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Account.class, id);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate get Error.", e);
        }
    }

    @Override
    public Account get(String email) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (Account) session.createQuery("from Account where email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate get Error.", e);
        }
    }

    @Override
    public boolean exist(String email) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select email from Account where email = :email")
                    .setParameter("email", email)
                    .uniqueResult() != null;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate exist Error.", e);
        }
    }

    @Override
    public boolean confirmPass(String email, String pass) throws AccStorageException {
        return get(email).getPass().equals(pass);
    }

    @Override
    public List<Account> getAll() throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (List<Account>) session.createQuery("from Account").list();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getAll Error.", e);
        }
    }

    @Override
    public void addFriend(Account user, Account friend) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();


            user.getAccountSet().add(friend);
            friend.getAccountSet().add(user);

            session.update(user);
            session.update(friend);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addFriend Error.", e);
        }
    }

    @Override
    public void deleteFriend(Account user, Account friend) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            removeFromFriends(user, friend);
            removeFromFriends(friend, user);

            session.update(user);
            session.update(friend);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate deleteFriend Error.", e);
        }
    }

    private void removeFromFriends(Account userAcc, Account friendAcc) {
        for (Account account : userAcc.getAccountSet()) {
            if (account.equals(friendAcc)) {
                userAcc.getAccountSet().remove(account);
                break;
            }
        }
    }

    @Override
    public boolean isFriend(Account user, Account friend) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            boolean exist = false;
            for (Account account : user.getAccountSet()) {
                if (account.equals(friend)) {
                    exist = true;
                    break;
                }
            }

            session.getTransaction().commit();
            return exist;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate isFriend Error.", e);
        }
    }

    @Override
    public List<Account> getFriends(Account user) throws AccStorageException {
        return Arrays.asList(user.getAccountSet().toArray(new Account[0]));
    }

    @Override
    public Post addPost(Post post, Account user) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            int id = (int) session.createSQLQuery("INSERT INTO Post (ACC_ID, TEXT, DATE) VALUES (:id, :text, now()) RETURNING ID;")
                    .setParameter("id", user.getId())
                    .setParameter("text", post.getText())
                    .getSingleResult();

            session.getTransaction().commit();

            return getPost(id);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addPost Error.", e);
        }
    }

    @Override
    public void deletePost(Post post) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.delete(post);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addPost Error.", e);
        }
    }

    //TODO заменить параметры
    @Override
    public void updatePost(Post oldPost, Post newPost) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createSQLQuery("UPDATE Post SET text=:text WHERE id=:id")
                    .setParameter("text", newPost.getText())
                    .setParameter("id", oldPost.getId())
                    .executeUpdate();

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate updatePost Error.", e);
        }
    }

    @Override
    public Post getPost(int postId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Post.class, postId);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getPosts(int userId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (List<Post>) session.createQuery("From Post where ACC_ID = :id ORDER BY id DESC")
                    .setParameter("id", userId)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getPosts(int userId, int firstPostId, int maxCount) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (List<Post>) session.createQuery("FROM Post WHERE ACC_ID = :acc_id AND ID < :firstPostId ORDER BY id DESC")
                    .setParameter("acc_id", userId)
                    .setParameter("firstPostId", firstPostId)
                    .setMaxResults(maxCount)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getFriendsPosts(Account user, int firstPostId, int maxCount) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            List<Integer> ids = new ArrayList<>();

            for (Account account : getFriends(user)) {
                ids.add(account.getId());
            }

            Iterator results = session.createSQLQuery("SELECT p.id, p.ACC_ID, p.TEXT, p.DATE, a.user_name FROM post as p JOIN accounts as a ON p.ACC_ID=a.id WHERE p.ACC_ID in (:ids) AND p.id < :firstPostId ORDER BY p.id DESC LIMIT :maxCount")
                    .setParameterList("ids", ids)
                    .setParameter("firstPostId", firstPostId)
                    .setParameter("maxCount", maxCount)
                    .getResultList()
                    .iterator();

            List<Post> posts = new ArrayList<>();

            while (results.hasNext()) {
                Object[] row = (Object[]) results.next();
                Post post = new Post();
                post.setId((Integer) row[0]);
                post.setText((String) row[2]);
                Timestamp timestamp = (Timestamp) row[3];
                //TODO Сделать нормально отображение времени
                post.setDate(new Date(timestamp.getTime()).toString());
                post.setUserName((String) row[4]);
                posts.add(post);
            }

            return posts;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public void addMessage(Message message) throws AccStorageException {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.save(message);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addMessage Error");
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
            Iterator results = session.createSQLQuery("SELECT m.id, m.CHAT_ID, m.TEXT, a.user_name FROM message as m JOIN accounts as a ON p.ACC_ID=a.id WHERE chat_id == :chatId AND m.id < :firstMessageId ORDER BY m.id DESC LIMIT :maxCount")
                    .setParameter("chatId", chatId)
                    .setParameter("firstMessageId", firstMessageId)
                    .setParameter("maxCount", maxCount)
                    .getResultList()
                    .iterator();

            List<Message> messages = new ArrayList<>();

            while (results.hasNext()) {
                Object[] row = (Object[]) results.next();
                Message message = new Message();
                message.setId((Integer) row[0]);
                message.setText((String) row[1]);
                message.setName((String) row[2]);
                message.setChatId(chatId);
                messages.add(message);
            }

            return messages;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getMessages Error");
        }
    }

}
