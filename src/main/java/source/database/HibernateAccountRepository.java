package source.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import source.exception.AccStorageException;
import source.controllers.authorization.entity.Account;

import java.util.Arrays;
import java.util.List;

@Repository
public class HibernateAccountRepository implements AccountRepository {
    @Autowired
    public HibernateAccountRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final SessionFactory sessionFactory;

    @Override
    public Account add(Account account) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            int id = (Integer) session.save(account);
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
            session.beginTransaction();

            Account account = session.get(Account.class, id);

            session.getTransaction().commit();
            return account;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate get Error.", e);
        }
    }

    @Override
    public Account get(String email) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Account account = (Account) session.createQuery("from Account where email = :email")
                    .setParameter("email", email)
                    .getSingleResult();

            session.getTransaction().commit();
            return account;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate get Error.", e);
        }
    }

    @Override
    public boolean exist(String email) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            boolean exist = session.createQuery("select email from Account where email = :email")
                    .setParameter("email", email)
                    .uniqueResult() != null;

            session.getTransaction().commit();
            return exist;
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
            session.beginTransaction();

            List<Account> accounts = session.createQuery("from Account").list();

            session.getTransaction().commit();
            return accounts;
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate getAll Error.", e);
        }
    }

    @Override
    public void addFriend(int userId, int friendId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Account userAcc = get(userId);
            Account friendAcc = get(friendId);

            userAcc.getAccountSet().add(friendAcc);
            friendAcc.getAccountSet().add(userAcc);

            session.update(userAcc);
            session.update(friendAcc);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new AccStorageException("Hibernate addFriend Error.", e);
        }
    }

    @Override
    public boolean isFriend(int userId, int friendId) throws AccStorageException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            boolean exist = false;
            Account friendAccount = get(friendId);
            for (Account account : get(userId).getAccountSet()) {
                if (account.equals(friendAccount)) {
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
    public List<Account> getFriends(int userId) throws AccStorageException {
        Account account = get(userId);
        return Arrays.asList(account.getAccountSet().toArray(new Account[0]));
    }
}
