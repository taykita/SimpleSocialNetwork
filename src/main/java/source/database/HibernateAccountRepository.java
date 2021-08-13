package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import source.controllers.entity.Post;
import source.exception.AccStorageException;
import source.controllers.entity.Account;
import sun.security.util.Password;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class HibernateAccountRepository implements AccountRepository {
    @Autowired
    public HibernateAccountRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    PasswordEncoder passwordEncoder;

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
    public void addPost(Post post, Account user) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            post.setUserName(user.getUserName());
            post.setAccount(user);

            session.save(post);
            session.getTransaction().commit();
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

    @Override
    public void updatePost(Post oldPost, Post newPost) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            oldPost.setText(newPost.getText());
            session.update(oldPost);

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
            throw new AccStorageException("Hibernate addPost Error.", e);
        }
    }

    //TODO Заменить на объектное представление
    @Override
    public List<Post> getFriendsPosts(Account user) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            List<Integer> ids = new ArrayList<>();

            for (Account account: getFriends(user)) {
                ids.add(account.getId());
            }

            return (List<Post>) session.createQuery("FROM Post WHERE ACC_ID IN (:ids) ORDER BY id DESC")
                    .setParameterList("ids", ids)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addPost Error.", e);
        }
    }
}
