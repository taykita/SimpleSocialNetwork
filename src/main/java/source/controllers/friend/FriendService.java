package source.controllers.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

@Service
public class FriendService {
    @Autowired
    public FriendService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    public void addFriend(int id, User activeUser) throws AccStorageException {
        Account user = accountRepository.getAccount(activeUser.getId());
        accountRepository.addFriend(user, accountRepository.getAccount(id));
    }

    public void deleteFriend(int id, User activeUser) throws AccStorageException {
        Account user = accountRepository.getAccount(activeUser.getId());
        accountRepository.deleteFriend(user, accountRepository.getAccount(id));
    }
}
