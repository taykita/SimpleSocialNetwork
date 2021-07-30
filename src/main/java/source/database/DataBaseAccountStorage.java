package source.database;

import source.exception.AccStorageException;
import source.verification.Account;

import java.util.List;

public class DataBaseAccountStorage implements AccountStorage {

    public DataBaseAccountStorage() {
        this.queryController = new QueryController();
    }

    private final QueryController queryController;

    @Override
    public Account add(Account account) throws AccStorageException {
        int id = queryController.queryAdd("INSERT INTO accounts (email, name, pass) " +
                "VALUES ('" + account.getEmail() + "', '" + account.getUserName() + "', '" + account.getPass() + "') " +
                "RETURNING id;",
                "INSERT INTO user_list (name, avatar) " +
                "VALUES ('" + account.getUserName() + "', 'resources/images/avatars/1.png')");
        account.setId(id);
        return account;
    }

    @Override
    public Account get(int id) throws AccStorageException {
        return queryController.getQuery("SELECT * FROM accounts WHERE id =" + id + ";");
    }

    @Override
    public Account get(String email) throws AccStorageException {
        return queryController.getQuery("SELECT * FROM accounts WHERE email ='" + email + "';");
    }

    @Override
    public boolean exist(String email) throws AccStorageException {
        return queryController.existQuery("SELECT EXISTS(SELECT email FROM accounts WHERE email='" + email + "');");
    }

    @Override
    public boolean confirmPass(String email, String pass) throws AccStorageException {
        return get(email).getPass().equals(pass);
    }

    @Override
    public List<Account> getAll() throws AccStorageException {
        return queryController.getAllQuery("SELECT * FROM accounts;");
    }

    @Override
    public void addFriend(int userId, int friendId) throws AccStorageException {
        queryController.addLinkQuery("INSERT INTO accounts_user_list (acc_id, list_id) VALUES (" + userId + ", " + friendId + ");");
        queryController.addLinkQuery("INSERT INTO accounts_user_list (acc_id, list_id) VALUES (" + friendId + ", " + userId + ");");
    }

    @Override
    public boolean isFriend(int userId, int friendId) throws AccStorageException {
        return queryController.existQuery("SELECT EXISTS(SELECT acc_id, list_id FROM accounts_user_list " +
                "WHERE acc_id=" + userId + " AND list_id= " + friendId +");");
    }

    @Override
    public List<Account> getFriends(int userId) throws AccStorageException {
        return queryController.getAllQuery("SELECT * FROM  accounts_user_list LEFT JOIN accounts \n" +
                    "ON id = acc_id \n" +
                    "WHERE accounts_user_list.list_id = "+ userId +";");
    }

}

