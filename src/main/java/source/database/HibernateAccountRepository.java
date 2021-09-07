package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.exception.AccStorageException;

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
    public Account addAccount(Account account) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int id = (Integer) session.save(account);
                account.setPass(passwordEncoder.encode(account.getPass()));
                account.setId(id);

                transaction.commit();
                return account;
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate add Error.", e);
        }
    }

    @Override
    public Account updateAccount(Account account) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(account);

                transaction.commit();
                return account;
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate add Error.", e);
        }
    }

    @Override
    public Account getAccount(int id) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Account.class, id);
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate get Error.", e);
        }
    }

    @Override
    public Account getAccount(String email) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (Account) session.createQuery("from Account where email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate get Error.", e);
        }
    }

    @Override
    public boolean existAccount(String email) throws AccStorageException {
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
        return getAccount(email).getPass().equals(pass);
    }

    @Override
    public List<Account> getAllAccounts() throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (List<Account>) session.createQuery("from Account").list();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getAll Error.", e);
        }
    }

    @Override
    public void addFriend(Account user, Account friend) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();
            try {
                session.createSQLQuery("INSERT INTO Accounts_Accounts (ACC_ID, USER_ID) VALUES (:accId, :userId);")
                        .setParameter("accId", user.getId())
                        .setParameter("userId", friend.getId())
                        .executeUpdate();

                session.createSQLQuery("INSERT INTO Accounts_Accounts (ACC_ID, USER_ID) VALUES (:accId, :userId);")
                        .setParameter("accId", friend.getId())
                        .setParameter("userId", user.getId())
                        .executeUpdate();


                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }


        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addFriend Error.", e);
        }
    }

    @Override
    public void deleteFriend(Account user, Account friend) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createSQLQuery("DELETE FROM Accounts_Accounts WHERE ACC_ID = :accId AND USER_ID = :userId")
                        .setParameter("accId", user.getId())
                        .setParameter("userId", friend.getId())
                        .executeUpdate();

                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate deleteFriend Error.", e);
        }
    }

    @Override
    public boolean isFriend(Account user, Account friend) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                return session.createSQLQuery("SELECT * FROM Accounts_Accounts WHERE ACC_ID = :accId AND USER_ID = :userId")
                        .setParameter("accId", user.getId())
                        .setParameter("userId", friend.getId())
                        .uniqueResult() != null;

            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate isFriend Error.", e);
        }
    }

    @Override
    public List<Account> getFriends(int userId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            List<Account> friends = session.createSQLQuery("SELECT a.id, a.email, a.pass, a.user_name FROM Accounts as a JOIN Accounts_Accounts as aa ON a.id = aa.user_id WHERE aa.acc_id = :userId")
                    .setParameter("userId", userId)
                    .addEntity(Account.class)
                    .getResultList();

            return friends;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public Post addPost(Post post, Account user) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int id = (int) session.createSQLQuery("INSERT INTO Post (ACC_ID, TEXT, DATE) VALUES (:id, :text, now()) RETURNING ID;")
                        .setParameter("id", user.getId())
                        .setParameter("text", post.getText())
                        .getSingleResult();

                transaction.commit();

                return getPost(id);
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addPost Error.", e);
        }
    }

    @Override
    public void deletePost(Post post) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.delete(post);

                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addPost Error.", e);
        }
    }

    //TODO заменить параметры
    @Override
    public void updatePost(Post oldPost, Post newPost) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createSQLQuery("UPDATE Post SET text=:text WHERE id=:id")
                        .setParameter("text", newPost.getText())
                        .setParameter("id", oldPost.getId())
                        .executeUpdate();

                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
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

            for (Account account : getFriends(user.getId())) {
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
                post.setDate(timestamp.toString());
                post.setUserName((String) row[4]);
                posts.add(post);
            }

            return posts;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }


}
