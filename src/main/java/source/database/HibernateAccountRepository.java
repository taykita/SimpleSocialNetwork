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
            return (Account) session.getNamedNativeQuery("Account.getAccount")
                    .setParameter("email", email)
                    .addEntity(Account.class)
                    .getSingleResult();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate get Error.", e);
        }
    }

    @Override
    public boolean existAccount(String email) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return session.getNamedNativeQuery("Account.existAccount")
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
            return (List<Account>) session.getNamedNativeQuery("Account.getAllAccounts")
                    .addEntity(Account.class)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getAll Error.", e);
        }
    }

    @Override
    public void addFriend(Account user, Account friend) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();
            try {
                session.getNamedNativeQuery("Account.addFriend")
                        .setParameter("accId", user.getId())
                        .setParameter("userId", friend.getId())
                        .executeUpdate();

                session.getNamedNativeQuery("Account.addFriend")
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
                session.getNamedNativeQuery("Account.deleteFriend")
                        .setParameter("accId", user.getId())
                        .setParameter("userId", friend.getId())
                        .executeUpdate();

                session.getNamedNativeQuery("Account.deleteFriend")
                        .setParameter("accId", friend.getId())
                        .setParameter("userId", user.getId())
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
                return session.getNamedNativeQuery("Account.isFriend")
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

            List<Account> friends = session.getNamedNativeQuery("Account.getFriends")
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
                int id = (int) session.getNamedNativeQuery("Account.addPost")
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
                session.getNamedNativeQuery("Account.deletePost")
                        .setParameter("id", post.getId())
                        .executeUpdate();

                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate deletePost Error.", e);
        }
    }

    //TODO заменить параметры
    @Override
    public void updatePost(Post oldPost, Post newPost) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.getNamedNativeQuery("Account.updatePost")
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
            return (Post) session.getNamedNativeQuery("Account.getPost")
                    .setParameter("id", postId)
                    .addEntity(Post.class)
                    .getSingleResult();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getPosts(int userId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (List<Post>) session.getNamedNativeQuery("Account.getAllUsersPosts")
                    .setParameter("id", userId)
                    .addEntity(Post.class)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getPosts(int userId, int firstPostId, int maxCount) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (List<Post>) session.getNamedNativeQuery("Account.getPosts")
                    .setParameter("acc_id", userId)
                    .setParameter("firstPostId", firstPostId)
                    .setMaxResults(maxCount)
                    .addEntity(Post.class)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getFriendsPosts(Account user, int firstPostId, int maxCount) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            List<Integer> ids = new ArrayList<>();
//TODO Как сделать nullable username
            for (Account account : getFriends(user.getId())) {
                ids.add(account.getId());
            }

            Iterator results = session.getNamedNativeQuery("Account.getFriendsPosts")
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
                post.setDate((Timestamp) row[3]);
                post.setUserName((String) row[4]);
                posts.add(post);
            }

            return posts;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }


}
