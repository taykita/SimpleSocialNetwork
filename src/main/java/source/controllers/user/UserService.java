package source.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.main.MainService;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserService(AccountRepository accountRepository,
                       MainService mainService) {
        this.accountRepository = accountRepository;
        this.mainService = mainService;
    }

    private final AccountRepository accountRepository;
    private final MainService mainService;

    public List<Account> getAllAccounts() throws AccStorageException {
        return accountRepository.getAllAccounts();
    }

    public Account getAccount(int id) throws AccStorageException {
        return accountRepository.getAccount(id);
    }

    public boolean isFriend(Account user, Account friend) throws AccStorageException{
        return accountRepository.isFriend(user, friend);
    }

    public List<Post> getPosts(int id, int firstPostId) throws AccStorageException {
        return mainService.getPosts(id, firstPostId);
    }
}
