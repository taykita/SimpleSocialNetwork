package source.controllers.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.controllers.post.PostService;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Service
public class MainService {
    @Autowired
    public MainService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Value("${post.pagination.count}")
    private int postCount;

    public Account getAccount(User activeUser) throws AccStorageException {
        return accountRepository.getAccount(activeUser.getId());
    }

    public void checkAndUpdate(User activeUser, Account newAccount,
                               String chPass, String oldPass) throws AccStorageException {
        Account currentAccount = accountRepository.getAccount(activeUser.getId());

        boolean isEdit = false;

        if (!currentAccount.getName().equals(newAccount.getName())) {
            currentAccount.setName(newAccount.getName());
            isEdit = true;
        }
        if (!currentAccount.getEmail().equals(newAccount.getEmail())) {
            if (!accountRepository.existAccount(newAccount.getEmail())) {
                currentAccount.setEmail(newAccount.getEmail());
                isEdit = true;
            }
        }
        //TODO Изменить кодировку
        if (!newAccount.getPass().equals("") &&
                currentAccount.getPass().equals(passwordEncoder.encode(oldPass)) &&
                newAccount.getPass().equals(chPass)) {
            currentAccount.setPass(passwordEncoder.encode(newAccount.getPass()));
            isEdit = true;
        }
        if (isEdit) {
            accountRepository.updateAccount(currentAccount);
        }
    }

    public List<Post> getPosts(int userId, int firstPostId) throws AccStorageException {
        return accountRepository.getPosts(userId, firstPostId, postCount);
    }
}
