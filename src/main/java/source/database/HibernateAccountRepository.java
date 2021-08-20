package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.exception.AccStorageException;

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
    public void addPost(Post post, Account user) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createSQLQuery("INSERT INTO Post (ACC_ID, TEXT, DATE) " +
                    "VALUES (" + user.getId() + ", '" + post.getText() + "', now())").executeUpdate();

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

    //TODO заменить параметры
    @Override
    public void updatePost(Post oldPost, Post newPost) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createSQLQuery("UPDATE Post " +
                    "SET text='" + newPost.getText() + "' " +
                    "WHERE id=" + oldPost.getId() + ";").executeUpdate();

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
    public List<Post> getPosts(int userId, int count) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (List<Post>) session.createQuery("FROM Post WHERE ACC_ID = :id ORDER BY id DESC")
                    .setParameter("id", userId)
                    .setMaxResults(count)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getPosts(int userId, int firstPostId, int maxCount) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            return (List<Post>) session.createQuery("FROM Post WHERE ACC_ID = :acc_id " +
                    "AND ID < :firstPostId ORDER BY id DESC")
                    .setParameter("acc_id", userId)
                    .setParameter("firstPostId", firstPostId)
                    .setMaxResults(maxCount)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getFriendsPosts(Account user) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            List<Integer> ids = new ArrayList<>();

            for (Account account : getFriends(user)) {
                ids.add(account.getId());
            }

            return (List<Post>) session.createQuery("FROM Post WHERE ACC_ID IN (:ids) ORDER BY id DESC")
                    .setParameterList("ids", ids)
                    .getResultList();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    @Override
    public List<Post> getFriendsPosts(Account user, int count) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            List<Integer> ids = new ArrayList<>();

            for (Account account : getFriends(user)) {
                ids.add(account.getId());
            }

            return (List<Post>) session.createQuery("FROM Post WHERE ACC_ID IN (:ids) ORDER BY id DESC")
                    .setParameterList("ids", ids)
                    .setMaxResults(count)
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

            Iterator results = session.createQuery("SELECT (Post.id, Post.ACC_ID, Post.TEXT, Post.DATE, Accounts.user_name) " +
                    "FROM Post join Accounts on Post.ACC_ID=Accounts.id WHERE Post.ACC_ID IN (:ids) " +
                    "AND Post.ID < :firstPostId ORDER BY Post.id DESC")
                    .setParameterList("ids", ids)
                    .setParameter("firstPostId", firstPostId)
                    .setMaxResults(maxCount)
                    .getResultList()
                    .iterator();

            List<Post> posts = new ArrayList<>();

            while (results.hasNext()) {
                Object[] row = (Object[]) results.next();
                Post post = new Post();
                post.setId((Integer) row[0]);
                post.setText((String) row[2]);
                post.setDate((String) row[3]);
                post.setUserName((String) row[4]);
                posts.add(post);
            }

//            StringBuilder ids = new StringBuilder();
//            List<Account> friends = getFriends(user);
//            for (int i = 0; i < friends.size()-1; i++) {
//                ids.append(friends.get(i).getId()).append(", ");
//            }
//            ids.append(friends.get(friends.size()-1));
//
//            Iterator results = session.createSQLQuery("SELECT (Post.id, Post.ACC_ID, Post.TEXT, Post.DATE, Accounts.user_name) " +
//                    "FROM Post join Accounts on Post.ACC_ID=Accounts.id WHERE Post.ACC_ID IN (" + ids + ") " +
//                    "AND Post.ID < " + firstPostId +" ORDER BY Post.id DESC LIMIT " + maxCount + ";")
//                    .list()
//                    .iterator();
//
//            List<Post> posts = new ArrayList<>();

            return posts;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    //TODO Убрать
    @Override
    public int getFriendsPostsLength(Account user) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            List<Integer> ids = new ArrayList<>();

            for (Account account : getFriends(user)) {
                ids.add(account.getId());
            }

            Object result = session.createQuery("SELECT COUNT(*) FROM Post WHERE ACC_ID IN (:ids)")
                    .setParameterList("ids", ids)
                    .uniqueResult();
            return Integer.parseInt(result.toString());
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }

    //TODO Убрать
    @Override
    public int getPostsLength(int userId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            Object result = session.createQuery("SELECT COUNT(*) FROM Post WHERE ACC_ID = :id")
                    .setParameter("id", userId)
                    .uniqueResult();
            return Integer.parseInt(result.toString());
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getPost Error.", e);
        }
    }
}
